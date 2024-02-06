package com.integracion.sdp.endpoint;

import com.integracion.sdp.controller.ConsumeSDP;
import com.integracion.sdp.converter.IncidentConverter;
import com.integracion.sdp.gen.*;
import com.integracion.sdp.model.IncidentModel;
import com.integracion.sdp.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class IncidentEndpoint {

    private static final String NAMESPACE_URI = "http://integracion.com/sdp/gen";

    @Autowired
    private IncidentRepository incidentRepository;

    @Autowired
    private IncidentConverter incidentConverter;

    @Autowired
    private ConsumeSDP consumeSDP;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getIncidentRequest")
    @ResponsePayload
    public GetIncidentResponse getIncident(@RequestPayload GetIncidentRequest request) {
        GetIncidentResponse response = new GetIncidentResponse();
        IncidentModel incidentModel = incidentRepository.findByIdsdp(request.getName());
        response.setIncident(incidentConverter.convertIncidentModelToIncident(incidentModel));
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getIncidentsRequest")
    @ResponsePayload
    public GetIncidentsResponse getIncidents(@RequestPayload GetIncidentsRequest request) {
        GetIncidentsResponse response = new GetIncidentsResponse();
        List<IncidentModel> incidentModels = incidentRepository.findAll();
        List<Incident> incidents = incidentConverter.convertIncidentModelsToIncidents(incidentModels);
        response.getIncidents().addAll(incidents);
        return response;
    }
    
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "postIncidentRequest")
    @ResponsePayload
    public PostIncidentResponse postIncidents(@RequestPayload PostIncidentRequest request) {
        PostIncidentResponse response = new PostIncidentResponse();
        // Obtener el objeto Incident de la solicitud
        Incident incidentFromRequest = request.getIncident();
        // Obtener el valor del campo descripcion
        String descripcion = incidentFromRequest.getDescripcion();
        System.out.println("Valor de Descripcion: " + descripcion);
        String incidenteServiceDeskplus = consumeSDP.sendRequest(descripcion);
        IncidentModel incidentModel = incidentConverter.convertIncidentToIncidentModel(request.getIncident());
        Incident incident = incidentConverter.convertIncidentModelToIncident(incidentRepository.save(incidentModel));
        incident.setIdsdp(incidenteServiceDeskplus);
        System.out.println("incidenteServiceDeskplus = " + incidenteServiceDeskplus);
        System.out.println("Ejecucion de metodo PostIncidentResponse");
        response.setIncident(incident);
        return response;
    }
}
