package com.integracion.sdp.utils;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Base64;

@Component

public class ConsumeSDPAdjunto {

    public void subirArchivo(String ruta, String nombre, String incidenteServiceDeskplus) {

       // archivoAdjunto = new File("/Users/danielsosa/Downloads/archivo_pdf.pdf");
        // Codifica el archivo adjunto a Base64
        String rutaArchivo = ruta;
        // Realiza la solicitud PUT
        HttpResponse<String> response = Unirest.put("http://servicedesklinux:8080/api/v3/requests/"+incidenteServiceDeskplus+"/upload")
                .header("Accept", "application/vnd.manageengine.sdp.v3+json")
                .header("Authtoken", "06BE6EE0-D056-4B55-9C46-80A7977DCD38")
                .header("Cookie", "SDPSESSIONID=B00AD9982F116711936E38E2305699FA; _zcsr_tmp=c91c4516-ad55-439f-a023-ed7a91821280; sdpcsrfcookie=c91c4516-ad55-439f-a023-ed7a91821280; sdplogincsrfcookie=cec7cc6a-5e42-4c0f-96c4-6dcae358b10f")
                .field("file", new File(rutaArchivo),nombre)
                .asString();
        // Imprime la respuesta
        System.out.println(response.getBody());
    }
}
