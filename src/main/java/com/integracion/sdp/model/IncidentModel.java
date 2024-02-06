package com.integracion.sdp.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

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
    //private byte[] archivo1;
}