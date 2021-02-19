package com.example.AppHisLib.presentacion;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.AppHisLib.R;
import com.example.AppHisLib.casosdeuso.AdaptadorLibrosPublicados;
import com.example.AppHisLib.casosdeuso.AdaptadorListaLibros;
import com.example.AppHisLib.casosdeuso.CrearEstructura;
import com.example.AppHisLib.casosdeuso.Libros;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ContentMainActivity extends BaseActivity implements Serializable{

    private String usuario;
    DatabaseReference myRef,myRef2;
    BottomNavigationView btnNavegacion;
    ActionBar actionBar;
    CrearEstructura ce;
    RecyclerView librosPublicados;
    RecyclerView.Adapter adapter2;
    RecyclerView.LayoutManager layoutManager2;
    List<Libros> listaLibrosPublicados;
    Boolean accion;

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Principal");

        usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = FirebaseDatabase.getInstance().getReference("Usuarios");
        myRef2 = FirebaseDatabase.getInstance().getReference("Usuarios");
        ce = new CrearEstructura(ContentMainActivity.this,usuario,myRef);
        ce.crearEstructuraDatos();
        Bundle extras = getIntent().getExtras();
        if(extras==null){
            //nada
            accion = false;
            System.out.println("Si extra es null: "+listaLibrosPublicados.size());
        }else{
            accion = extras.getBoolean("Accion");
        }
        if(accion){
            extras = getIntent().getExtras();
            listaLibrosPublicados = (List<Libros>) extras.getSerializable("ListaLibrosPublicados");
            System.out.println("Si extra es true: "+listaLibrosPublicados.size());

            librosPublicados = findViewById(R.id.rvListaLibrosPublicados);
            adapter2 = new AdaptadorLibrosPublicados(ContentMainActivity.this,listaLibrosPublicados);
            layoutManager2 = new LinearLayoutManager(ContentMainActivity.this);
            librosPublicados.setLayoutManager(layoutManager2);
            librosPublicados.setHasFixedSize(true);
            librosPublicados.setAdapter(adapter2);
            accion=false;
        }

        btnNavegacion = (BottomNavigationView)findViewById(R.id.btnNavegacion);

        btnNavegacion.setOnNavigationItemSelectedListener(this);


    } //fin onCreate

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        btnNavegacion.postDelayed(() -> {
            int itemId = item.getItemId();
            Intent intent;
            if(itemId == R.id.irPerfil){
                intent = new Intent(this, PerfilActivity.class);
                intent.putExtra("ListaLibrosPublicados", (Serializable) listaLibrosPublicados);
                startActivity(intent);
            }
            if(itemId == R.id.irPrincipal){
                intent = new Intent(this,ContentMainActivity.class);
                intent.putExtra("ListaLibrosPublicados", (Serializable) listaLibrosPublicados);
                startActivity(intent);
            }
            if(itemId == R.id.irLibros){
                intent = new Intent(this, LibrosActivity.class);
                intent.putExtra("ListaLibrosPublicados", (Serializable) listaLibrosPublicados);
                startActivity(intent);
            }
            finish();
        },300);
        return true;
    }


    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.irPrincipal;
    }

    @Override
    int getLayoutId() {
        return R.layout.content_main;
    }
}