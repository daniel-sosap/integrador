package com.integracion.sdp.utils;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class ConsumeRemedyRest {

    public String  actualizaEstado(String token, String incidenteRemedy, String statusRemedy) {

        HttpResponse<String> response = Unirest.post("http://192.168.1.81:8008/api/arsys/v1/entry/test/")
                .header("Content-type", "Application/json")
                .header("Authorization", "AR-JWT "+token)
                .body("{\n  \"values\":{\n    " +
                        "\"Submitter__c\":\"Demo\",\n    " +
                        "\"Operacion__c\":\"actualizacion\",\n    " +
                        "\"idsdp\":\""+incidenteRemedy+"\",\n    " +
                        "\"EstatusSDP__c\":\""+statusRemedy+"\",\n    " +
                        "\"Short Description__c\":\"Actualizacion desde SDP con Rest\",\n    " +
                        "\"Operacion__c\" :\"actualizacion\"\n  }\n}")
                .asString();

        // Obteniendo el valor del encabezado "Location"
        String location = response.getHeaders().getFirst("Location");

        // Imprimiendo el valor del encabezado "Location"
        System.out.println("Location: " + location);
        return location;
    }


}
