package com.integracion.sdp.repository;

import com.integracion.sdp.model.IncidentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends JpaRepository<IncidentModel, Integer> {
    IncidentModel findByIdsdp(String idsdp);
}