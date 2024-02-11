package com.integracion.sdp.utils;

import java.io.File;
import java.util.Arrays;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;


public class PostAttachment {

    public static void main(String[] args) throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIwVXBCTUJiUjE0WkhoczF4cW9BTHR4QlUwZ2lrZ3dNaVwvcVVtOU42UzlxQ0hlbHZXelVzM0toZ2dlRm81VlE3U1ByNUNtXC9EQzQ5YitkRXlIZkp1SXhHeGNpWXpwc2ZCQThIWjd5MWc1TEZSSzlcL1VueTF2c01nPT0iLCJuYmYiOjE3MDc0MzI0MDUsImlzcyI6ImFyc2RldiIsImV4cCI6MTcwNzQzNjEyNSwiX2NhY2hlSWQiOjEwMjQsImlhdCI6MTcwNzQzMjUyNSwianRpIjoiSURHQjVLTThISUxEN0FTSUtIM0pTSDlNQVJKQjVNIn0.IrQyePYKtj_NLCtunVTzQMz5j1tXWmlZ-Z9mNuzh8uY";
        String filename = "/var/folders/sw/d339vyf566xb19vdgz_16yjm0000gn/T/archivo_pdf.pdf";

        // start HTTP POST to create an entry
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://192.168.1.81:8008/api/arsys/v1/entry/test/");

        // build the JSON entry
        String json = "{ \"values\" : { ";
        json += "\"Submitter__c\" : \"Demo\", ";
        json += "\"Short Description__c\" : \"testing 123\", ";
        json += "\"Adjunto 1__c\" : \"archivo_pdf.pdf\"";
        json += " } }";

        // build the multipart entity
        HttpEntity entity = MultipartEntityBuilder.create()
                .addTextBody("entry", json, ContentType.APPLICATION_JSON)
                .addBinaryBody("attach-Adjunto 1__c", new File(filename))
                .build();
        httpPost.setEntity(entity);
        httpPost.addHeader("Authorization", "AR-JWT " + token);

        // make the call and print the Location
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            Header locationHeader = response.getFirstHeader("Location");
            System.out.println(locationHeader.getValue());
            System.out.println(Arrays.toString(response.getAllHeaders()));
        }
        catch (Exception e){
            System.out.println("Error obtenido "+e);
        }


    }

}
