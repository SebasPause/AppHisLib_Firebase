package com.example.AppHisLib.casosdeuso;

import java.util.List;

public class Valoracion {
    private String comentario,valor;
    
    public Valoracion(){}

    public Valoracion(String comentario, String valor) {
        this.comentario = comentario;
        this.valor = valor;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public List<Valoracion> valoraciones;

}