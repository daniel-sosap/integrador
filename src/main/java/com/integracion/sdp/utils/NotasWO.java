package com.integracion.sdp.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integracion.sdp.config.ConfigurationManager;
import com.integracion.sdp.controller.RemedyController;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class NotasWO {

    static ConfigurationManager configManager = ConfigurationManager.getInstance();

    static final String urlServicedesk = "http://" + configManager.getProperty("config.servicedesk.hostname") + ":" + configManager.getProperty("config.servicedesk.puerto");

    @Scheduled(fixedRate = 15000) // Se ejecutará cada minuto (60000 ms)
    public void ejecutarActualizacionDeNotas() {
  // public static void main(String[] args) {
        try {
            if(configManager.getProperty("config.wo.mode").toString().equals("on")) {
                System.out.println("Envio de Adjuntos notas activo");
                actualizaNotasAdjuntoWO();
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void actualizaNotasAdjuntoWO() throws JsonProcessingException {
        System.out.println("Inicia el metodo NotasWO/ actualizaNotasAdjuntoWO");
        HttpResponse<String> responseTasks = Unirest.get(urlServicedesk + "/api/v3/tasks/?input_data=%7B%22list_info%22%3A%7B%22row_count%22%3A100%2C%22start_index%22%3A0%20%2C%22get_total_count%22%3A%22true%22%2C%22sort_field%22%3A%22id%22%2C%22sort_order%22%3A%22asc%22%2C%22fields_required%22%3A%5B%20%22id%22%2C%20%22status.name%22%2C%20%22title%22%2C%22udf_fields.sline_id_remedy%22%5D%2C%22search_criteria%22%3A%20%5B%7B%22field%22%3A%20%22status.name%22%2C%22condition%22%3A%20%22is%20not%22%2C%22logical_operator%22%3A%20%22and%22%2C%22values%22%3A%20%5B%22Cerrado%22%2C%22Cancelado%22%2C%20%22Resuelto%22%5D%7D%5D%7D%7D%26format%3Djson")
                .header("Authtoken", configManager.getProperty("config.token"))
                .asString();

        // Supongamos que `jsonResponse` contiene la respuesta JSON que proporcionaste
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
            //BuscaAdjuntos(tareaId, idRemedy); //omiti esta
            RemedyController.actualizaAdjuntosWO(tareaId);

        }

    }






}
