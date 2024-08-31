package com.integracion.sdp.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Data
public class IncidentModel {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;
    private String idsdp;
    private String id_cliente;
    private String cliente;
    private String descripcion;
    private String id_categorizacion;
    private String resumen;
    private String impacto;
    private String urgencia;
    private String id_agente;
    private String rfc_corto_cliente;
    private String rfc_corto_contacto;
    private String direccion;
    private String id_activo;
    private String id_ticket_padre;
    private String adjunto_nombre;
    private LocalDate fecha_envio;
    private byte[] archivoAdjunto;
    private String nombreadjunto;
    private String resultadoMensaje;
    private String estadoTransaccion;
    // Métodos para convertir de MultipartFile a byte[] y viceversa
    public void setArchivoAdjunto(MultipartFile archivo) {
        try {
            this.archivoAdjunto = archivo.getBytes();
        } catch (IOException e) {
            // Manejar la excepción apropiadamente
        }
    }

}