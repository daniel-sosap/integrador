package com.integracion.sdp.dto;
import org.springframework.web.multipart.MultipartFile;

public class ActualizaIncidenteDTO {
    private String notas;

    public String getNotas() {
        return notas;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public MultipartFile getAdjunto() {
        return adjunto;
    }

    public void setAdjunto(MultipartFile adjunto) {
        this.adjunto = adjunto;
    }

    private MultipartFile adjunto;


}
