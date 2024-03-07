package com.integracion.sdp.utils;

import com.integracion.sdp.gen.Incident;
import com.integracion.sdp.gen.IncidentUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WSDLStatusIncidentRequest {

    private static final Logger logger = LoggerFactory.getLogger(WSDLStatusIncidentRequest.class);

    public void imprimeIncidentUpdate(IncidentUpdate incidentUpdate){
        logger.info("***********Valores recibidos************\n" +
                "\nIdsdp: " + incidentUpdate.getIdsdp() +
                "\ncliente: " + incidentUpdate.getCliente()+
                "\nstatus: " + incidentUpdate.getStatus() +
                "\nmotivo actualizacion: " + incidentUpdate.getMotivoActualizacion() +
                "\nimpacto: " + incidentUpdate.getImpacto() +
                "\nurgencia: " + incidentUpdate.getUrgencia() +
                "\nPrioridad: " + incidentUpdate.getPrioridad() +
                "\nid_agente: " + incidentUpdate.getIdAgente() +
                "\nrfc_corto_cliente: " + incidentUpdate.getRfcCortoCliente() +
                "\nrfc_corto_contacto: " + incidentUpdate.getRfcCortoContacto() +
                "\ndireccion: " + incidentUpdate.getDireccion() +
                "\ntoken: " + incidentUpdate.getToken());
    }

    public String validaIncidentUpdate(IncidentUpdate incidentUpdate){

        List<String> camposNulos = new ArrayList<>();

        if (incidentUpdate.getIdsdp() == null || incidentUpdate.getIdsdp().trim().isEmpty()) {
            camposNulos.add("IdSDP");
        }
        if (incidentUpdate.getCliente() == null || incidentUpdate.getCliente().trim().isEmpty()) {
            camposNulos.add("Cliente");

        }

        if (incidentUpdate.getStatus() == null || incidentUpdate.getStatus().trim().isEmpty()) {
            if (incidentUpdate.getStatus() == null || incidentUpdate.getStatus().trim().isEmpty())
            camposNulos.add("Status");
        }
       /*
        if (incidentUpdate.getMotivoActualizacion() == null || incidentUpdate.getMotivoActualizacion().trim().isEmpty()) {
            camposNulos.add("MotivoActualizacion");
        }
        if (incidentUpdate.getImpacto() == null || incidentUpdate.getImpacto().trim().isEmpty()) {
            camposNulos.add("Impacto");
        }
        if (incidentUpdate.getUrgencia() == null || incidentUpdate.getUrgencia().trim().isEmpty()) {
            camposNulos.add("Urgencia");
        }
        if (incidentUpdate.getPrioridad() == null || incidentUpdate.getPrioridad().trim().isEmpty()) {
            camposNulos.add("Prioridad");
        }
        if (incidentUpdate.getIdAgente() == null || incidentUpdate.getIdAgente().trim().isEmpty()) {
            camposNulos.add("IdAgente");
        }
        if (incidentUpdate.getRfcCortoCliente() == null || incidentUpdate.getRfcCortoCliente().trim().isEmpty()) {
            camposNulos.add("RfcCortoCliente");
        }
        if (incidentUpdate.getRfcCortoContacto() == null || incidentUpdate.getRfcCortoContacto().trim().isEmpty()) {
            camposNulos.add("RfcCortoContacto");
        }
        if (incidentUpdate.getDireccion() == null || incidentUpdate.getDireccion().trim().isEmpty()) {
            camposNulos.add("Direccion");
        }
        if (incidentUpdate.getToken() == null || incidentUpdate.getToken().trim().isEmpty()) {
            camposNulos.add("Token");
        }
*/

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
