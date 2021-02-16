package com.example.AppHisLib.presentacion;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;

import com.example.AppHisLib.R;
import com.example.AppHisLib.casosdeuso.DatosPerfil;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PerfilActivity extends BaseActivity {

    ImageView imgEditarPerfil;
    TextView txtAutor,txtDescripcion;
    BottomNavigationView btnNavegacion;
    private String usuario;
    DatabaseReference myRef;
    Uri uri;
    ActionBar actionBar;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Perfil");


        txtAutor = (TextView)findViewById(R.id.txtAutor);
        txtDescripcion = (TextView)findViewById(R.id.txtDescripcion);
        imgEditarPerfil = (ImageView)findViewById(R.id.imgEditarPerfil);

        btnNavegacion = (BottomNavigationView)findViewById(R.id.btnNavegacion);

        btnNavegacion.setOnNavigationItemSelectedListener(this);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        myRef = db.getReference().child("Usuarios").child(usuario).child("Perfil");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatosPerfil datosPerfil = snapshot.getValue(DatosPerfil.class);
                txtAutor.setText(datosPerfil.Autor);
                txtDescripcion.setText(datosPerfil.Descripcion);
                uri = Uri.parse(datosPerfil.Foto);
                imgEditarPerfil.setImageURI(uri);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void updateNavigationBarState(){
        int actionId = getBottomNavigationMenuItemId();
        selectedBottomNavigationBarItem(actionId);
    }

    void selectedBottomNavigationBarItem(int itemId){
        MenuItem item = btnNavegacion.getMenu().findItem(itemId);
        item.setChecked(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.btnEditarPerfil){
            lanzarEditarPerfil(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void lanzarEditarPerfil(View view) {
        Intent intent = new Intent(this, EditarPerfilActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        btnNavegacion.postDelayed(() -> {
            int itemId = item.getItemId();
            Intent intent;
            if(itemId == R.id.irPerfil){
                intent = new Intent(this,PerfilActivity.class);
                startActivity(intent);
            }
            if(itemId == R.id.irPrincipal){
                intent = new Intent(this, ContentMainActivity.class);
                startActivity(intent);
            }
            if(itemId == R.id.irLibros){
                intent = new Intent(this, LibrosActivity.class);
                startActivity(intent);
            }
            finish();
        },300);
        return true;
    }


    @Override
    int getBottomNavigationMenuItemId() {
        return R.id.irPerfil;
    }

    @Override
    int getLayoutId() {
        return R.layout.perfil;
    }

}