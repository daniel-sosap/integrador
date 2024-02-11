package com.integracion.sdp.controller;
import com.integracion.sdp.dto.ActualizaIncidenteDTO;
import com.integracion.sdp.utils.ConsumeRemedyRest;
import com.integracion.sdp.utils.RemedyLogin;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/remedy")
public class RemedyController {


    @PostMapping("/actualizaincidente/{remedyid}/{status}")
    public ResponseEntity<String> actualizaIncidente(@PathVariable String remedyid, @PathVariable String status) {

        System.out.println("Se recibio el ticket " + remedyid);
        System.out.println("Se recbibio el status " + status);

        String tokenRemedy = "";
        String respuestaRemedy = "";
        String idRespuestaRemedy = "";
        RemedyLogin remedyToken = new RemedyLogin();
        try {
            tokenRemedy = remedyToken.obtieneToken();
            System.out.println(" token" + tokenRemedy);
        }
        catch (Exception e){
            System.out.println("Error" +e);
        }
        ConsumeRemedyRest consumeRemedy = new ConsumeRemedyRest();

        try {
            respuestaRemedy =consumeRemedy.actualizaEstado(tokenRemedy,remedyid,status);
        }

        catch (Exception e){
            System.out.println("Error" +e);
        }

        idRespuestaRemedy = respuestaRemedy.substring(respuestaRemedy.length()-15);
        return new ResponseEntity<>("{\"respuesta\": \"Incidente actualizado correctamente\"" +
                "\"idRespuesta\": \""+idRespuestaRemedy+"\"" +
                "}", HttpStatus.OK);
    }


    @PostMapping("/agreganotas/{remedyid}")
    public ResponseEntity<String> agreganotasIncidente(@PathVariable String remedyid,
                                                       @RequestParam("notas") String notas,
                                                       @RequestParam("file") Optional<MultipartFile> fileOptional) {

        // Lógica para manejar la actualización del incidente con los parámetros recibidos, incluido el archivo adjunto
        System.out.println("Se consumio agregar /agreganotas");
        System.out.printf("Se recibio la nota: " + notas);
        if (fileOptional.isPresent()) {
            MultipartFile file = fileOptional.get();
            if (!file.isEmpty()) {
                try {
                    String adjuntoNombre = file.getOriginalFilename();
                    // Resto del código para guardar el archivo en la carpeta temporal
                    String rutaArchivo = System.getProperty("java.io.tmpdir") + File.separator + adjuntoNombre;
                    FileOutputStream fos = new FileOutputStream(rutaArchivo);
                    fos.write(file.getBytes());
                    fos.close();
                    System.out.println("Archivo guardado en la carpeta temporal: " + rutaArchivo);
                } catch (Exception e) {
                    System.err.println("Error al guardar el archivo en la carpeta temporal: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return new ResponseEntity<>("{\"respuesta\": \"Incidente actualizado correctamente\", \"remedyid\": \"" + remedyid + "\", \"notas\": \"" + notas + "\"}", HttpStatus.OK);
    }

    @PostMapping("/q/{remedyid}")
    public ResponseEntity<String> agreganotasIncidente(@PathVariable String remedyid,
                                                       @RequestParam("notas") String notas,
                                                       @RequestParam("file") MultipartFile file) {
        // Tu lógica para manejar la actualización del incidente con los parámetros recibidos, incluido el archivo adjunto
        System.out.println("Se consumio agregar /agreganotasadjunto");
        System.out.printf("Se recibio la nota: " + notas);
        if (!file.isEmpty()) {
            try {
                String adjuntoNombre = file.getOriginalFilename();
                // Resto del código para guardar el archivo en la carpeta temporal
                String rutaArchivo = System.getProperty("java.io.tmpdir") + File.separator + adjuntoNombre;
                FileOutputStream fos = new FileOutputStream(rutaArchivo);
                fos.write(file.getBytes());
                fos.close();
                System.out.println("Archivo guardado en la carpeta temporal: " + rutaArchivo);
            } catch (Exception e) {
                System.err.println("Error al guardar el archivo en la carpeta temporal: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>("{\"respuesta\": \"Incidente actualizado correctamente\", \"remedyid\": \"" + remedyid + "\", \"notas\": \"" + notas + "\"}", HttpStatus.OK);
    }



}
