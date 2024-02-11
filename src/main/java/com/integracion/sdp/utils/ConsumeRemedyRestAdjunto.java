package com.integracion.sdp.utils;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class ConsumeRemedyRestAdjunto {

    public String  actualizaEstado(String token, String incidenteSDP, String notas, String nombre, String Ruta) {

        HttpResponse<String> response = Unirest.post("http://192.168.1.81:8008/api/arsys/v1/entry/test/")
                .header("Content-type", "Application/json")
                .header("Authorization", "AR-JWT "+token)
                .body("{\n  \"values\":{\n    " +
                        "\"Submitter__c\":\"Demo\",\n    " +
                        "\"Operacion__c\":\"actualizacion\",\n    " +
                        "\"idsdp\":\""+incidenteSDP+"\",\n    " +
                        "\"Notas__c\":\""+notas+"\",\n    " +
                        "\"Short Description__c\":\"Actualizacion desde SDP con Rest con adjunto\",\n    " +
                        "\"Operacion__c\" :\"actualizacion\"\n  }\n}")
                .asString();

        // Obteniendo el valor del encabezado "Location"
        String location = response.getHeaders().getFirst("Location");

        // Imprimiendo el valor del encabezado "Location"
        System.out.println("Location: " + location);
        return location;
    }


}
