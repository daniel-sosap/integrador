package com.integracion.sdp.converter;

import com.integracion.sdp.gen.Incident;
import com.integracion.sdp.model.IncidentModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class IncidentConverter {

    public IncidentModel convertIncidentToIncidentModel(Incident incident) {
        IncidentModel incidentModel = new IncidentModel();
        incidentModel.setId(incident.getId());
        incidentModel.setName(incident.getName());
        incidentModel.setPrice(incident.getPrice());
        incidentModel.setDescription(incident.getDescription());
        return incidentModel;
    }

    public Incident convertIncidentModelToIncident(IncidentModel incidentModel) {
        Incident incident = new Incident();
        incident.setId(incidentModel.getId());
        incident.setName(incidentModel.getName());
        incident.setPrice(incidentModel.getPrice());
        incident.setDescription(incidentModel.getDescription());
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
