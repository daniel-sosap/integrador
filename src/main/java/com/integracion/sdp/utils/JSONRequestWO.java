package com.integracion.sdp.utils;

import com.integracion.sdp.config.ConfigurationManager;
import com.integracion.sdp.gen.Incident;
import com.integracion.sdp.gen.IncidentUpdate;
import com.integracion.sdp.gen.WoUpdate;
import com.integracion.sdp.gen.WorkOrder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JSONRequestWO {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(WSDLRequestWO.class);
    static ConfigurationManager configManager = ConfigurationManager.getInstance();

    public String convierteWOToJSON(WorkOrder wo) {


        //String valorPropiedad = propiedadesService.getProperty("config.texto.ambiente");
        // System.out.println("Valor de la propiedad config.texto.ambiente: " + valorPropiedad);

        JSONObject jsonObject = new JSONObject();

        JSONObject requestObject = new JSONObject();
        requestObject.put("subject", wo.getResumen());
        requestObject.put("description", wo.getDescripcion());
        JSONObject priorityObject = new JSONObject();
       priorityObject.put("name", wo.getPrioridad());
        requestObject.put("priority", priorityObject);

        JSONObject modeObject = new JSONObject();
        modeObject.put("name", configManager.getProperty("config.inc.field.mode"));
        requestObject.put("mode", modeObject);

        JSONObject udfFieldsObject = new JSONObject();
        udfFieldsObject.put(configManager.getProperty("config.inc.field.cliente"), wo.getCliente());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.id_remedy"), wo.getIdCliente());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.categorizacion"), wo.getIdCategorizacion());
//        udfFieldsObject.put(configManager.getProperty("config.inc.field.id_agente"), wo.getIdAgente());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.rfc_cliente"), wo.getRfcCortoCliente());
        udfFieldsObject.put(configManager.getProperty("config.inc.field.rfc_contacto"), wo.getRfcCortoContacto());
//        udfFieldsObject.put(configManager.getProperty("config.inc.field.direccion"), wo.getDireccion());
//        udfFieldsObject.put(configManager.getProperty("config.inc.field.id_activo"), wo.getIdActivo());
//        udfFieldsObject.put(configManager.getProperty("config.inc.field.id_ticket_padre"), wo.getIdTicketPadre());
//        udfFieldsObject.put(configManager.getProperty("config.inc.field.fecha_envio"), wo.getFechaEnvio());
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

    public String convierteWOToJSON2(WorkOrder wo) {
        JSONObject jsonObject = new JSONObject();

        JSONObject taskObject = new JSONObject();
        taskObject.put("description", wo.getDescripcion());
        taskObject.put("title", wo.getResumen());

        JSONObject priorityObject = new JSONObject();
        priorityObject.put("name", wo.getPrioridad());
        taskObject.put("priority", priorityObject);

        JSONObject udfFieldsObject = new JSONObject();
        udfFieldsObject.put("sline_direccion", wo.getDireccion());
        udfFieldsObject.put("sline_fecha_de_envio", wo.getFechaEnvio());
        udfFieldsObject.put("sline_cliente", wo.getCliente());
        udfFieldsObject.put("sline_id_de_categorizacion_sat", wo.getIdCategorizacion());
        udfFieldsObject.put("sline_rfc_corto_contacto", wo.getRfcCortoContacto());
        udfFieldsObject.put("sline_id_de_incidente_padre", wo.getIdTicketPadre());
        udfFieldsObject.put("sline_rfc_corto_cliente", wo.getRfcCortoCliente());
        udfFieldsObject.put("sline_id_del_activo", wo.getIdActivo());
        udfFieldsObject.put("sline_id_remedy", wo.getIdCliente());
        udfFieldsObject.put("sline_resumen", wo.getResumen());
        udfFieldsObject.put("sline_id_de_agente_de_mesa", wo.getIdAgente());

        taskObject.put("udf_fields", udfFieldsObject);

        JSONObject statusObject = new JSONObject();
        statusObject.put("name", "Asignado");
        taskObject.put("status", statusObject);

        jsonObject.put("task", taskObject);

        return jsonObject.toString();
    }


    public String convierteWoUpdateToJSON(WoUpdate woUpdate) {

        JSONObject jsonObject = new JSONObject();
        JSONObject requestObject = new JSONObject();
        JSONObject udfFieldsObject = new JSONObject(); //Campos adicionales
        // Comprobar si se recibió el campo status

        JSONObject statusObject = new JSONObject();
        statusObject.put("name", woUpdate.getStatus());
        requestObject.put("status", statusObject);

        // Verificar si se recibió motivo_actualizacion y agregarlo si es así
//        if (woUpdate.getMotivoActualizacion() == null || woUpdate.getMotivoActualizacion().trim().isEmpty()) {
//            System.out.println("MotivoActualizacion es vacio");
//        } else {
//            udfFieldsObject.put("udf_pick_601", woUpdate.getMotivoActualizacion());
//            System.out.println("MotivoActualizacion NO es vacio");
//        }


            if (woUpdate.getPrioridad() == null || woUpdate.getPrioridad().trim().isEmpty()) {
                System.out.println("Prioridad es vacio");
            }else{
                JSONObject priorityObject = new JSONObject();
                priorityObject.put("name", woUpdate.getPrioridad());
                requestObject.put("priority", priorityObject); //ok
                System.out.println("Prioridad NO es vacio");
            }
            if (woUpdate.getRfcCortoCliente() == null || woUpdate.getRfcCortoCliente().trim().isEmpty()) {
                System.out.println("RfcCortoCliente es vacio");
            }else {
                udfFieldsObject.put("sline_rfc_corto_cliente", woUpdate.getRfcCortoCliente()); //ok
                System.out.println("RfcCortoCliente NO es vacio");
            }

            if (woUpdate.getRfcCortoContacto() == null || woUpdate.getRfcCortoContacto().trim().isEmpty()) {
                System.out.println("RfcContacto  es vacio");
            }else {
                udfFieldsObject.put("sline_rfc_corto_contacto", woUpdate.getRfcCortoContacto()); //ok
                System.out.println("RfcContacto NO es vacio");
            }

            if (woUpdate.getDireccion() == null || woUpdate.getDireccion().trim().isEmpty()) {
                System.out.println("Direccion  es vacio");
            }else {
                udfFieldsObject.put("sline_direccion", woUpdate.getDireccion()); //ok
                System.out.println("Direccion NO es vacio");
            }

        if(udfFieldsObject.isEmpty()){
            System.out.println("Campos adicionales vacios");
        }else {
            requestObject.put("udf_fields", udfFieldsObject);
            System.out.println("Campos adicionales NO vacios");
        }


        // Añadir el objeto requester (nombre fijo "Temp User" según tus especificaciones)


        // Añadir el objeto request al JSON principal
        jsonObject.put("task", requestObject);

        System.out.println("JSON data");
        System.out.println(jsonObject.toString());
        return jsonObject.toString();
    }
}
