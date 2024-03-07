package com.integracion.sdp.endpoint;

import com.integracion.sdp.gen.PostWORequest;
import com.integracion.sdp.gen.PostWOResponse;
import com.integracion.sdp.gen.WOResponse;
import com.integracion.sdp.gen.WorkOrder;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class WorkOrderEndpoint {

    private static final String NAMESPACE_URI = "http://AMINTUBSRVITSM1A/sdp/gen";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postWORequest")
    @ResponsePayload
    public PostWOResponse postWORequest(@RequestPayload PostWORequest request) {
        // Procesar la solicitud y devolver la respuesta
        PostWOResponse response = new PostWOResponse();
        // Lógica de procesamiento de la solicitud de workorder
        WOResponse woResponse = new WOResponse();
        woResponse.setIdsdp("NA");
        woResponse.setMensajeTransaccion("Tarea Registrada");
        woResponse.setResultadoTransaccion("Completada");
        response.setWOResponse(woResponse);
        return response;
    }
}
