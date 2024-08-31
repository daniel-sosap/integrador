package com.integracion.sdp.endpoint;

import com.integracion.sdp.client.ConsumeSDPWO;
import com.integracion.sdp.config.ConfigurationManager;
import com.integracion.sdp.gen.*;
import com.integracion.sdp.dto.AdjuntoInfo;
import com.integracion.sdp.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class WorkOrderEndpoint {

    @Autowired
    private WSDLRequestWO WSDLRequestWO;
    static ConfigurationManager configManager = ConfigurationManager.getInstance();
    @Autowired
    private ConsumeSDPWO consumeSDPWO;

    @Autowired
    private WSDLStatusWoRequest WSDLStatusWoRequest;

    @Autowired
    private WSDLWorklogWORequest WSDLWorklogWORequest;

    private static final String NAMESPACE_URI = "http://AMINTUBSRVITSM1A/sdp/gen";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postWORequest")
    @ResponsePayload
    public PostWOResponse postWORequest(@RequestPayload PostWORequest request) {
        // Procesar la solicitud y devolver la respuesta
        PostWOResponse response = new PostWOResponse();
        // Lógica de procesamiento de la solicitud de workorder
        WorkOrder WOSAT = request.getWorkOrder();
        WOSAT.setPrioridad(WSDLRequestWO.mapeoPrioridad(WOSAT.getPrioridad()));


        System.out.println("WO Recibido");
        WSDLRequestWO.imprimeWO(WOSAT);
        //Valida el WO
        String resultadoTransaccion = WSDLRequestWO.validaWO(WOSAT);
        WOResponse woSDP = new WOResponse();

        //Si la validacion mostro algun error
        if(!resultadoTransaccion.isEmpty()) {
            woSDP.setIdsdp("NA");
            woSDP.setMensajeTransaccion(resultadoTransaccion);
            woSDP.setResultadoTransaccion("Error");
        }

        if(!WOSAT.getToken().equals(configManager.getProperty("config.token"))){
            resultadoTransaccion = "Error token invalido";
            woSDP.setIdsdp("NA");
            woSDP.setMensajeTransaccion(resultadoTransaccion);
            woSDP.setResultadoTransaccion("Error");
        }

        //Si la validacion no mostro error
        else if (resultadoTransaccion.isEmpty()){
            System.out.println("Comienza la ejecución de la creacin de WO");
            woSDP =consumeSDPWO.creaTask(WOSAT);

        }
        // Imprime el estado de la transacción
        System.out.println("Ejecucion de metodo PostIncidentResponse: " + woSDP.getIdsdp());


        response.setWOResponse(woSDP);

        return response;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "statusWORequest")
    @ResponsePayload
    public StatusWOResponse StatusWORequest(@RequestPayload StatusWORequest request) {

        StatusWOResponse response = new StatusWOResponse();

        System.out.println(request.getWoUpdate().getIdsdp());
        System.out.println(request.getWoUpdate().getCliente());

        // Obtiene el incidente recibido por WSDL
        WoUpdate woUpdate = request.getWoUpdate();


        woUpdate.setPrioridad(WSDLStatusWoRequest.mapeoPrioridad(woUpdate.getPrioridad()));
        woUpdate.setStatus(WSDLRequestWO.mapeoStatus(woUpdate.getStatus()));

        System.out.println("Actualizacion de Incidente Recibido");
        WSDLStatusWoRequest.imprimeWoUpdate(woUpdate);
        // Valida el incidente
        String resultadoTransaccion = WSDLStatusWoRequest.validaWoUpdate(woUpdate);
        UpdateWOResponse statusResponse = new UpdateWOResponse();
        statusResponse.setIdsdp(woUpdate.getIdsdp());
        statusResponse.setMensajeTransaccion("Completada");
        statusResponse.setResultadoTransaccion("Exitoso");
        response.setUpdateWOResponse(statusResponse);
        //Si la validacion mostro algun error
        if(!resultadoTransaccion.isEmpty()) {
            statusResponse.setIdsdp("NA");
            statusResponse.setMensajeTransaccion(resultadoTransaccion);
            statusResponse.setResultadoTransaccion("Error");
            response.setUpdateWOResponse(statusResponse);
            return response;
        }
        //Si la validacion no mostro error
        else if (resultadoTransaccion.isEmpty()){
            statusResponse =consumeSDPWO.updateTask(woUpdate);
            System.out.println("Consumo del servicio Update Request");
            statusResponse.setIdsdp(woUpdate.getIdsdp());

        }
        // Imprime el estado de la transacción
        System.out.println(statusResponse.toString());
        System.out.println("Ejecucion de metodo PostIncidentResponse: " + woUpdate.getIdsdp());
        return response;
    }

//++++++++++++++++++++++++++++++++++++++++++++
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "worklogWORequest")
    @ResponsePayload
    public WorklogWOResponse worklogWORequest(@RequestPayload WorklogWORequest request) {

        System.out.println("Creacion de Bitacora de Tareas desde Remedy");
        WorklogWOResponse response = new WorklogWOResponse();

        CommentWOResponse responseWSDL = new CommentWOResponse();
        response.setCommentWOResponse(responseWSDL);
        responseWSDL.setIdsdp("OK");
        responseWSDL.setMensajeTransaccion("Mensaje de prueba");
        responseWSDL.setResultadoTransaccion("Completada");

        System.out.println("Bitacora de tarea desde Remedy" + "\n Cliente: " + request.getWOworklog().getCliente() + "\n ID ServiceDesk: " +request.getWOworklog().getIdsdp() + "\n Token: " +request.getWOworklog().getToken() +"\n Nota: " +request.getWOworklog().getNotas());
        System.out.println("");
        if(!request.getWOworklog().getToken().equals(configManager.getProperty("config.token"))){
            responseWSDL.setIdsdp("NA");
            responseWSDL.setMensajeTransaccion("Error token invalido");
            responseWSDL.setResultadoTransaccion("Error");
            response.setCommentWOResponse(responseWSDL);
            return response;
        }

        if (request.getWOworklog().getNotas().isEmpty()){
            responseWSDL.setResultadoTransaccion("Error");
            responseWSDL.setIdsdp("0");
            responseWSDL.setMensajeTransaccion("El campo Nota es obligatorio");
            response.setCommentWOResponse(responseWSDL);
            System.out.println("Bitacora con campo vacio, error");
            return response;
        }

        if (request.getWOworklog().getIdsdp().isEmpty()){
            responseWSDL.setResultadoTransaccion("Error");
            responseWSDL.setIdsdp("0");
            responseWSDL.setMensajeTransaccion("El campo ID Service Desk plus es obligatorio");
            response.setCommentWOResponse(responseWSDL);
            System.out.println("Bitacora con campo vacio, error");
            return response;
        }

        WSDLWorklogWORequest.imprimeWOWorklog(request);
        List<AdjuntoInfo> adjuntosGuardados = WSDLWorklogWORequest.validaAdjuntos(request);
       // response = WSDLWorklogIncidentRequest.validaWorklogIncident(request);
        System.out.println("Consumo de adjuntos de Tareas");
      responseWSDL =consumeSDPWO.addNoteTask(request.getWOworklog().getIdsdp(),request.getWOworklog().getNotas());
//
        if (!adjuntosGuardados.isEmpty()) {
            System.out.println("Nota con adjuntos");
            int adjuntoError = 0;
            for (AdjuntoInfo adjunto : adjuntosGuardados) {
                // Llamar a la función addNoteAttachmentRequest para cada adjunto
                if (!consumeSDPWO.addNoteAttachmentRequest(adjunto, request.getWOworklog().getIdsdp(), responseWSDL.getIdsdp())) {
                    adjuntoError++;
                }
            }
            System.out.println("Se encontraron " + adjuntoError + " con error");
        }

        response.setCommentWOResponse(responseWSDL);
        return response;
    }


}
