package com.integracion.sdp.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.integracion.sdp.config.ConfigurationManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.integracion.sdp.dto.CommentInfo;
import com.integracion.sdp.dto.TaskInfo;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Comments {

    static ConfigurationManager configManager = ConfigurationManager.getInstance();

    static final String urlServicedesk = "http://" + configManager.getProperty("config.servicedesk.hostname") + ":" + configManager.getProperty("config.servicedesk.puerto");

    public static void main(String[] args) throws Exception {
        HttpResponse<String> responseTasks = Unirest.get(urlServicedesk + "/api/v3/tasks/")
                .header("Authtoken", configManager.getProperty("config.token"))
                .asString();

        // Supongamos que `jsonResponse` contiene la respuesta JSON que proporcionaste
        String jsonResponse = responseTasks.getBody();

        // Convertir la respuesta JSON a un árbol JSON
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode rootNode = objectMapper.readValue(jsonResponse, ObjectNode.class);

        // Obtener el array de tareas
        ArrayNode tasksNode = (ArrayNode) rootNode.get("tasks");

        // Filtrar las tareas que no están en los estados "Resuelto", "Cancelado" o "Cerrado"
        List<ObjectNode> filteredTasks = new ArrayList<>();
        for (JsonNode taskNode : tasksNode) {
            String statusName = taskNode.get("status").get("name").asText();
            JsonNode slineClienteNode = taskNode.get("udf_fields").get("sline_cliente"); // Obtener el nodo del campo "sline_cliente"
            if (slineClienteNode != null && slineClienteNode.asText().equals("SAT") &&
                    !statusName.equals("Resuelto") && !statusName.equals("Cancelado") && !statusName.equals("Cerrado")) {
                filteredTasks.add((ObjectNode) taskNode);
                System.out.println("Tarea identificada" + taskNode.get("id").toString());
                System.out.println("Id de remedy: "+ taskNode.get("udf_fields").get("sline_id_remedy"));
                //BuscaComentarios(taskNode.get("id").asInt(),taskNode.get("udf_fields").get("sline_id_remedy").toString() );
            }
        }


        // Ordenar las tareas por el ID de la tarea
        filteredTasks.sort(Comparator.comparing(task -> task.get("id").asInt()));

        // Procesar la respuesta para obtener la información requerida
        List<TaskInfo> taskInfoList = procesarRespuesta(filteredTasks);

        // Imprimir la lista de información de las tareas
        for (TaskInfo taskInfo : taskInfoList) {
            System.out.println(taskInfo);
        }

        BuscaComentarios(taskInfoList);
    }

    // Método para procesar la respuesta y filtrar los atributos requeridos
    public static List<TaskInfo> procesarRespuesta(List<ObjectNode> filteredTasks) {
        // Crear una lista para almacenar los objetos TaskInfo
        List<TaskInfo> taskInfoList = new ArrayList<>();

        // Iterar sobre las tareas filtradas
        for (JsonNode taskNode : filteredTasks) {
            // Obtener los atributos requeridos de cada tarea
            int taskId = taskNode.get("id").asInt();
            String slineIdRemedy = taskNode.get("udf_fields").get("sline_id_remedy").asText();
            String statusName = taskNode.get("status").get("name").asText();

            // Crear una nueva instancia de TaskInfo con los atributos filtrados
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setId(taskId);
            taskInfo.setSlineIdRemedy(slineIdRemedy);
            taskInfo.setStatusName(statusName);

            // Agregar el objeto TaskInfo a la lista
            taskInfoList.add(taskInfo);
        }

        // Devolver la lista de objetos TaskInfo filtrados
        return taskInfoList;
    }



    public static void BuscaComentarios(List<TaskInfo> tareas) {
        System.out.println("Dentro de la funcion BuscaComentarios");
        for (TaskInfo taskInfo : tareas) {
            int taskId = taskInfo.getId();

            System.out.println("ID de la tarea: " + taskId);
            try {
                System.out.println("Prueba de consumo para obtener comentarios");
                // Hacer la solicitud para obtener los comentarios de la tarea específica
                HttpResponse<String> responseComments = Unirest.get(urlServicedesk + "/api/v3/tasks/" + taskId + "/comments/")
                        .header("Authtoken", configManager.getProperty("config.token"))
                        .asString();

                String commentsResponse = responseComments.getBody();

                // Procesar la respuesta para obtener los comentarios
                List<CommentInfo> commentInfoList = procesarComentarios(commentsResponse);

                // Mostrar los comentarios
                for (CommentInfo commentInfo : commentInfoList) {
                    // Verificar que el creador del comentario no sea "administrator"
                    System.out.println("Comentarios finales ");
                        System.out.println("taskId: " + taskId);
                        System.out.println("ID: " + commentInfo.getId());
                        System.out.println("Cantidad de respuestas: " + commentInfo.getReplyCount());
                        System.out.println("Contenido: " + commentInfo.getContent());
                        System.out.println();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Antes del try de consumo ");
    }




    public static List<CommentInfo> procesarComentarios(String commentsResponse) throws Exception {
        List<CommentInfo> commentInfoList = new ArrayList<>();

        // Convertir la respuesta JSON a un árbol JSON
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode commentsRootNode = objectMapper.readTree(commentsResponse);

        // Verificar el estado de la respuesta
        JsonNode responseStatusNode = commentsRootNode.get("response_status");
        if (responseStatusNode != null && responseStatusNode.isArray() && responseStatusNode.size() > 0) {
            JsonNode statusNode = responseStatusNode.get(0);
            int statusCode = statusNode.get("status_code").asInt();
            if (statusCode == 2000) { // Verificar si la solicitud fue exitosa
                // Obtener la lista de comentarios
                JsonNode commentsNode = commentsRootNode.get("comments");
                if (commentsNode != null && commentsNode.isArray()) {
                    for (JsonNode comment : commentsNode) {
                        // Verificar que el comentario no sea creado por "administrator"
                        JsonNode createdByNode = comment.get("created_by");
                        if (createdByNode != null && !"administrator".equals(createdByNode.get("name").asText())) {
                            // Obtener el replyCount
                            int replyCount = comment.get("reply_count").asInt();

                            // Solo procesar comentarios con replyCount igual a cero
                            if (replyCount == 0) {
                                // Obtener los datos relevantes de cada comentario
                                String id = comment.get("id").asText();
                                String content = comment.get("content").asText();

                                // Crear un objeto CommentInfo y agregarlo a la lista
                                CommentInfo commentInfo = new CommentInfo(id, replyCount, content);
                                commentInfoList.add(commentInfo);
                            }
                        }
                    }
                }
            }
        }else {
            // Manejar el caso en que no se puede encontrar el estado de la respuesta
            System.out.println("No se pudo encontrar el estado de la respuesta de comentarios.");
        }

        return commentInfoList;
    }

}
