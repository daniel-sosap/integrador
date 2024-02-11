package com.integracion.sdp.utils;

import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class ConsumeRemedy {

    public String actualizaEstado(String tokenAWT)throws Exception {
        String token = tokenAWT;

        // start HTTP POST to create an entry with fields parameter
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://192.168.1.81:8008/api/arsys/v1/entry/test/");

        // build the JSON entry
        String json = "{ \"values\" : { ";
        json += "\"Submitter__c\" : \"Daniel Sosa\", ";
        json += "\"Short Description__c\" : \"testing desde Intellij\"";
        json += "\"Operacion__c\" : \"actualizacion\"";
        json += " } }";
        httpPost.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        httpPost.addHeader("Authorization", "AR-JWT " + token);

        // make the call and print the Location
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            Header locationHeader = response.getFirstHeader("Status");
            System.out.println(locationHeader.getValue());
        }

        catch (Exception e){
            System.out.println("Error obtenido " + e);
        }

        return "Consumo de servicio";
    }
}
