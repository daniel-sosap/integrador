package com.integracion.sdp.controller;
import com.google.gson.*;
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
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api/remedy")
public class RemedyController {

    static ConfigurationManager configManager = ConfigurationManager.getInstance();
    static final String urlServicedesk = "http://"+configManager.getProperty("config.servicedesk.hostname")+":"+configManager.getProperty("config.servicedesk.puerto");



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
        System.out.println("Se consumio agregar /agreganotasadjunto");
        System.out.printf("Se recibio la nota: " + notas);
        if (!file.isEmpty()) {
            try {
                String adjuntoNombre = file.getOriginalFilename();
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

    @PostMapping("/notaconadjuntowo/{remedyid}")
    public ResponseEntity<String> agregaNotasConAdjuntoWO(@PathVariable String remedyid,
                                                                 @RequestParam("notas") String notas,
                                                                 @RequestParam("file") MultipartFile file) {
        System.out.println("Se consumio agregar /agreganotasadjunto WO");
        System.out.printf("Se recibio la nota: " + notas);
        if (!file.isEmpty()) {
            try {
                String adjuntoNombre = file.getOriginalFilename();
                String rutaArchivo = System.getProperty("java.io.tmpdir") + File.separator + adjuntoNombre;
                FileOutputStream fos = new FileOutputStream(rutaArchivo);
                fos.write(file.getBytes());
                fos.close();
                System.out.println("Archivo guardado en la carpeta temporal: " + rutaArchivo);
                ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2", remedyid, notas, "adjuntoNombre", "rutaArchivo", "", "", "", "");

            } catch (Exception e) {
                System.err.println("Error al guardar el archivo en la carpeta temporal: " + e.getMessage());
                e.printStackTrace();
            }
        }

        return new ResponseEntity<>("{\"respuesta\": \"Incidente actualizado correctamente\", \"remedyid\": \"" + remedyid + "\", \"notas\": \"" + notas + "\"}", HttpStatus.OK);
    }

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

        String INCRemedy = remedyid;

        System.out.println("Valor de udf_sline_301: " + INCRemedy);

                ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2", INCRemedy, notas, "", "", "", "", "", "");


        return new ResponseEntity<>("{\"respuesta\": \"Bitacora actualizado correctamente v2\", \"remedyid\": \"" + remedyid + "\", \"notas\": \"" + notas + "\"}", HttpStatus.OK);
    }


    @PostMapping("/actualizawo/{taskid}")
    public ResponseEntity<String> actualizaWO(@PathVariable String taskid) {

        System.out.println("Se recibio el ticket /actualizawo/{taskid}" + taskid);

        HttpResponse<String> response = Unirest.get(urlServicedesk+"/api/v3/tasks/"+taskid)
                .header("Authtoken", configManager.getProperty("config.token"))
                .asString();

        System.out.println("Respuesta del servicio " + response.getBody());
        String responseBody = response.getBody();

        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        // Obtener el valor de "sline_id_remedy"
        String idremedy = jsonObject.getAsJsonObject("task")
                .getAsJsonObject("udf_fields")
                .getAsJsonPrimitive("sline_id_remedy")
                .getAsString();
        //System.out.println("ID de remedy " + idremedy);
        List<String> rutasAdjuntos = new ArrayList<>();
        List<String> nombresAdjuntos = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convertir el JSON a un árbol de nodos JsonNode
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            // Obtener el nodo "task" si existe
            JsonNode taskNode = rootNode.path("task");
            // Verificar si el nodo "task" no es nulo
            if (!taskNode.isMissingNode()) {
                // Obtener el nodo "attachments" si existe
                JsonNode attachmentsNode = taskNode.path("attachments");
                // Iterar sobre los nodos de los adjuntos
                for (JsonNode attachmentNode : attachmentsNode) {
                    // Verificar las condiciones antes de acceder a los campos
                    if (attachmentNode.has("attached_by") && !attachmentNode.path("attached_by").isNull() &&
                            attachmentNode.has("description") && !attachmentNode.path("description").isNull() &&
                            !attachmentNode.path("description").asText().contains("Enviado")) {
                        // Acceder a los campos y realizar las acciones necesarias
                        String contentType = attachmentNode.path("content_type").asText();
                        int size = attachmentNode.path("size").path("value").asInt();
                        String name = attachmentNode.path("name").asText();
                        String contentUrl = attachmentNode.path("content_url").asText();
                        int id = attachmentNode.path("id").asInt();
                        // Imprimir los detalles del adjunto
                        System.out.println("ContentType: " + contentType);
                        System.out.println("Size: " + size);
                        System.out.println("Name: " + name);
                        System.out.println("ContentUrl: " + contentUrl);
                        System.out.println("Id:" + id);

                        // Carpeta temporal donde se guardarán los archivos
                        String carpetaTemporal = System.getProperty("java.io.tmpdir");

                            rutasAdjuntos.add(descargarArchivo(carpetaTemporal, name, contentUrl));
                            nombresAdjuntos.add(name);

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Manejar el error de lectura del JSON
        }

        try {
            if(nombresAdjuntos.size()==3) {
                ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2", idremedy, "Adjunto", nombresAdjuntos.get(0), rutasAdjuntos.get(0), nombresAdjuntos.get(1), rutasAdjuntos.get(1), nombresAdjuntos.get(2), rutasAdjuntos.get(2));
            }

            if(nombresAdjuntos.size()==2) {
                ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2",idremedy , "Adjunto", nombresAdjuntos.get(0), rutasAdjuntos.get(0), nombresAdjuntos.get(1), rutasAdjuntos.get(1), "", "");
            }

            if(nombresAdjuntos.size()==1) {
                ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2", idremedy, "Adjunto", nombresAdjuntos.get(0), rutasAdjuntos.get(0), "", "", "", "");
            }
            if(nombresAdjuntos.isEmpty()) {
             //   ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2", idremedy, "Adjunto", "", "", "", "", "", "");
            }

            //String username, String password, String tipoOperacion, String nombreProveedor, String idTicketInterno, String notas, String archivo1, String archivo2
            //ConsumeRemedyWSDLNotasINC.AgregaNotaIncidenteRemedy("Demo","123","Bitacora","SAT","INC0001",notas,rutasAdjuntos.get(0) ,rutasAdjuntos.get(1));

        }catch (Exception e){
            System.out.println("Error al tratar de consumir o armar la peticion WSDL hacia remedy "+e);
        }


        return new ResponseEntity<>("{\"respuesta\": \"WO actualizado correctamente\"" +
                "\"idRespuesta\": \""+taskid+"\"" +
                "}", HttpStatus.OK);
    }


    public static void actualizaAdjuntosWO(String taskid) {

        //System.out.println("Se recibio el ticket " + taskid);

        HttpResponse<String> response = Unirest.get(urlServicedesk+"/api/v3/tasks/"+taskid)
                .header("Authtoken", configManager.getProperty("config.token"))
                //.header("Cookie", "SDPSESSIONID=F3CDBC4300CE0460466E0ECC3213316F; _zcsr_tmp=6a64161e-d4e8-42ae-b1be-6805eac90d89; sdpcsrfcookie=6a64161e-d4e8-42ae-b1be-6805eac90d89; sdplogincsrfcookie=938f1307-1324-4d43-b513-1bd55d798d04")
                .asString();

//        System.out.println("Respuesta del servicio " + response.getBody());
        String responseBody = response.getBody();

        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();

        // Obtener el valor de "sline_id_remedy"
        String idremedy = jsonObject.getAsJsonObject("task")
                .getAsJsonObject("udf_fields")
                .getAsJsonPrimitive("sline_id_remedy")
                .getAsString();
        //System.out.println("ID de remedy " + idremedy);
        List<String> rutasAdjuntos = new ArrayList<>();
        List<String> nombresAdjuntos = new ArrayList<>();
        String idAttach = "";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Convertir el JSON a un árbol de nodos JsonNode
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            // Obtener el nodo "task" si existe
            JsonNode taskNode = rootNode.path("task");
            // Verificar si el nodo "task" no es nulo
            if (!taskNode.isMissingNode()) {
                // Obtener el nodo "attachments" si existe
                JsonNode attachmentsNode = taskNode.path("attachments");
                // Iterar sobre los nodos de los adjuntos
                for (JsonNode attachmentNode : attachmentsNode) {

                    if ((attachmentNode.has("attached_by") && !attachmentNode.path("attached_by").isNull() &&
                            attachmentNode.has("description") && !attachmentNode.path("description").asText().contains("enviado"))  &&
                    !"administrator".equals(attachmentNode.path("attached_by").path("name").asText())){
                        // Acceder a los campos y realizar las acciones necesarias
                        String contentType = attachmentNode.path("content_type").asText();
                        int size = attachmentNode.path("size").path("value").asInt();
                        String name = attachmentNode.path("name").asText();
                        String contentUrl = attachmentNode.path("content_url").asText();
                         idAttach = attachmentNode.path("id").asText();
                        // Imprimir los detalles del adjunto
                        System.out.println("ContentType: " + contentType);
                        System.out.println("Size: " + size);
                        System.out.println("Name: " + name);
                        System.out.println("ContentUrl: " + contentUrl);
                        System.out.println("Id:" + idAttach);

                        // Carpeta temporal donde se guardarán los archivos
                        String carpetaTemporal = System.getProperty("java.io.tmpdir");

                        rutasAdjuntos.add(descargarArchivo(carpetaTemporal, name, contentUrl));
                        nombresAdjuntos.add(name);

                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Manejar el error de lectura del JSON
        }

        try {
            if(nombresAdjuntos.size()==3) {
                ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2", idremedy, "Adjunto", nombresAdjuntos.get(0), rutasAdjuntos.get(0), nombresAdjuntos.get(1), rutasAdjuntos.get(1), nombresAdjuntos.get(2), rutasAdjuntos.get(2));
           for(int i = 0; i<3 ; i++){
               restableceArchivo(taskid, idAttach, rutasAdjuntos.get(i));
           }

            }

            if(nombresAdjuntos.size()==2) {
                ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2",idremedy , "Adjunto", nombresAdjuntos.get(0), rutasAdjuntos.get(0), nombresAdjuntos.get(1), rutasAdjuntos.get(1), "", "");
                for(int i = 0; i<2 ; i++){
                    restableceArchivo(taskid, idAttach, rutasAdjuntos.get(i));
                }
            }

            if(nombresAdjuntos.size()==1) {
                ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2", idremedy, "Adjunto", nombresAdjuntos.get(0), rutasAdjuntos.get(0), "", "", "", "");
                for(int i = 0; i<1 ; i++){
                    restableceArchivo(taskid, idAttach, rutasAdjuntos.get(i));
                }
            }
            if(nombresAdjuntos.isEmpty()) {
                //   ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2", idremedy, "Adjunto", "", "", "", "", "", "");
            }


        }catch (Exception e){
            System.out.println("Error al tratar de consumir o armar la peticion WSDL hacia remedy "+e);
        }


    }

    static void restableceArchivo(String idtask, String idAttach, String ruta){


//Delete
        try {
            HttpResponse<String> response = Unirest.delete(urlServicedesk+"/api/v3/tasks/"+idtask+"/attachments/"+idAttach+"/")
                    //.header("Authorization", "687eb536a6b20140dd8dc01d8e2109987BEC15D3-1C3F-4AB2-8C0A-3CF6821C8A60")
                    .header("Accept", "application/v3+json")
                    .header("Authtoken", configManager.getProperty("config.token"))
                    .header("Cookie", "SDPSESSIONID=85CAE84CE2E96B77C60C91AC348BDC8A; _zcsr_tmp=81f1eab1-759f-492d-a10d-ded16d75c130; sdpcsrfcookie=81f1eab1-759f-492d-a10d-ded16d75c130; sdplogincsrfcookie=6f8dc3b2-494d-4504-994e-f5ff83974956")
                    .asString();

            System.out.println("Respuesta del servicio: " + response.getBody());
            System.out.println("Código de estado HTTP: " + response.getStatus());
            System.out.println("Se elimina el archivo");
        } catch (Exception e) {
            e.printStackTrace();
        }
//carga


        try {

            HttpResponse<String> response = Unirest.put(urlServicedesk+"/api/v3/tasks/"+idtask+"/_upload")
                    //.header("Authorization", "687eb536a6b20140dd8dc01d8e2109987BEC15D3-1C3F-4AB2-8C0A-3CF6821C8A60")
                    .header("Accept", "application/v3+json")
                    .header("Authtoken", configManager.getProperty("config.token"))
                    //.header("Cookie", "SDPSESSIONID=85CAE84CE2E96B77C60C91AC348BDC8A; _zcsr_tmp=81f1eab1-759f-492d-a10d-ded16d75c130; sdpcsrfcookie=81f1eab1-759f-492d-a10d-ded16d75c130; sdplogincsrfcookie=6f8dc3b2-494d-4504-994e-f5ff83974956")
                    .field("file", new File(ruta))
                    .field("description", "enviado")
                    .asString();

            System.out.println("Respuesta del servicio: " + response.getBody());
            System.out.println("Código de estado HTTP: " + response.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
