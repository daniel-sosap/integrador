package com.integracion.sdp.controller;

public class IncidenteRequest {

    private int numeroIncidente;
    private String estado;


    public int getNumeroIncidente() {
        return numeroIncidente;
    }

    public void setNumeroIncidente(int numeroIncidente) {
        this.numeroIncidente = numeroIncidente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

