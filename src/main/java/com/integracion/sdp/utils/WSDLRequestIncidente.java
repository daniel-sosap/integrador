package com.integracion.sdp.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.integracion.sdp.gen.Incident;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WSDLRequestIncidente {

    private static final Logger logger = LoggerFactory.getLogger(WSDLRequestIncidente.class);

    public void imprimeIncidente(Incident incidente){
        logger.info("***********Valores recibidos************\n" +
                "\nid_cliente: " + incidente.getIdCliente() +
                "\ncliente: " + incidente.getCliente()+
                "\ndescripcion: " + incidente.getDescripcion() +
                "\nresumen: " + incidente.getResumen() +
                "\nimpacto: " + incidente.getImpacto() +
                "\nurgencia: " + incidente.getUrgencia() +
                "\nid_agente: " + incidente.getIdAgente() +
                "\nrfc_corto_cliente: " + incidente.getRfcCortoCliente() +
                "\nrfc_corto_contacto: " + incidente.getRfcCortoContacto() +
                "\ndireccion: " + incidente.getDireccion() +
                "\nid_activo: " + incidente.getIdActivo() +
                "\nid_ticket_padre: " + incidente.getIdTicketPadre() +
                "\nfecha_envio: "+ incidente.getFechaEnvio());
    }

    public String validaIncidente(Incident incidente){

        List<String> camposNulos = new ArrayList<>();

        if (incidente.getIdCliente() == null || incidente.getIdCliente().trim().isEmpty()) {
            camposNulos.add("IdCliente");
        }
        if (incidente.getCliente() == null || incidente.getCliente().trim().isEmpty()) {
            camposNulos.add("Cliente");
        }
        if (incidente.getDescripcion() == null || incidente.getDescripcion().trim().isEmpty()) {
            camposNulos.add("Descripcion");
        }
        if (incidente.getResumen() == null || incidente.getResumen().trim().isEmpty()) {
            camposNulos.add("Resumen");
        }
        if (incidente.getImpacto() == null || incidente.getImpacto().trim().isEmpty()) {
            camposNulos.add("Impacto");
        }
        if (incidente.getUrgencia() == null || incidente.getUrgencia().trim().isEmpty()) {
            camposNulos.add("Urgencia");
        }
        if (incidente.getPrioridad() == null || incidente.getPrioridad().trim().isEmpty()) {
            camposNulos.add("Prioridad");
        }
        if (incidente.getIdAgente() == null || incidente.getIdAgente().trim().isEmpty()) {
            camposNulos.add("IdAgente");
        }
        if (incidente.getRfcCortoCliente() == null || incidente.getRfcCortoCliente().trim().isEmpty()) {
            camposNulos.add("RfcCortoCliente");
        }
        if (incidente.getRfcCortoContacto() == null || incidente.getRfcCortoContacto().trim().isEmpty()) {
            camposNulos.add("RfcCortoContacto");
        }
        if (incidente.getDireccion() == null || incidente.getDireccion().trim().isEmpty()) {
            camposNulos.add("Direccion");
        }
//        if (incidente.getIdActivo() == null || incidente.getIdActivo().trim().isEmpty()) {
//            camposNulos.add("IdActivo");
//        }
        if (incidente.getFechaEnvio() == null || incidente.getFechaEnvio().trim().isEmpty()) {
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
            case "1":
                statusdMapeo = "Asignado";
                break;
            case "2":
                statusdMapeo = "En curso";
                break;
            case "3":
                statusdMapeo = "Pendiente";
                break;
            case "4":
                statusdMapeo = "Resuelto";
                break;
            case "5":
                statusdMapeo = "Cerrado";
                break;
            case "6":
                statusdMapeo = "Cerrado";
                break;
        }
        return statusdMapeo;
    }

}
