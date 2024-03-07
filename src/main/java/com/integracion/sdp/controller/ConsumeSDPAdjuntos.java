package com.integracion.sdp.controller;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.JsonNode;
import org.springframework.web.bind.annotation.*;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ConsumeSDPAdjuntos {
String input_data;

    @GetMapping("/sendRequestAttachments")
    public String sendRequest(String descripcion) {
        //String descripcion = "Prueba con descripcion";
input_data = "{\"request\":{\"subject\": \""+ descripcion + "\",\"requester\": {\"name\": \"Temp User\"}}}";
        HttpResponse<String> response = Unirest.post("http://servicedesklinux:8080/api/v3/requests")
                .header("Accept", "application/vnd.manageengine.sdp.v3+json")
                .header("Authtoken", "06BE6EE0-D056-4B55-9C46-80A7977DCD38")
                .multiPartContent()
                .field("input_data", input_data)
                //.field("input_data", "{\"request\":{\"subject\": \" Prueba desde Intellij\",\"requester\": {\"name\": \"Temp User\"}}}")
                .asString();
        System.out.println("Consumo de servicio");
        System.out.println("Respuesta de Servicicio \n" + response.getBody());
        String id = new JsonNode(response.getBody()).getObject().getJSONObject("request").getString("id");
        System.out.println("ID obtenido: " + id);
        //return response.getBody();
        return id;
    }
}
