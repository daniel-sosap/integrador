package com.integracion.sdp.utils;

import com.integracion.sdp.gen.IncidentUpdate;
import com.integracion.sdp.gen.WoUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WSDLStatusWoRequest {

    private static final Logger logger = LoggerFactory.getLogger(WSDLStatusWoRequest.class);

    public void imprimeWoUpdate(WoUpdate woUpdate){
        logger.info("***********Valores recibidos************\n" +
                "\nIdsdp: " + woUpdate.getIdsdp() +
                "\ncliente: " + woUpdate.getCliente()+
                "\nstatus: " + woUpdate.getStatus() +
                "\nmotivo actualizacion: " + woUpdate.getMotivoActualizacion() +
//                "\nimpacto: " + woUpdate.getImpacto() +
//                "\nurgencia: " + woUpdate.getUrgencia() +
                "\nPrioridad: " + woUpdate.getPrioridad() +
//                "\nid_agente: " + woUpdate.getIdAgente() +
                "\nrfc_corto_cliente: " + woUpdate.getRfcCortoCliente() +
                "\nrfc_corto_contacto: " + woUpdate.getRfcCortoContacto() +
                "\ndireccion: " + woUpdate.getDireccion() +
                "\ntoken: " + woUpdate.getToken());
    }

    public String validaWoUpdate(WoUpdate woUpdate){

        List<String> camposNulos = new ArrayList<>();

        if (woUpdate.getIdsdp() == null || woUpdate.getIdsdp().trim().isEmpty()) {
            camposNulos.add("IdSDP");
        }
        if (woUpdate.getCliente() == null || woUpdate.getCliente().trim().isEmpty()) {
            camposNulos.add("Cliente");

        }

        if (woUpdate.getStatus() == null || woUpdate.getStatus().trim().isEmpty()) {
            if (woUpdate.getStatus() == null || woUpdate.getStatus().trim().isEmpty())
            camposNulos.add("Status");
        }


        // Construir el mensaje de campos nulos
        StringBuilder mensajeCamposNulos = new StringBuilder();
        if (!camposNulos.isEmpty()) {
            mensajeCamposNulos.append("Los siguientes campos son nulos o vacíos: ");
            for (int i = 0; i < camposNulos.size(); i++) {
                mensajeCamposNulos.append(camposNulos.get(i));
                if (i < camposNulos.size() - 1) {
                    mensajeCamposNulos.append(", ");
                }
            }
        }
        System.out.println(mensajeCamposNulos.toString());
        return mensajeCamposNulos.toString();

    }

    public String mapeoImpacto(String impacto){
        String impactoMapeo = null;
        switch (impacto) {
            case "1000":
                impactoMapeo = "1-Extenso/Generalizado";
                break;
            case "2000":
                impactoMapeo = "2-Significativo/Amplio";
                break;
            case "3000":
                impactoMapeo = "3-Moderado/Limitado";
                break;
            case "4000":
                impactoMapeo = "4-Menor/Localizado";
                break;
            default:
                impactoMapeo = impacto;
                break;
        }
        return impactoMapeo;
    }

    public String mapeoUrgencia(String urgencia){
        String urgenciaMapeo = null;
        switch (urgencia) {
            case "1000":
                urgenciaMapeo = "1-Critica";
                break;
            case "2000":
                urgenciaMapeo = "2-Alta";
                break;
            case "3000":
                urgenciaMapeo = "3-Medio";
                break;
            case "4000":
                urgenciaMapeo = "4-Baja";
                break;
            default:
                urgenciaMapeo = urgencia;
                break;
        }
        return urgenciaMapeo;
    }

    public String mapeoPrioridad(String prioridad){
        String prioridadMapeo = null;
        switch (prioridad) {
            case "0":
                prioridadMapeo = "Critica";
                break;
            case "1":
                prioridadMapeo = "Alta";
                break;
            case "2":
                prioridadMapeo = "Media";
                break;
            case "3":
                prioridadMapeo = "Baja";
                break;
            default:
                prioridadMapeo = prioridad;
                break;
        }
        return prioridadMapeo;
    }

}
