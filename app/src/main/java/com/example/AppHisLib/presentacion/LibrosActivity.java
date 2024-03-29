package com.example.AppHisLib.presentacion;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AppHisLib.R;
import com.example.AppHisLib.casosdeuso.AdaptadorListaLibros;
import com.example.AppHisLib.casosdeuso.Libros;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que implementa un adaptador de los libros personales
 */
public class LibrosActivity extends BaseActivity implements Serializable {

    FloatingActionButton anadirLibro;
    BottomNavigationView btnNavegacion;
    ActionBar actionBar;
    RecyclerView libros;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    private String usuario;
    DatabaseReference myRef;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.libros);

        /**
         * Datos relacionados al menu superior de la app
         */
        actionBar = getSupportActionBar();
        actionBar.setTitle("Libros");

        anadirLibro = findViewById(R.id.anadirLibro);
        btnNavegacion = findViewById(R.id.btnNavegacion);

        /**
         * En caso de que el boton flotante se seleccione
         * procederá a realizar un intent enviando el valor true
         * de que se va a crear un nuevo libro
         */
        anadirLibro.setOnClickListener(v -> {
            Intent intent = new Intent(LibrosActivity.this, AnadirLibroActivity.class);
            intent.putExtra("AnadirLibro",true);
            startActivity(intent);
        });

        btnNavegacion.setOnNavigationItemSelectedListener(this);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        /**
         * Obtengo toda la informacion relacionada con el cada libro
         * para poder hacer uso de un adaptador que se encargue de manejar todos esos datos
         */
        myRef = db.getReference().child("Usuarios").child(usuario).child("Libros");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Libros> listaLibros = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    String autor = ds.child("Autor").getValue(String.class);
                    String descripcion = ds.child("Descripcion").getValue(String.class);
                    String foto = ds.child("Foto").getValue(String.class);
                    String genero = ds.child("Genero").getValue(String.class);
                    String Id = ds.child("Id").getValue(String.class);
                    String valoracion = ds.child("Valoracion").getValue(String.class);
                    Libros libro = new Libros(autor,descripcion,genero,foto,valoracion,Id);

                    listaLibros.add(libro);
                }

                libros = findViewById(R.id.rvListaLibros);
                adapter = new AdaptadorListaLibros(LibrosActivity.this,listaLibros);
                layoutManager = new LinearLayoutManager(LibrosActivity.this);
                libros.setLayoutManager(layoutManager);
                libros.setHasFixedSize(true);
                libros.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Metodo para actualizar el estado del menu inferior
     */
    private void updateNavigationBarState(){
        int actionId = getBottomNavigationMenuItemId();
        selectedBottomNavigationBarItem(actionId);
    }

    /**
     * Metodo que sirve para decirle al menu que estamos en este activity
     * @param itemId
     */
    void selectedBottomNavigationBarItem(int itemId){
        MenuItem item = btnNavegacion.getMenu().findItem(itemId);
        item.setChecked(true);
    }


    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    /**
     * Metodo para volver a obtener los datos al pasar por el ciclo de vida de la app
     */
    @Override
    public void onResume() {
        super.onResume();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = db.getReference().child("Usuarios").child(usuario).child("Libros");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Libros> listaLibros = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()){
                    String autor = ds.child("Autor").getValue(String.class);
                    String descripcion = ds.child("Descripcion").getValue(String.class);
                    String foto = ds.child("Foto").getValue(String.class);
                    String genero = ds.child("Genero").getValue(String.class);
                    String Id = ds.child("Id").getValue(String.class);
                    String valoracion = ds.child("Valoracion").getValue(String.class);
                    Libros libro = new Libros(autor,descripcion,genero,foto,valoracion,Id);

                    listaLibros.add(libro);
                }

                libros = findViewById(R.id.rvListaLibros);
                adapter = new AdaptadorListaLibros(LibrosActivity.this,listaLibros);
                layoutManager = new LinearLayoutManager(LibrosActivity.this);
                libros.setLayoutManager(layoutManager);
                libros.setHasFixedSize(true);
                libros.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    /**
     * Metodo para poder interactuar con los elementos del menu inferior
     * @param item
     * @return
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        btnNavegacion.postDelayed(() -> {
            int itemId = item.getItemId();
            Intent intent;
            if(itemId == R.id.irPerfil){
                intent = new Intent(this, PerfilActivity.class);
                startActivity(intent);
            }
            if(itemId == R.id.irPrincipal){
                intent = new Intent(this, ContentMainActivity.class);
                startActivity(intent);
            }
            if(itemId == R.id.irLibros){
                intent = new Intent(this,LibrosActivity.class);
                startActivity(intent);
            }
            finish();
        },300);
        return true;
    }


    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.irLibros;
    }

    @Override
    int getLayoutId() {
        return R.layout.libros;
    }
}