package com.integracion.sdp.utils;

import com.integracion.sdp.gen.Incident;
import com.integracion.sdp.gen.WorkOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WSDLRequestWO {

    private static final Logger logger = LoggerFactory.getLogger(WSDLRequestWO.class);

    public void imprimeWO(WorkOrder wo){
        logger.info("***********Valores recibidos************\n" +
                "\nid_cliente: " + wo.getIdCliente() +
                "\ncliente: " + wo.getCliente()+
                "\ndescripcion: " + wo.getDescripcion() +
                "\nresumen: " + wo.getResumen() +
//                "\nimpacto: " + incidente.getImpacto() +
//                "\nurgencia: " + incidente.getUrgencia() +
                "\nprioridad: " + wo.getPrioridad() +
                "\nid_agente: " + wo.getIdAgente() +
                "\nrfc_corto_cliente: " + wo.getRfcCortoCliente() +
                "\nrfc_corto_contacto: " + wo.getRfcCortoContacto() +
                "\ndireccion: " + wo.getDireccion() +
                "\nid_activo: " + wo.getIdActivo() +
                "\nid_ticket_padre: " + wo.getIdTicketPadre() +
                "\nfecha_envio: "+ wo.getFechaEnvio());
    }

    public String validaWO(WorkOrder wo){

        List<String> camposNulos = new ArrayList<>();

        if (wo.getIdCliente() == null || wo.getIdCliente().trim().isEmpty()) {
            camposNulos.add("IdCliente");
        }
        if (wo.getCliente() == null || wo.getCliente().trim().isEmpty()) {
            camposNulos.add("Cliente");
        }
        if (wo.getDescripcion() == null || wo.getDescripcion().trim().isEmpty()) {
            camposNulos.add("Descripcion");
        }
        if (wo.getResumen() == null || wo.getResumen().trim().isEmpty()) {
            camposNulos.add("Resumen");
        }
//        if (incidente.getImpacto() == null || incidente.getImpacto().trim().isEmpty()) {
//            camposNulos.add("Impacto");
//        }
//        if (incidente.getUrgencia() == null || incidente.getUrgencia().trim().isEmpty()) {
//            camposNulos.add("Urgencia");
//        }
        if (wo.getPrioridad() == null || wo.getPrioridad().trim().isEmpty()) {
            camposNulos.add("Prioridad");
        }
        if (wo.getIdAgente() == null || wo.getIdAgente().trim().isEmpty()) {
            camposNulos.add("IdAgente");
        }
        if (wo.getRfcCortoCliente() == null || wo.getRfcCortoCliente().trim().isEmpty()) {
            camposNulos.add("RfcCortoCliente");
        }
        if (wo.getRfcCortoContacto() == null || wo.getRfcCortoContacto().trim().isEmpty()) {
            camposNulos.add("RfcCortoContacto");
        }
        if (wo.getDireccion() == null || wo.getDireccion().trim().isEmpty()) {
            camposNulos.add("Direccion");
        }
//        if (incidente.getIdActivo() == null || incidente.getIdActivo().trim().isEmpty()) {
//            camposNulos.add("IdActivo");
//        }
        if (wo.getFechaEnvio() == null || wo.getFechaEnvio().trim().isEmpty()) {
           camposNulos.add("FechaEnvio");
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

    public String mapeoStatus(String status){
        String statusdMapeo = null;
        switch (status) {
            case "0":
                statusdMapeo = "Asignado";
                break;
            case "4":
                statusdMapeo = "En curso";
                break;
            case "1":
                statusdMapeo = "Pendiente";
                break;
            case "5":
                statusdMapeo = "Resuelto";
                break;
            case "8":
                statusdMapeo = "Cerrado";
                break;
            case "7":
                statusdMapeo = "Cancelado";
                break;
        }
        return statusdMapeo;
    }

}
