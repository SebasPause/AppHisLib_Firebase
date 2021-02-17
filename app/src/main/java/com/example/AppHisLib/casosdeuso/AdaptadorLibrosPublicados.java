package com.example.AppHisLib.casosdeuso;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.AppHisLib.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AdaptadorLibrosPublicados extends RecyclerView.Adapter<AdaptadorLibrosPublicados.LibrosPublicadosViewHolder> {

    private Context contexto;
    private String usuario;
    DatabaseReference myRef;
    List<Libros> libros;
    Uri uri;


    public AdaptadorLibrosPublicados(Context contexto, List<Libros> libros){
        this.contexto = contexto;
        this.libros = libros;
    }

    @NonNull
    @Override
    public LibrosPublicadosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(contexto).inflate(R.layout.lista_libros,parent,false);
        AdaptadorLibrosPublicados.LibrosPublicadosViewHolder librospublicadosViewHolder = new AdaptadorLibrosPublicados.LibrosPublicadosViewHolder(v);
        return librospublicadosViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LibrosPublicadosViewHolder holder, int position) {
        String autor = libros.get(position).Autor;
        String descripcion = libros.get(position).Descripcion;
        String genero = libros.get(position).Genero;
        String foto = libros.get(position).Foto;
        String valoracion = libros.get(position).Valoracion;
        String id = libros.get(position).Id;

        uri = Uri.parse(foto);

        holder.txtAutor.setText(autor);
        holder.txtDescripcion.setText(descripcion);
        holder.txtGenero.setText(genero);
        holder.imagenListaLibros.setImageURI(uri);
        holder.ratingBar.setRating(Float.parseFloat(valoracion));

        usuario = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseStorage mStorage = FirebaseStorage.getInstance();
        StorageReference storageRef = mStorage.getReference().child("Imagenes").child(usuario).child("Libros").child(id).child("Libro.jpeg");
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(contexto)
                        .load(uri)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)         //ALL or NONE as your requirement
                        .into(holder.imagenListaLibros);
            }
        });
    }

    @Override
    public int getItemCount() {
        return libros.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class LibrosPublicadosViewHolder extends  RecyclerView.ViewHolder{
        CardView cvListaLibros;
        ImageButton imagenOpciones;
        RatingBar ratingBar;
        ImageView imagenListaLibros;
        TextView txtAutor, txtDescripcion, txtGenero;


        public LibrosPublicadosViewHolder(@NonNull View itemView) {
            super(itemView);
            cvListaLibros = (CardView)itemView.findViewById(R.id.cvListaLibros);
            imagenOpciones = (ImageButton)itemView.findViewById(R.id.imagen_opciones);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            imagenListaLibros = (ImageView)itemView.findViewById(R.id.imgListaLibros);
            txtAutor = (TextView)itemView.findViewById(R.id.txtAutor);
            txtDescripcion = (TextView)itemView.findViewById(R.id.txtDescripcion);
            txtGenero = (TextView)itemView.findViewById(R.id.txtGenero);


        }
    }
}