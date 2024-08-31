package com.integracion.sdp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.integracion.sdp.config.ConfigurationManager;
import com.integracion.sdp.dto.CommentInfo;
import com.integracion.sdp.dto.TaskInfo;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
@Component
public class Commentarios {

    static ConfigurationManager configManager = ConfigurationManager.getInstance();

    static final String urlServicedesk = "http://" + configManager.getProperty("config.servicedesk.hostname") + ":" + configManager.getProperty("config.servicedesk.puerto");

    @Scheduled(fixedRate = 25000) // Se ejecutará cada minuto (60000 ms)
    public void ejecutarActualizacionDeNotas() {
        try {
            if(configManager.getProperty("config.wo.mode").toString().equals("on")) {
                System.out.println("Envio de notas activo");
                actualizaNotasWO();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void actualizaNotasWO() throws JsonProcessingException {
        HttpResponse<String> responseTasks = Unirest.get(urlServicedesk + "/api/v3/tasks/?input_data=%7B%22list_info%22%3A%7B%22row_count%22%3A100%2C%22start_index%22%3A0%20%2C%22get_total_count%22%3A%22true%22%2C%22sort_field%22%3A%22id%22%2C%22sort_order%22%3A%22asc%22%2C%22fields_required%22%3A%5B%20%22id%22%2C%20%22status.name%22%2C%20%22title%22%2C%22udf_fields.sline_id_remedy%22%5D%2C%22search_criteria%22%3A%20%5B%7B%22field%22%3A%20%22status.name%22%2C%22condition%22%3A%20%22is%20not%22%2C%22logical_operator%22%3A%20%22and%22%2C%22values%22%3A%20%5B%22Cerrado%22%2C%22Cancelado%22%2C%20%22Resuelto%22%5D%7D%5D%7D%7D%26format%3Djson")
                .header("Authtoken", configManager.getProperty("config.token"))
                .asString();

        String jsonResponse = responseTasks.getBody();
        System.out.println("jsonResponse" + jsonResponse);
        // Convertir la respuesta JSON a un árbol JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

       // String jsonResponse = responseTasks.getBody();

//         Convertir la respuesta JSON en un objeto JSONObject
        JSONObject tasks = new JSONObject(jsonResponse);

        // Filtrar las tareas
        //List<JSONObject> tareasFiltradas = filtrarTareas(tasks);

        JSONArray tasksArray = tasks.getJSONArray("tasks");
        for (int i = 0; i < tasksArray.length(); i++) {
            JSONObject tarea = tasksArray.getJSONObject(i);
            JSONObject status = tarea.getJSONObject("status");
            JSONObject udfFields = tarea.getJSONObject("udf_fields");

            //String statusName = status.getString("name");
            String statusName = status.getString("name");
            String idRemedy = udfFields.getString("sline_id_remedy");
            String tareaId = tarea.getString("id");
            //System.out.println("Tarea filtrada " + tareaId);

                System.out.println("Tarea filtrada " + tareaId);
                System.out.println("Tarea statusName " + statusName);
                System.out.println("Tarea sline_id_remedy " + idRemedy);
            BuscaComentarios(tareaId, idRemedy);

        }

        //BuscaComentarios();
    }


    public static void BuscaComentarios(String tarea, String idRemedy) {
        try {
            // Hacer la solicitud para obtener los comentarios de la tarea específica
            HttpResponse<String> responseComments = Unirest.get(urlServicedesk + "/api/v3/tasks/" + tarea + "/comments/?input_data=%7B%22list_info%22%3A%7B%22row_count%22%3A100%2C%22start_index%22%3A0%2C%22get_total_count%22%3A%22true%22%2C%22sort_field%22%3A%22id%22%2C%22sort_order%22%3A%22asc%22%2C%22search_criteria%22%3A%5B%7B%22field%22%3A%22created_by.name%22%2C%22condition%22%3A%22is%20not%22%2C%22values%22%3A%5B%22administrator%22%5D%7D%2C%7B%22field%22%3A%22reply_count%22%2C%22condition%22%3A%22is%22%2C%22logical_operator%22%3A%22and%22%2C%22values%22%3A%5B%220%22%5D%7D%5D%7D%7D")
                    .header("Authtoken", configManager.getProperty("config.token"))
                    .asString();

            // Parsear la respuesta JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(responseComments.getBody());


            // Verificar si hay comentarios
            if (rootNode.has("comments")) {
                // Obtener la lista de comentarios
                JsonNode commentsNode = rootNode.get("comments");
                // Iterar sobre cada comentario
                for (JsonNode comment : commentsNode) {
                    // Extraer el contenido de cada comentario
                    String content = comment.get("content").asText();
                    String idComentario = comment.get("id").asText();
                    int contador = comment.get("reply_count").asInt();
                    // Imprimir el contenido

                    Document doc = Jsoup.parseBodyFragment(content);
                    content = doc.body().text();
                    System.out.println("Contador de comentario: " + contador);
                    System.out.println("Contenido del comentario: " + content);
                    if(contador == 0) {
                        System.out.println("Se envia la nota por que se cumple el contador");
                        ConsumeRemedyWSDLNotasWO.CreaBitacoraRemedy("C_IT_WO", "MESA_SERVICIO_SENHA2", idRemedy, content, "", "", "", "", "", "");
                        actualizaStatusComentario(tarea, idComentario);
                    }
                    else {
                        System.out.println("No se envia nota por que no cumple el contador");
                    }
                }
            } else {
                System.out.println("No se encontraron comentarios.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

public static void actualizaStatusComentario(String tarea, String idComentario){

    try {
        // Hacer la solicitud para obtener los comentarios de la tarea específica
        System.out.println("URL de ServideDeskplus: " + urlServicedesk );
        String input_data = "{\"comment\":{\"content\":\"Enviado a remedy\"}}";
        HttpResponse<String> response = Unirest.post(urlServicedesk+"/api/v3/tasks/"+tarea+"/comments/"+idComentario+"/_reply")
                .header("Accept", "application/vnd.manageengine.sdp.v3+json")
                .header("Authtoken", configManager.getProperty("config.token"))
                .multiPartContent()
                .field("input_data", input_data)
                .asString();

        System.out.println("Consumo de servicio Service desk plus actualizacion de nota");
        System.out.println(response);
        // Parsear la respuesta JSON

    }
    catch (Exception e){
        System.out.println("Error" + e);
    }

}

}
