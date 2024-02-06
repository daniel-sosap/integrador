package com.integracion.sdp.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/remedy")
public class RemedyController {

    /*
    @PostMapping("/actualizaincidente")
    public ResponseEntity<String> actualizaIncidente(@RequestBody IncidenteRequest incidenteRequest) {
        // Aquí puedes realizar la lógica para actualizar el incidente con los datos recibidos

        // Supongamos que la actualización fue exitosa
        return new ResponseEntity<>("Incidente actualizado correctamente", HttpStatus.OK);
    }

     */

    @PostMapping("/actualizaincidente/{remedyid}/{status}")
    public ResponseEntity<String> actualizaIncidente(@PathVariable String remedyid, @PathVariable String status) {
        // Aquí puedes realizar la lógica para actualizar el incidente con los datos recibidos

        System.out.println("Se recibio el ticket " + remedyid);
        System.out.println("Se recbibio el status " + status);
        // Supongamos que la actualización fue exitosa
        return new ResponseEntity<>("Incidente actualizado correctamente", HttpStatus.OK);
    }

}
