package com.integracion.sdp.converter;

import com.integracion.sdp.gen.Incident;
import com.integracion.sdp.model.IncidentModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class IncidentConverter {

    public IncidentModel convertIncidentToIncidentModel(Incident incident) {
        IncidentModel incidentModel = new IncidentModel();
        incidentModel.setIdsdp(incident.getIdsdp());
        incidentModel.setId_cliente(incident.getIdCliente());
        incidentModel.setDescripcion(incident.getDescripcion());
        incidentModel.setId_categorizacion(incident.getIdCategorizacion());
        //incidentModel.setArchivo1(Base64.getDecoder().decode(incident.getArchivo1()));
        return incidentModel;
    }

    public Incident convertIncidentModelToIncident(IncidentModel incidentModel) {
        Incident incident = new Incident();
        incident.setIdsdp(incidentModel.getIdsdp());
        incident.setIdCliente(incidentModel.getId_cliente());
        incident.setDescripcion(incidentModel.getDescripcion());
        incident.setIdCategorizacion(incidentModel.getId_categorizacion());
        //incident.setArchivo1(Base64.getEncoder().encodeToString(incidentModel.getArchivo1()).getBytes());
        return incident;
    }

    public List<IncidentModel> convertIncidentsToIncidentModels(List<Incident> incidents) {
        List<IncidentModel> incidentModels = new ArrayList<IncidentModel>();
        for (Incident incident : incidents) {
            incidentModels.add(convertIncidentToIncidentModel(incident));
        }
        return incidentModels;
    }

    public List<Incident> convertIncidentModelsToIncidents(List<IncidentModel> incidentModels) {
        List<Incident> incidents = new ArrayList<Incident>();
        for (IncidentModel incidentModel : incidentModels) {
            incidents.add(convertIncidentModelToIncident(incidentModel));
        }
        return incidents;
    }
}
