package com.integracion.sdp.dto;

public class ActualizaEstadoIncidenteDTO {
    private String status;
    private String motivo;
    private String resolucion;

    // Constructor, getters y setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResolucion() {
        return resolucion;
    }

    public String getMotivo() { return motivo;}

    public void setResolucion(String resolucion) {
        this.resolucion = resolucion;
    }

    public void setMotivo(String motivo) {this.motivo = motivo;}

}
