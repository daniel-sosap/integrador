package com.integracion.sdp.endpoint;

import com.integracion.sdp.controller.ConsumeSDP;
import com.integracion.sdp.converter.IncidentConverter;
import com.integracion.sdp.gen.*;
import com.integracion.sdp.model.IncidentModel;
import com.integracion.sdp.repository.IncidentRepository;
import com.integracion.sdp.utils.ConsumeSDPAdjunto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.Base64;
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

    @Autowired
    private ConsumeSDPAdjunto consumeSDPAdjunto;

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

        Incident incidentFromRequest = request.getIncident();

        //Llamada a Service Desk plus para crear ticket
        String descripcion = incidentFromRequest.getDescripcion();
        System.out.println("Valor de Descripcion: " + descripcion);
        String incidenteServiceDeskplus = consumeSDP.sendRequest(descripcion);

        //Llamada a service desk plus para agregar adjunto

        // Obtener el nombre y los datos del archivo adjunto desde la solicitud
        String adjuntoNombre = incidentFromRequest.getAdjuntoNombre();
        byte[] adjuntoData = incidentFromRequest.getAdjuntoData();
        String rutaArchivo = null;
        System.out.println("Nombre del archivo adjunto " + adjuntoNombre);
        // Guardar el archivo adjunto en la carpeta temporal
        if (adjuntoData != null && adjuntoNombre != null) {
            try {
                 rutaArchivo = System.getProperty("java.io.tmpdir") + File.separator + adjuntoNombre;
                FileOutputStream fos = new FileOutputStream(rutaArchivo);
                fos.write(adjuntoData);
                fos.close();
                System.out.println("Archivo guardado en la carpeta temporal: " + rutaArchivo);
            } catch (IOException e) {
                System.err.println("Error al guardar el archivo en la carpeta temporal: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Llamar al método subirArchivo de ConsumeSDPAdjunto para manejar el archivo adjunto
        if (adjuntoData != null && adjuntoNombre != null) {
            System.out.println("Entro al if para mandar el archivo");
            consumeSDPAdjunto.subirArchivo(rutaArchivo, adjuntoNombre, incidenteServiceDeskplus);
        }

        IncidentModel incidentModel = incidentConverter.convertIncidentToIncidentModel(request.getIncident());
        Incident incident = incidentConverter.convertIncidentModelToIncident(incidentRepository.save(incidentModel));
        incident.setIdsdp(incidenteServiceDeskplus);
        System.out.println("incidenteServiceDeskplus = " + incidenteServiceDeskplus);
        System.out.println("Ejecucion de metodo PostIncidentResponse");
        response.setIncident(incident);
        return response;
    }
}
