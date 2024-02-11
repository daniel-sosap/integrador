package com.integracion.sdp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@Data
public class IncidentModel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String idsdp;
    private String id_cliente;
    private String descripcion;
    private String id_categorizacion;
    private byte[] archivoAdjunto;
    private String nombreadjunto;
    // Métodos para convertir de MultipartFile a byte[] y viceversa
    public void setArchivoAdjunto(MultipartFile archivo) {
        try {
            this.archivoAdjunto = archivo.getBytes();
        } catch (IOException e) {
            // Manejar la excepción apropiadamente
        }
    }

}