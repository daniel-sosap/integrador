package com.integracion.sdp.utils;

import com.integracion.sdp.config.ConfigurationManager;
import com.integracion.sdp.gen.Incident;
import com.integracion.sdp.gen.IncidentUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JSONRequestIncidente {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(WSDLRequestIncidente.class);
    static ConfigurationManager configManager = ConfigurationManager.getInstance();

    public String convierteIncidenteToJSON(Incident incident) {


        //String valorPropiedad = propiedadesService.getProperty("config.texto.ambiente");
        // System.out.println("Valor de la propiedad config.texto.ambiente: " + valorPropiedad);

        JSONObject jsonObject = new JSONObject();

        JSONObject requestObject = new JSONObject();
        requestObject.put("subject", incident.getResumen());
        requestObject.put("description", incident.getDescripcion());

        JSONObject impactObject = new JSONObject();
        impactObject.put("name", incident.getImpacto());
        requestObject.put("impact", impactObject);

        JSONObject urgencyObject = new JSONObject();
        urgencyObject.put("name", incident.getUrgencia());
        requestObject.put("urgency", urgencyObject);

        JSONObject priorityObject = new JSONObject();
        priorityObject.put("name", incident.getPrioridad());
        requestObject.put("priority", priorityObject);

        JSONObject modeObject = new JSONObject();
        modeObject.put("name", configManager.getProperty("config.inc.field.mode"));
        requestObject.put("mode", modeObject);

        JSONObject udfFieldsObject = new JSONObject();
        udfFieldsObject.put(configManager.getProperty("config.inc.field.cliente"), incident.getCliente());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.id_remedy"), incident.getIdCliente());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.categorizacion"), incident.getIdCategorizacion());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.id_agente"), incident.getIdAgente());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.rfc_cliente"), incident.getRfcCortoCliente());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.rfc_contacto"), incident.getRfcCortoContacto());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.direccion"), incident.getDireccion());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.id_activo"), incident.getIdActivo());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.id_ticket_padre"), incident.getIdTicketPadre());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.fecha_envio"), incident.getFechaEnvio());
        //udfFieldsObject.put("udf_sline_901", "fecha remedy");



        requestObject.put("udf_fields", udfFieldsObject);

        JSONObject requesterObject = new JSONObject();
        requesterObject.put("name", "administrador");
        requestObject.put("requester", requesterObject);

        jsonObject.put("request", requestObject);

        System.out.println("JSON data");
        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }

    public String convierteIncidentUpdateToJSON(IncidentUpdate incidentUpdate) {

        JSONObject jsonObject = new JSONObject();
        JSONObject requestObject = new JSONObject();
        JSONObject udfFieldsObject = new JSONObject(); //Campos adicionales
        // Comprobar si se recibió el campo status

        JSONObject statusObject = new JSONObject();
        statusObject.put("name", incidentUpdate.getStatus());
        requestObject.put("status", statusObject);

        // Verificar si se recibió motivo_actualizacion y agregarlo si es así
        if (incidentUpdate.getMotivoActualizacion() == null || incidentUpdate.getMotivoActualizacion().trim().isEmpty()) {
            System.out.println("MotivoActualizacion es vacio");
        } else {
            udfFieldsObject.put(configManager.getProperty("config.inc.field.motivo_actualizacion"), incidentUpdate.getMotivoActualizacion());
            System.out.println("MotivoActualizacion NO es vacio");
        }


            // Si no se recibió status, generar JSON correspondiente con los campos recibidos
            if (incidentUpdate.getImpacto() == null || incidentUpdate.getImpacto().trim().isEmpty()) {
                System.out.println("Impacto es vacio");
            }else {
                JSONObject impactObject = new JSONObject();
                impactObject.put("name", incidentUpdate.getImpacto());
                requestObject.put("impact", impactObject);
                System.out.println("Impacto NO es vacio");
            }
            if (incidentUpdate.getUrgencia() == null || incidentUpdate.getUrgencia().trim().isEmpty()) {
                System.out.println("Urgencia es vacio");

            }else {
                JSONObject urgencyObject = new JSONObject();
                urgencyObject.put("name", incidentUpdate.getUrgencia());
                requestObject.put("urgency", urgencyObject);
                System.out.println("Urgencia NO es vacio");

            }
            if (incidentUpdate.getPrioridad() == null || incidentUpdate.getPrioridad().trim().isEmpty()) {
                System.out.println("Prioridad es vacio");
            }else{
                JSONObject priorityObject = new JSONObject();
                priorityObject.put("name", incidentUpdate.getPrioridad());
                requestObject.put("priority", priorityObject);
                System.out.println("Prioridad NO es vacio");
            }
            if (incidentUpdate.getRfcCortoCliente() == null || incidentUpdate.getRfcCortoCliente().trim().isEmpty()) {
                System.out.println("RfcCortoCliente es vacio");
            }else {
                udfFieldsObject.put(configManager.getProperty("config.inc.field.rfc_cliente"), incidentUpdate.getRfcCortoCliente());
                System.out.println("RfcCortoCliente NO es vacio");
            }

            if (incidentUpdate.getRfcCortoContacto() == null || incidentUpdate.getRfcCortoContacto().trim().isEmpty()) {
                System.out.println("RfcContacto  es vacio");
            }else {
                udfFieldsObject.put(configManager.getProperty("config.inc.field.rfc_contacto"), incidentUpdate.getRfcCortoContacto());
                System.out.println("RfcContacto NO es vacio");
            }

            if (incidentUpdate.getDireccion() == null || incidentUpdate.getDireccion().trim().isEmpty()) {
                System.out.println("Direccion  es vacio");
            }else {
                udfFieldsObject.put(configManager.getProperty("config.inc.field.direccion"), incidentUpdate.getDireccion());
                System.out.println("Direccion NO es vacio");
            }

        if(udfFieldsObject.isEmpty()){
            System.out.println("Campos adicionales vacios");
        }else {
            requestObject.put("udf_fields", udfFieldsObject);
            System.out.println("Campos adicionales NO vacios");
        }




        // Añadir el objeto request al JSON principal
        jsonObject.put("request", requestObject);

        System.out.println("JSON data");
        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }
}
