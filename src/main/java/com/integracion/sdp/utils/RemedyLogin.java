package com.integracion.sdp.utils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class RemedyLogin {
    String token;
    public String obtieneToken () throws Exception {
        // start HTTP POST to get a token
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://192.168.1.81:8008/api/jwt/login");

        // send the username and password
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("username", "Demo"));
        nvps.add(new BasicNameValuePair("password", "123"));
        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        // make the call and print the token
        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            HttpEntity entity = response.getEntity();
             token = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            System.out.println(token);
        }

        return token;
    }


}

