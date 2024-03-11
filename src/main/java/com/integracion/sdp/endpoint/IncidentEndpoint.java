package com.integracion.sdp.endpoint;

import com.integracion.sdp.client.ConsumeSDP;
import com.integracion.sdp.config.ConfigurationManager;
import com.integracion.sdp.converter.IncidentConverter;
import com.integracion.sdp.gen.*;
import com.integracion.sdp.model.AdjuntoInfo;
import com.integracion.sdp.model.IncidentModel;
import com.integracion.sdp.repository.IncidentRepository;
import com.integracion.sdp.utils.ConsumeSDPAdjunto;
import com.integracion.sdp.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Endpoint
public class IncidentEndpoint {

    private static final String NAMESPACE_URI = "http://AMINTUBSRVITSM1A/sdp/gen";

    @Autowired
    private IncidentRepository incidentRepository;
    @Autowired
    private IncidentConverter incidentConverter;
    @Autowired
    private ConsumeSDP consumeSDP;
    @Autowired
    private ConsumeSDPAdjunto consumeSDPAdjunto;
    @Autowired
    private WSDLRequestIncidente WSDLRequestIncidente;
    @Autowired
    private WSDLStatusIncidentRequest WSDLStatusIncidentRequest;
    @Autowired
    private WSDLWorklogIncidentRequest WSDLWorklogIncidentRequest;
    static ConfigurationManager configManager = ConfigurationManager.getInstance();
    String resultadoTransaccion;



    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "worklogIncidentRequest")
    @ResponsePayload
    public WorklogIncidentResponse worklogIncidentRequest(@RequestPayload WorklogIncidentRequest request) {


       WorklogIncidentResponse response = new WorklogIncidentResponse();

       WorklogResponse responseWSDL = new WorklogResponse();
       response.setWorklogResponse(responseWSDL);

        System.out.println("Bitacora desde Remedy" + "\n" + request.getWorklog().getCliente() + "\n" +request.getWorklog().getIdsdp() + "\n" +request.getWorklog().getToken() +"\n" +request.getWorklog().getNotas());

        if(!request.getWorklog().getToken().equals(configManager.getProperty("config.token"))){
            responseWSDL.setIdsdp("NA");
            responseWSDL.setMensajeTransaccion("Error token invalido");
            responseWSDL.setResultadoTransaccion("Error");
            response.setWorklogResponse(responseWSDL);
            return response;
        }

        if (request.getWorklog().getNotas().isEmpty()){
            responseWSDL.setResultadoTransaccion("Error");
            responseWSDL.setIdsdp("0");
            responseWSDL.setMensajeTransaccion("El campo Nota es obligatorio");
            response.setWorklogResponse(responseWSDL);
            System.out.println("Bitacora con campo vacio, error");
            return response;
        }

        if (request.getWorklog().getIdsdp().isEmpty()){
            responseWSDL.setResultadoTransaccion("Error");
            responseWSDL.setIdsdp("0");
            responseWSDL.setMensajeTransaccion("El campo ID Service Desk plus es obligatorio");
            response.setWorklogResponse(responseWSDL);
            System.out.println("Bitacora con campo vacio, error");
            return response;
        }

        WSDLWorklogIncidentRequest.imprimeIncidentUpdate(request);
        List<AdjuntoInfo> adjuntosGuardados = WSDLWorklogIncidentRequest.validaAdjuntos(request);
        //response = WSDLWorklogIncidentRequest.validaWorklogIncident(request);
        responseWSDL =consumeSDP.addNoteRequest(request.getWorklog().getIdsdp(),request.getWorklog().getNotas());

        if (!adjuntosGuardados.isEmpty()) {
            System.out.println("Nota con adjuntos");
            int adjuntoError = 0;
            for (AdjuntoInfo adjunto : adjuntosGuardados) {
                // Llamar a la función addNoteAttachmentRequest para cada adjunto
                if (!consumeSDP.addNoteAttachmentRequest(adjunto, request.getWorklog().getIdsdp(), responseWSDL.getIdsdp())) {
                    adjuntoError++;
                }
            }
            System.out.println("Se encontraron " + adjuntoError + " con error");
        }

        response.setWorklogResponse(responseWSDL);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "statusIncidentRequest")
    @ResponsePayload
    public StatusIncidentResponse StatusIncidentRequest(@RequestPayload StatusIncidentRequest request) {


        StatusIncidentResponse response = new StatusIncidentResponse();

        System.out.println(request.getIncidentUpdate().getIdsdp());
        System.out.println(request.getIncidentUpdate().getCliente());
        System.out.println(request.getIncidentUpdate().getImpacto());

        // Obtiene el incidente recibido por WSDL
        IncidentUpdate incidentUpdate = request.getIncidentUpdate();

        incidentUpdate.setImpacto(WSDLStatusIncidentRequest.mapeoImpacto(incidentUpdate.getImpacto()));
        incidentUpdate.setUrgencia(WSDLStatusIncidentRequest.mapeoUrgencia(incidentUpdate.getUrgencia()));
        incidentUpdate.setPrioridad(WSDLStatusIncidentRequest.mapeoPrioridad(incidentUpdate.getPrioridad()));
        incidentUpdate.setStatus(WSDLRequestIncidente.mapeoStatus(incidentUpdate.getStatus()));

        System.out.println("Actualizacion de Incidente Recibido");
        WSDLStatusIncidentRequest.imprimeIncidentUpdate(incidentUpdate);
        // Valida el incidente
        String resultadoTransaccion = WSDLStatusIncidentRequest.validaIncidentUpdate(incidentUpdate);
        StatusResponse statusResponse = new StatusResponse();
        statusResponse.setIdsdp("123");
        statusResponse.setMensajeTransaccion("Completada");
        statusResponse.setResultadoTransaccion("Exitoso");
        response.setStatusResponse(statusResponse);
        //Si la validacion mostro algun error
        if(!resultadoTransaccion.isEmpty()) {
            statusResponse.setIdsdp("NA");
            statusResponse.setMensajeTransaccion(resultadoTransaccion);
            statusResponse.setResultadoTransaccion("Error");
            response.setStatusResponse(statusResponse);
            return response;
        }
        //Si la validacion no mostro error
        else if (resultadoTransaccion.isEmpty()){
            statusResponse =consumeSDP.updateRequest(incidentUpdate);
            System.out.println("Consumo del servicio Update Request");
            statusResponse.setIdsdp(incidentUpdate.getIdsdp());

        }
        // Imprime el estado de la transacción
        System.out.println("Ejecucion de metodo PostIncidentResponse: " + incidentUpdate.getIdsdp());
        return response;
    }

    //Endpoint para creacion de incidentes
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postIncidentRequest")
    @ResponsePayload
    public PostIncidentResponse postIncidents(@RequestPayload PostIncidentRequest request) {
        PostIncidentResponse response = new PostIncidentResponse();
        // Obtiene el incidente recibido por WSDL
        Incident incidenteSAT = request.getIncident();
        incidenteSAT.setImpacto(WSDLRequestIncidente.mapeoImpacto(incidenteSAT.getImpacto()));
        incidenteSAT.setUrgencia(WSDLRequestIncidente.mapeoUrgencia(incidenteSAT.getUrgencia()));
        incidenteSAT.setPrioridad(WSDLRequestIncidente.mapeoPrioridad(incidenteSAT.getPrioridad()));


        System.out.println("Incidente Recibido");
        WSDLRequestIncidente.imprimeIncidente(incidenteSAT);
        //Valida el incidente
        String resultadoTransaccion = WSDLRequestIncidente.validaIncidente(incidenteSAT);
        IncidentResponse incidentSDP = new IncidentResponse();

        //Si la validacion mostro algun error
        if(!resultadoTransaccion.isEmpty()) {
            incidentSDP.setIdsdp("NA");
            incidentSDP.setMensajeTransaccion(resultadoTransaccion);
            incidentSDP.setResultadoTransaccion("Error");
        }

        if(!incidenteSAT.getToken().equals(configManager.getProperty("config.token"))){
            resultadoTransaccion = "Error token invalido";
            incidentSDP.setIdsdp("NA");
            incidentSDP.setMensajeTransaccion(resultadoTransaccion);
            incidentSDP.setResultadoTransaccion("Error");
        }

        //Si la validacion no mostro error
        else if (resultadoTransaccion.isEmpty()){
        incidentSDP =consumeSDP.creaRequest(incidenteSAT);

        }
        // Imprime el estado de la transacción
        System.out.println("Ejecucion de metodo PostIncidentResponse: " + incidentSDP.getIdsdp());

        // Construye la respuesta
        //PostIncidentResponse response = new PostIncidentResponse();
        response.setIncidentResponse(incidentSDP);

        return response;
    }
}
