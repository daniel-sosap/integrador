package com.integracion.sdp.utils;

import com.integracion.sdp.gen.WorklogIncidentRequest;
import com.integracion.sdp.gen.WorklogResponse;
import com.integracion.sdp.gen.WorklogWORequest;
import com.integracion.sdp.dto.AdjuntoInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class WSDLWorklogWORequest {

    private static final Logger logger = LoggerFactory.getLogger(WSDLWorklogWORequest.class);

    public void imprimeWOWorklog(WorklogWORequest woWorklog){

        logger.info("***********Valores recibidos************\n" +
                "\nIdsdp : " + woWorklog.getWOworklog().getIdsdp() +
                "\ncliente : " + woWorklog.getWOworklog().getCliente()+
                "\nnotas : " + woWorklog.getWOworklog().getNotas() +
                "\ntoken : " + woWorklog.getWOworklog().getToken());
    }


public List<AdjuntoInfo> validaAdjuntos(WorklogWORequest request) {
        List<AdjuntoInfo> adjuntosGuardados = new ArrayList<>();
        int contadorAdjuntos = 0;

        // Adjunto 1
        String adjuntoNombre1 = request.getWOworklog().getAdjunto1Nombre();
        byte[] adjuntoData1 = request.getWOworklog().getAdjunto1Data();
        if (adjuntoNombre1 != null && !adjuntoNombre1.isEmpty() && adjuntoData1 != null) {
            guardarAdjunto(adjuntoNombre1, adjuntoData1, adjuntosGuardados);
            contadorAdjuntos++;
        }

        // Adjunto 2
        String adjuntoNombre2 = request.getWOworklog().getAdjunto2Nombre();
        byte[] adjuntoData2 = request.getWOworklog().getAdjunto2Data();
        if (adjuntoNombre2 != null && !adjuntoNombre2.isEmpty() && adjuntoData2 != null) {
            guardarAdjunto(adjuntoNombre2, adjuntoData2, adjuntosGuardados);
            contadorAdjuntos++;
        }

        // Adjunto 3
        String adjuntoNombre3 = request.getWOworklog().getAdjunto3Nombre();
        byte[] adjuntoData3 = request.getWOworklog().getAdjunto3Data();
        if (adjuntoNombre3 != null && !adjuntoNombre3.isEmpty() && adjuntoData3 != null) {
            guardarAdjunto(adjuntoNombre3, adjuntoData3, adjuntosGuardados);
            contadorAdjuntos++;
        }

        System.out.println("Total de archivos adjuntos guardados Bitacora Tarea: " + contadorAdjuntos);
        return adjuntosGuardados;
    }

    private void guardarAdjunto(String nombre, byte[] data, List<AdjuntoInfo> adjuntosGuardados) {
        String rutaArchivo = System.getProperty("java.io.tmpdir") + File.separator + nombre;
        try (FileOutputStream fos = new FileOutputStream(rutaArchivo)) {
            fos.write(data);
            System.out.println("Archivo guardado en la carpeta temporal: " + rutaArchivo);
            adjuntosGuardados.add(new AdjuntoInfo(nombre, rutaArchivo));
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo en la carpeta temporal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public WorklogResponse validaWorklogIncident(WorklogIncidentRequest request){
        WorklogResponse responseWSDL = new WorklogResponse();


        if(request.getWorklog().getNotas().trim().isEmpty() || request.getWorklog().getNotas()==null){
            System.out.println("Notas vacias");
            responseWSDL.setIdsdp("NA");
            responseWSDL.setMensajeTransaccion("Debe de capturar una nota");
            responseWSDL.setResultadoTransaccion("Error");
        }


        return responseWSDL;
    }
    public String mapeoImpacto(String impacto){
        String impactoMapeo = null;
        switch (impacto) {
            case "1000":
                impactoMapeo = "1-Extenso/Generalizado";
                break;
            case "2000":
                impactoMapeo = "2-Significativo/Amplio";
                break;
            case "3000":
                impactoMapeo = "3-Moderado/Limitado";
                break;
            case "4000":
                impactoMapeo = "4-Menor/Localizado";
                break;
            default:
                impactoMapeo = impacto;
                break;
        }
        return impactoMapeo;
    }

    public String mapeoUrgencia(String urgencia){
        String urgenciaMapeo = null;
        switch (urgencia) {
            case "1000":
                urgenciaMapeo = "1-Critica";
                break;
            case "2000":
                urgenciaMapeo = "2-Alta";
                break;
            case "3000":
                urgenciaMapeo = "3-Medio";
                break;
            case "4000":
                urgenciaMapeo = "4-Baja";
                break;
            default:
                urgenciaMapeo = urgencia;
                break;
        }
        return urgenciaMapeo;
    }

    public String mapeoPrioridad(String prioridad){
        String prioridadMapeo = null;
        switch (prioridad) {
            case "0":
                prioridadMapeo = "Critica";
                break;
            case "1":
                prioridadMapeo = "Alta";
                break;
            case "2":
                prioridadMapeo = "Media";
                break;
            case "3":
                prioridadMapeo = "Baja";
                break;
            default:
                prioridadMapeo = prioridad;
                break;
        }
        return prioridadMapeo;
    }

}



