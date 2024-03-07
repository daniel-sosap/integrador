package com.integracion.sdp.model;

public class AdjuntoInfo {
    private String nombre;
    private String ruta;

    public AdjuntoInfo(String nombre, String ruta) {
        this.nombre = nombre;
        this.ruta = ruta;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRuta() {
        return ruta;
    }
}