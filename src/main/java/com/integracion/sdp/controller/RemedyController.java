package com.integracion.sdp.controller;
import com.integracion.sdp.config.ConfigurationManager;
import com.integracion.sdp.dto.ActualizaEstadoIncidenteDTO;
import com.integracion.sdp.utils.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


@RestController
@RequestMapping("/api/remedy")
public class RemedyController {

    static ConfigurationManager configManager = ConfigurationManager.getInstance();
    final String urlServicedesk = "http://"+configManager.getProperty("config.servicedesk.hostname")+":"+configManager.getProperty("config.servicedesk.puerto");

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


    @PostMapping("/notasinadjunto/{remedyid}")
    public ResponseEntity<String> agregaNotasSinAdjuntoIncidente(@PathVariable String remedyid,
                                                       @RequestParam("notas") String notas,
                                                       @RequestParam("file") Optional<MultipartFile> fileOptional) {

        // Lógica para manejar la actualización del incidente con los parámetros recibidos, incluido el archivo adjunto
        System.out.println("Se consumio agregar /agreganotas");
        System.out.println("El id de nota es: " + remedyid);
        System.out.println("Se recibio la nota: " + notas);
        if (fileOptional.isPresent()) {
            System.out.println("Se recibio un archivo ");
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

        if(fileOptional.isEmpty()){
            System.out.println("No se recibio archivo");
        }

        return new ResponseEntity<>("{\"respuesta\": \"Incidente actualizado correctamente\", \"remedyid\": \"" + remedyid + "\", \"notas\": \"" + notas + "\"}", HttpStatus.OK);
    }

    @PostMapping("/notaconadjunto/{remedyid}")
    public ResponseEntity<String> agregaNotasConAdjuntoIncidente(@PathVariable String remedyid,
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

    //Actualizacion de estado por medio de SOAP
    @PostMapping("/actualizaestadoincidente/{remedyid}/")
    public ResponseEntity<String> actualizaEstadoIncidente(
            @PathVariable String remedyid,
            @RequestBody ActualizaEstadoIncidenteDTO actualizaEstadoDTO) {
        String status = actualizaEstadoDTO.getStatus();
        String resolucion = actualizaEstadoDTO.getResolucion();
        String motivoEstado = actualizaEstadoDTO.getMotivo();
        Document doc = Jsoup.parseBodyFragment(resolucion);
        resolucion = doc.body().text();
        System.out.println("REsolucion en texto limpio: " + resolucion);

        System.out.println("Ejecucion de Metodo /actualizaestadoincidente/{remedyid}/");
        System.out.println("Status recibido: "+ status);
        System.out.println("Motivo Recibido: " + motivoEstado);
        System.out.println("Resolucion recibida: " +  resolucion);
        System.out.println("Reenvio de la peticion de Service desk a Remedy");

//        if (status.equals("1")){
//            System.out.println("Escenario 2, Service Desk cancelado, Remedy Asignado / Rechazado ");
//            motivoEstado = "25000";
//        }

         ConsumeRemedyWSDLEstadoINC.ActualizaIncidenteRemedy("A_INC","MESA_SERVICIO_SENHA2",remedyid,status,motivoEstado,resolucion);
        System.out.println("Resultado de la llamada al servicio SOAP para actualizacion de incidentes: " );



        return new ResponseEntity<>("{\n\"respuesta\": \"Incidente actualizado correctamente\"," +
                "\n\"idRespuesta\": \"" + remedyid + "\"" +
                "}", HttpStatus.OK);
    }

//+++++METODO FINAL A UTILIZAR++++++++++++++++++++++++++++++
    @PostMapping("/notas/{remedyid}")
    public ResponseEntity<String> agregaNotas(@PathVariable String remedyid,
                                                                 @RequestParam("notas") String notas,
                                                                 @RequestParam("notaid") String notaid) {

        // Lógica para manejar la actualización del incidente con los parámetros recibidos, incluido el archivo adjunto
        System.out.println("Se consumio agregar /agreganotas");
        System.out.println("El id de nota es: " + remedyid);
        System.out.println("Se recibio la nota en formato html: " + notas);
        System.out.println("Se recibio la nota id: " + notaid);
////////////***************

        Document doc = Jsoup.parseBodyFragment(notas);

        // Obtener el texto limpio del HTML
        //notas = Jsoup.clean(doc.body().html(), Whitelist.none());
        notas = doc.body().text();
        System.out.println("Nota en texto limpio: " + notas);


        HttpResponse<String> response = Unirest.get(urlServicedesk+"/api/v3/requests/"+remedyid+"/notes/"+notaid)
                .header("Authtoken", configManager.getProperty("config.token"))
                //.header("Cookie", "SDPSESSIONID=F3CDBC4300CE0460466E0ECC3213316F; _zcsr_tmp=6a64161e-d4e8-42ae-b1be-6805eac90d89; sdpcsrfcookie=6a64161e-d4e8-42ae-b1be-6805eac90d89; sdplogincsrfcookie=938f1307-1324-4d43-b513-1bd55d798d04")
                .asString();

        HttpResponse<String> remedyticket = Unirest.get(urlServicedesk+"/api/v3/requests/"+remedyid)
                .header("Authtoken", configManager.getProperty("config.token"))
                //.header("Cookie", "SDPSESSIONID=F3CDBC4300CE0460466E0ECC3213316F; _zcsr_tmp=6a64161e-d4e8-42ae-b1be-6805eac90d89; sdpcsrfcookie=6a64161e-d4e8-42ae-b1be-6805eac90d89; sdplogincsrfcookie=938f1307-1324-4d43-b513-1bd55d798d04")
                .asString();
        int statusINC = remedyticket.getStatus();
        String bodyINC = remedyticket.getBody();
        System.out.println("Status code: " + statusINC);
        System.out.println("Response body: " + bodyINC);

        String responseBody = remedyticket.getBody();
        JSONObject jsonObject = new JSONObject(responseBody);

        // Obtener el objeto "udf_fields"
        JSONObject udfFields = jsonObject.getJSONObject("request").getJSONObject("udf_fields");

        // Obtener el valor de "udf_sline_301 que corresponde al incidente de remedy"
        String INCRemedy = udfFields.getString("udf_sline_301");

        System.out.println("Valor de udf_sline_301: " + INCRemedy);


        int status = response.getStatus();
        String body = response.getBody();
        System.out.println("Status code: " + status);
        System.out.println("Response body: " + body);

        JSONObject respuestaObjeto = new JSONObject(body);
        boolean hasAttachments = respuestaObjeto.getJSONObject("note").getBoolean("has_attachments");
        List<String> rutasAdjuntos = new ArrayList<>();
        List<String> nombresAdjuntos = new ArrayList<>();

        if (hasAttachments) {
            try {
                // Obtener el array de adjuntos
                JSONArray adjuntos = respuestaObjeto.getJSONObject("note").getJSONArray("attachments");

                // Carpeta temporal donde se guardarán los archivos
                String carpetaTemporal = System.getProperty("java.io.tmpdir");

                // Iterar sobre cada adjunto
                for (int i = 0; i < adjuntos.length(); i++) {
                    JSONObject adjunto = adjuntos.getJSONObject(i);
                    String nombreArchivo = adjunto.getString("name");
                    String urlDescarga = adjunto.getString("content_url");



                    // Descargar el archivo adjunto
                    //descargarArchivo(carpetaTemporal, nombreArchivo, urlDescarga);
                    rutasAdjuntos.add(descargarArchivo(carpetaTemporal, nombreArchivo, urlDescarga));
                    nombresAdjuntos.add(nombreArchivo);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       //////////////***********

try {
    if(nombresAdjuntos.size()==3) {
        ConsumeRemedyWSDLNotasINC.NotasIncidenteRemedy("C_IT_INC", "MESA_SERVICIO_SENHA2", INCRemedy, notas, nombresAdjuntos.get(0), rutasAdjuntos.get(0), nombresAdjuntos.get(1), rutasAdjuntos.get(1), nombresAdjuntos.get(2), rutasAdjuntos.get(2));
    }

    if(nombresAdjuntos.size()==2) {
        ConsumeRemedyWSDLNotasINC.NotasIncidenteRemedy("C_IT_INC", "MESA_SERVICIO_SENHA2", INCRemedy, notas, nombresAdjuntos.get(0), rutasAdjuntos.get(0), nombresAdjuntos.get(1), rutasAdjuntos.get(1), "", "");
    }

    if(nombresAdjuntos.size()==1) {
        ConsumeRemedyWSDLNotasINC.NotasIncidenteRemedy("C_IT_INC", "MESA_SERVICIO_SENHA2", INCRemedy, notas, nombresAdjuntos.get(0), rutasAdjuntos.get(0), "", "", "", "");
    }
    if(nombresAdjuntos.isEmpty()) {
        ConsumeRemedyWSDLNotasINC.NotasIncidenteRemedy("C_IT_INC", "MESA_SERVICIO_SENHA2", INCRemedy, notas, "", "", "", "", "", "");
    }

    //String username, String password, String tipoOperacion, String nombreProveedor, String idTicketInterno, String notas, String archivo1, String archivo2
    //ConsumeRemedyWSDLNotasINC.AgregaNotaIncidenteRemedy("Demo","123","Bitacora","SAT","INC0001",notas,rutasAdjuntos.get(0) ,rutasAdjuntos.get(1));

}catch (Exception e){
    System.out.println("Error al tratar de consumir o armar la peticion WSDL hacia remedy "+e);
}

        return new ResponseEntity<>("{\"respuesta\": \"Incidente actualizado correctamente v2\", \"remedyid\": \"" + remedyid + "\", \"notas\": \"" + notas + "\"}", HttpStatus.OK);
    }


    public static String descargarArchivo(String ruta, String nombreArchivo, String urlDescarga) throws IOException {
        String url = "http://"+configManager.getProperty("config.servicedesk.hostname")+":"+configManager.getProperty("config.servicedesk.puerto")+urlDescarga;
        String rutaArchivo = null;
        // Realiza la solicitud GET para descargar el archivo adjunto
        try {
            // Realiza la solicitud GET para descargar el archivo adjunto
            HttpResponse<byte[]> response = Unirest.get(url)
                    .header("Authtoken", configManager.getProperty("config.token"))
                    .asBytes();

            // Verifica si la solicitud fue exitosa (código de respuesta 200)
            if (response.isSuccess()) {
                // Obtiene el contenido binario de la respuesta
                byte[] contenido = response.getBody();

                // Guarda el contenido binario en un archivo
                 rutaArchivo = ruta+"/"+nombreArchivo; // Ruta donde guardar el archivo
                guardarEnArchivo(contenido, rutaArchivo);

                System.out.println("Archivo descargado correctamente: " + rutaArchivo);
            } else {
                System.out.println("Error al descargar el archivo. Código de respuesta: " + response.getStatus());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rutaArchivo;
    }

    public static void guardarEnArchivo(byte[] contenido, String rutaArchivo) throws IOException {
        Path path = Paths.get(rutaArchivo);
        Files.write(path, contenido);
    }



    /////////////***************wo*********************////////////
    @PostMapping("/actualizaestadowo/{remedyid}/")
    public ResponseEntity<String> actualizaEstadoWO(
            @PathVariable String remedyid,
            @RequestBody ActualizaEstadoIncidenteDTO actualizaEstadoDTO) {
        System.out.println("Consumo del servicio /actualizaestadowo/{remedyid}/" );
        String status = actualizaEstadoDTO.getStatus();
        String resolucion = actualizaEstadoDTO.getResolucion();
        String motivoEstado = actualizaEstadoDTO.getMotivo();
        Document doc = Jsoup.parseBodyFragment(resolucion);
        resolucion = doc.body().text();
        System.out.println("Resolucion en texto limpio: " + resolucion);

        System.out.println("Ejecucion de Metodo /actualizaestadowo/{remedyid}/");
        System.out.println("ID de SDP recibido: " + remedyid);
        System.out.println("Status recibido: "+ status);
        System.out.println("Motivo Recibido: " + motivoEstado);
        System.out.println("Resolucion recibida: " +  resolucion);
        System.out.println("Reenvio de la peticion de Service desk a Remedy");

//        if (status.equals("1")){
//            System.out.println("Escenario 2, Service Desk cancelado, Remedy Asignado / Rechazado ");
//            motivoEstado = "25000";
//        }

        ConsumeRemedyWSDLEstadoWO.ActualizaOrdenRemedy("A_WO","MESA_SERVICIO_SENHA2",remedyid,status,motivoEstado,resolucion);
        System.out.println("Resultado de la llamada al servicio SOAP para actualizacion de incidentes: " );



        return new ResponseEntity<>("{\n\"respuesta\": \"WO actualizado correctamente\"," +
                "\n\"idRespuesta\": \"" + remedyid + "\"" +
                "}", HttpStatus.OK);
    }


    @PostMapping("/notaswo/{remedyid}")
    public ResponseEntity<String> agregaNotasWO(@PathVariable String remedyid,
                                              @RequestParam("notas") String notas,
                                              @RequestParam("notaid") String notaid) {

        // Lógica para manejar la actualización del incidente con los parámetros recibidos, incluido el archivo adjunto
        System.out.println("Se consumio agregar /agreganotas");
        System.out.println("El id de nota es: " + remedyid);
        System.out.println("Se recibio la nota en formato html: " + notas);
        System.out.println("Se recibio la nota id: " + notaid);
////////////***************

        Document doc = Jsoup.parseBodyFragment(notas);

        // Obtener el texto limpio del HTML
        //notas = Jsoup.clean(doc.body().html(), Whitelist.none());
        notas = doc.body().text();
        System.out.println("Nota en texto limpio: " + notas);


//        HttpResponse<String> response = Unirest.get(urlServicedesk+"/api/v3/requests/"+remedyid+"/notes/"+notaid)
//                .header("Authtoken", configManager.getProperty("config.token"))
//                //.header("Cookie", "SDPSESSIONID=F3CDBC4300CE0460466E0ECC3213316F; _zcsr_tmp=6a64161e-d4e8-42ae-b1be-6805eac90d89; sdpcsrfcookie=6a64161e-d4e8-42ae-b1be-6805eac90d89; sdplogincsrfcookie=938f1307-1324-4d43-b513-1bd55d798d04")
//                .asString();
//
//        HttpResponse<String> remedyticket = Unirest.get(urlServicedesk+"/api/v3/requests/"+remedyid)
//                .header("Authtoken", configManager.getProperty("config.token"))
//                //.header("Cookie", "SDPSESSIONID=F3CDBC4300CE0460466E0ECC3213316F; _zcsr_tmp=6a64161e-d4e8-42ae-b1be-6805eac90d89; sdpcsrfcookie=6a64161e-d4e8-42ae-b1be-6805eac90d89; sdplogincsrfcookie=938f1307-1324-4d43-b513-1bd55d798d04")
//                .asString();
//        int statusINC = remedyticket.getStatus();
//        String bodyINC = remedyticket.getBody();
//        System.out.println("Status code: " + statusINC);
//        System.out.println("Response body: " + bodyINC);
//
//        String responseBody = remedyticket.getBody();
//        JSONObject jsonObject = new JSONObject(responseBody);
//
//        // Obtener el objeto "udf_fields"
//        JSONObject udfFields = jsonObject.getJSONObject("request").getJSONObject("udf_fields");

        // Obtener el valor de "udf_sline_301 que corresponde al incidente de remedy"
        String INCRemedy = remedyid;

        System.out.println("Valor de udf_sline_301: " + INCRemedy);


//
//        int status = response.getStatus();
//        String body = response.getBody();
//        System.out.println("Status code: " + status);
//        System.out.println("Response body: " + body);

//        JSONObject respuestaObjeto = new JSONObject(body);
//        boolean hasAttachments = respuestaObjeto.getJSONObject("note").getBoolean("has_attachments");
//        List<String> rutasAdjuntos = new ArrayList<>();
//        List<String> nombresAdjuntos = new ArrayList<>();
//
//        if (hasAttachments) {
//            try {
//                // Obtener el array de adjuntos
//                JSONArray adjuntos = respuestaObjeto.getJSONObject("note").getJSONArray("attachments");
//
//                // Carpeta temporal donde se guardarán los archivos
//                String carpetaTemporal = System.getProperty("java.io.tmpdir");
//
//                // Iterar sobre cada adjunto
//                for (int i = 0; i < adjuntos.length(); i++) {
//                    JSONObject adjunto = adjuntos.getJSONObject(i);
//                    String nombreArchivo = adjunto.getString("name");
//                    String urlDescarga = adjunto.getString("content_url");
//
//
//
//                    // Descargar el archivo adjunto
//                    //descargarArchivo(carpetaTemporal, nombreArchivo, urlDescarga);
//                    rutasAdjuntos.add(descargarArchivo(carpetaTemporal, nombreArchivo, urlDescarga));
//                    nombresAdjuntos.add(nombreArchivo);
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        //////////////***********

//        try {
//            if(nombresAdjuntos.size()==3) {
//                ConsumeRemedyWSDLNotasINC.NotasIncidenteRemedy("C_IT_INC", "MESA_SERVICIO_SENHA2", INCRemedy, notas, nombresAdjuntos.get(0), rutasAdjuntos.get(0), nombresAdjuntos.get(1), rutasAdjuntos.get(1), nombresAdjuntos.get(2), rutasAdjuntos.get(2));
//            }
//
//            if(nombresAdjuntos.size()==2) {
//                ConsumeRemedyWSDLNotasINC.NotasIncidenteRemedy("C_IT_INC", "MESA_SERVICIO_SENHA2", INCRemedy, notas, nombresAdjuntos.get(0), rutasAdjuntos.get(0), nombresAdjuntos.get(1), rutasAdjuntos.get(1), "", "");
//            }
//
//            if(nombresAdjuntos.size()==1) {
//                ConsumeRemedyWSDLNotasINC.NotasIncidenteRemedy("C_IT_INC", "MESA_SERVICIO_SENHA2", INCRemedy, notas, nombresAdjuntos.get(0), rutasAdjuntos.get(0), "", "", "", "");
//            }
//            if(nombresAdjuntos.isEmpty()) {
                ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2", INCRemedy, notas, "", "", "", "", "", "");
//            }

            //String username, String password, String tipoOperacion, String nombreProveedor, String idTicketInterno, String notas, String archivo1, String archivo2
            //ConsumeRemedyWSDLNotasINC.AgregaNotaIncidenteRemedy("Demo","123","Bitacora","SAT","INC0001",notas,rutasAdjuntos.get(0) ,rutasAdjuntos.get(1));

//        }catch (Exception e){
//            System.out.println("Error al tratar de consumir o armar la peticion WSDL hacia remedy "+e);
//        }

        return new ResponseEntity<>("{\"respuesta\": \"Bitacora actualizado correctamente v2\", \"remedyid\": \"" + remedyid + "\", \"notas\": \"" + notas + "\"}", HttpStatus.OK);
    }


}
