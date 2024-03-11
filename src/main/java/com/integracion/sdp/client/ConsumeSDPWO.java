package com.integracion.sdp.client;

import com.integracion.sdp.config.ConfigurationManager;
import com.integracion.sdp.gen.*;
import com.integracion.sdp.model.AdjuntoInfo;
import com.integracion.sdp.utils.JSONRequestIncidente;
import com.integracion.sdp.utils.JSONRequestWO;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;


@RestController
@RequestMapping("/api")
public class ConsumeSDPWO {

    static ConfigurationManager configManager = ConfigurationManager.getInstance();

    String input_data;

    @Autowired
    private JSONRequestIncidente jsonRequestIncidente;
    @Autowired
    private JSONRequestWO jsonRequestWO;
final String urlServicedesk = "http://"+configManager.getProperty("config.servicedesk.hostname")+":"+configManager.getProperty("config.servicedesk.puerto");

    public WOResponse creaTask(WorkOrder wo) {
        System.out.println("URL de ServideDeskplus: " + urlServicedesk );
        input_data = jsonRequestWO.convierteWOToJSON2(wo);
        System.out.println("JSON gnerado para crear tareas"+input_data);

        HttpResponse<String> response = Unirest.post(urlServicedesk+"/api/v3/tasks")
                .header("Accept", "application/vnd.manageengine.sdp.v3+json")
                .header("Authtoken", configManager.getProperty("config.token"))
                .multiPartContent()
                .field("input_data", input_data)
                .asString();

        System.out.println("Consumo de servicio Service desk plus");
        WOResponse incidentResponse = new WOResponse();

        if (response.getStatus() == 201) {
            System.out.println("Respuesta de Servicio versión completa de incidentes \n" + response.getBody());
            incidentResponse.setIdsdp(new JsonNode(response.getBody()).getObject().getJSONObject("task").getString("id"));
            incidentResponse.setMensajeTransaccion("Transaccion exitosa");
            incidentResponse.setResultadoTransaccion("Completada");
        } else {
            System.out.println("Error en la respuesta del servicio:");
            System.out.println(response.getBody());
            incidentResponse.setIdsdp("0");
            incidentResponse.setMensajeTransaccion(response.getBody());
            incidentResponse.setResultadoTransaccion("Error");
        }

        return incidentResponse;
    }

    public UpdateWOResponse updateTask(WoUpdate woUpdate) {
        input_data = jsonRequestWO.convierteWoUpdateToJSON(woUpdate);
        HttpResponse<String> response = Unirest.put(urlServicedesk+"/api/v3/tasks/"+woUpdate.getIdsdp())
                .header("Accept", "application/vnd.manageengine.sdp.v3+json")
                .header("Authtoken", configManager.getProperty("config.token"))
                .multiPartContent()
                .field("input_data", input_data)
                .asString();

        System.out.println("Consumo de servicio update de Task");
        UpdateWOResponse statusResponse = new UpdateWOResponse();

        if (response.getStatus() == 201) {
            System.out.println("Actualizacion de Tarea en SDP con exito \n" + response.getBody());
            statusResponse.setIdsdp(new JsonNode(response.getBody()).getObject().getJSONObject("task").getString("id"));
            statusResponse.setMensajeTransaccion("Transaccion exitosa");
            statusResponse.setResultadoTransaccion("Completada");
        } else {
            System.out.println("Error en la respuesta del servicio:");
            System.out.println(response.getBody());
            statusResponse.setIdsdp("0");
            statusResponse.setMensajeTransaccion(response.getBody());
            statusResponse.setResultadoTransaccion("Error");
        }

        return statusResponse;
    }


    public CommentWOResponse addNoteTask(String requestId, String noteDescription) {
        //String input_data = "{\"note\": {\"show_to_requester\": false,\"description\": \"" + noteDescription + "\"}}";
        String input_data = "{\n\"comment\": {\"content\": \""+noteDescription+"\"}\n}";

            System.out.println();
        CommentWOResponse worklogResponse = new CommentWOResponse();
        HttpResponse<String> response = Unirest.post(urlServicedesk+"/api/v3/tasks/" + requestId + "/comments/")
                .header("Accept", "application/vnd.manageengine.sdp.v3+json")
                .header("Authtoken", configManager.getProperty("config.token"))
                .multiPartContent()
                .field("input_data", input_data)
                .asString();

        System.out.println("Consumo de servicio Add Comment desde intellij");
       // JSONObject responseObject = new JSONObject(response);
      //  String responseStatus = responseObject.getJSONObject("response_status").getString("status");
        System.out.println("Respuesta del servicio success or failed: " +  response.getStatus());

        if (response.getStatus() == 201) {
            System.out.println("Respuesta de Servicio consumo de notas \n" + response.getBody());
            System.out.println((new JsonNode(response.getBody()).getObject().getJSONObject("comment").getString("id")));
            worklogResponse.setIdsdp((new JsonNode(response.getBody()).getObject().getJSONObject("comment").getString("id")));
            worklogResponse.setMensajeTransaccion("Transaccion exitosa");
            worklogResponse.setResultadoTransaccion("Completada");


            System.out.println("Nota agregada con éxito");
        } else {
            System.out.println("Error al agregar nota:");
            System.out.println(response.getBody());
            worklogResponse.setIdsdp(requestId);
            worklogResponse.setMensajeTransaccion(response.getBody());
            worklogResponse.setResultadoTransaccion("Error");
        }

        return worklogResponse;
    }

    public  boolean addNoteAttachmentRequest(AdjuntoInfo adjunto, String idRequest, String idNote) {
        // URL de la API de ServiceDeskPlus para subir un archivo adjunto a una nota
//String url = urlServicedesk+"/api/v3/tasks/"+idRequest+"/notes/"+idNote+"/upload";
        String url = urlServicedesk+"/api/v3/tasks/"+idRequest+"/upload";
        System.out.println("La URL  de service desk plus es " + urlServicedesk);

        // Ruta del archivo adjunto a subir
        String filePath = adjunto.getRuta();

        try {
            // Realizar la solicitud PUT utilizando Unirest
            HttpResponse<String> response = Unirest.put(url)
                    .header("Accept", "application/vnd.manageengine.sdp.v3+json")
                    .header("Authtoken",  configManager.getProperty("config.token"))
                    .field("file", new File(filePath))
                    .asString();

            // Manejar la respuesta de la solicitud
            if (response.getStatus() == 200) {
                // Si la solicitud fue exitosa, imprimir el cuerpo de la respuesta
                System.out.println("Respuesta de la solicitud: " + response.getBody());
                return true;
            } else {
                // Si hubo un error, imprimir el mensaje de error de la respuesta
                System.out.println("Error en la respuesta de la solicitud: " + response.getBody());
                return false;
            }
        } catch (UnirestException e) {
            // Capturar y manejar cualquier excepción que pueda ocurrir durante la solicitud
            System.err.println("Error al realizar la solicitud: " + e.getMessage());
            return false;
        }
    }
}
