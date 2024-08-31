package com.integracion.sdp.client;

import com.integracion.sdp.config.ConfigurationManager;
import com.integracion.sdp.gen.*;
import com.integracion.sdp.dto.AdjuntoInfo;
import com.integracion.sdp.utils.JSONRequestIncidente;
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
public class ConsumeSDP {

    static ConfigurationManager configManager = ConfigurationManager.getInstance();

    String input_data;

    @Autowired
    private JSONRequestIncidente jsonRequestIncidente;
final String urlServicedesk = "http://"+configManager.getProperty("config.servicedesk.hostname")+":"+configManager.getProperty("config.servicedesk.puerto");

    public IncidentResponse creaRequest(Incident incidente) {
        System.out.println("URL de ServideDeskplus: " + urlServicedesk );
        input_data = jsonRequestIncidente.convierteIncidenteToJSON(incidente);
        HttpResponse<String> response = Unirest.post(urlServicedesk+"/api/v3/requests")
                .header("Accept", "application/vnd.manageengine.sdp.v3+json")
                .header("Authtoken", configManager.getProperty("config.token"))
                .multiPartContent()
                .field("input_data", input_data)
                .asString();

        System.out.println("Consumo de servicio Service desk plus /api/v3/requests");
        IncidentResponse incidentResponse = new IncidentResponse();

        if (response.getStatus() == 201) {
            System.out.println("Respuesta de Servicio versión completa de incidentes \n" + response.getBody());
            incidentResponse.setIdsdp(new JsonNode(response.getBody()).getObject().getJSONObject("request").getString("id"));
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

    public StatusResponse updateRequest(IncidentUpdate incidentUpdate) {
        input_data = jsonRequestIncidente.convierteIncidentUpdateToJSON(incidentUpdate);
        HttpResponse<String> response = Unirest.put(urlServicedesk+"/api/v3/requests/"+incidentUpdate.getIdsdp())
                .header("Accept", "application/vnd.manageengine.sdp.v3+json")
                .header("Authtoken", configManager.getProperty("config.token"))
                .multiPartContent()
                .field("input_data", input_data)
                .asString();

        System.out.println("Consumo de servicio update de ticket put api/v3/requests/");
        StatusResponse statusResponse = new StatusResponse();

        if (response.getStatus() == 201) {
            System.out.println("Actualizacion de Incidente en SDP con exito \n" + response.getBody());
            statusResponse.setIdsdp(new JsonNode(response.getBody()).getObject().getJSONObject("request").getString("id"));
            statusResponse.setMensajeTransaccion("Transaccion exitosa");
            statusResponse.setResultadoTransaccion("Completada");
        }
             else if(response.getStatus() == 2000) {
                System.out.println("Actualizacion de Incidente en SDP con exito \n" + response.getBody());
                statusResponse.setIdsdp(new JsonNode(response.getBody()).getObject().getJSONObject("request").getString("id"));
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


    public WorklogResponse addNoteRequest(String requestId, String noteDescription) {
        String input_data = "{\"note\": {\"show_to_requester\": false,\"description\": \"" + noteDescription + "\"}}";
        System.out.println();
        WorklogResponse worklogResponse = new WorklogResponse();
        HttpResponse<String> response = Unirest.post(urlServicedesk+"/api/v3/requests/" + requestId + "/notes/")
                .header("Accept", "application/vnd.manageengine.sdp.v3+json")
                .header("Authtoken", configManager.getProperty("config.token"))
                .multiPartContent()
                .field("input_data", input_data)
                .asString();

        System.out.println("Respuesta del servicio success or failed: " +  response.getStatus());

        if (response.getStatus() == 201) {
            System.out.println("Respuesta de Servicio versión completa de incidentes \n" + response.getBody());
            System.out.println((new JsonNode(response.getBody()).getObject().getJSONObject("note").getString("id")));
            worklogResponse.setIdsdp((new JsonNode(response.getBody()).getObject().getJSONObject("note").getString("id")));
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
        String url = urlServicedesk+"/api/v3/requests/"+idRequest+"/notes/"+idNote+"/upload";

        System.out.println("La URL  de service desk plus es " + urlServicedesk);

        String filePath = adjunto.getRuta();

        try {
            HttpResponse<String> response = Unirest.put(url)
                    .header("Accept", "application/vnd.manageengine.sdp.v3+json")
                    .header("Authtoken",  configManager.getProperty("config.token"))
                    .field("file", new File(filePath))
                    .asString();

            if (response.getStatus() == 200) {
                System.out.println("Respuesta de la solicitud: " + response.getBody());
                return true;
            } else {
                System.out.println("Error en la respuesta de la solicitud: " + response.getBody());
                return false;
            }
        } catch (UnirestException e) {
            System.err.println("Error al realizar la solicitud: " + e.getMessage());
            return false;
        }
    }
}
