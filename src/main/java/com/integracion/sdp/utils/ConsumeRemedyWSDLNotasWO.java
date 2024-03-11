package com.integracion.sdp.utils;

import com.integracion.sdp.config.ConfigurationManager;
import jakarta.xml.soap.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ConsumeRemedyWSDLNotasWO {
    static ConfigurationManager configManager = ConfigurationManager.getInstance();

    static String soapEndpointUrl = "http://" + configManager.getProperty("config.remedy.server.midtier") + ":" + configManager.getProperty("config.remedy.puerto") + "/arsys/services/ARService?server=" + configManager.getProperty("config.remedy.server.ars") + "&webService=GestionDeOrdenes";

    static String soapAction = "urn:GestionDeOrdenes/Crea_Bitacora";
    static String username = configManager.getProperty("config.remedy.user");
    static String password = configManager.getProperty("config.remedy.pass");

    public static SOAPMessage callWebService(String soapEndpointUrl, String soapAction, String username, String password, SOAPMessage soapMessage) {
        try {
            // Creating SOAP connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Sending request
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, username, password, soapMessage), soapEndpointUrl);

            // Close connection
            soapConnection.close();

            return soapResponse;
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
            return null;
        }
    }

    private static SOAPMessage createSOAPRequest(String soapAction, String username, String password, SOAPMessage soapMessage) throws Exception {
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println("\n");
        return soapMessage;
    }

    public static SOAPMessage createSoapEnvelopeCreaBitacora(String username, String password, String tipoOperacion, String nombreProveedor, String idTicketInterno, String notas, String adjunto1Nombre, String adjunto1Data, String adjunto2Nombre, String adjunto2Data, String adjunto3Nombre, String adjunto3Data) throws SOAPException, IOException {
        SOAPMessage soapMessage = createSOAPMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String myNamespaceURI = "urn:GestionDeOrdenes";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("urn", myNamespaceURI);

        // SOAP Header
        SOAPHeader soapHeader = envelope.getHeader();
        if (soapHeader == null)
            soapHeader = envelope.addHeader();
        SOAPElement authInfoElem = soapHeader.addChildElement("AuthenticationInfo", "urn");
        SOAPElement userNameElem = authInfoElem.addChildElement("userName", "urn");
        userNameElem.addTextNode(username);
        SOAPElement passwordElem = authInfoElem.addChildElement("password", "urn");
        passwordElem.addTextNode(password);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement creaBitacoraElem = soapBody.addChildElement("Crea_Bitacora", "urn");
        creaBitacoraElem.addChildElement("TipoDeOperacion", "urn").addTextNode(tipoOperacion);
        creaBitacoraElem.addChildElement("NombreProveedor", "urn").addTextNode(nombreProveedor);
        creaBitacoraElem.addChildElement("IDTicketInterno", "urn").addTextNode(idTicketInterno);
        creaBitacoraElem.addChildElement("Notas", "urn").addTextNode(notas);

        if (adjunto1Nombre != null && !adjunto1Nombre.isEmpty()) {
            creaBitacoraElem.addChildElement("Adjunto01_attachmentName", "urn").addTextNode(adjunto1Nombre);
            creaBitacoraElem.addChildElement("Adjunto01_attachmentData", "urn").addTextNode(adjunto1Data);
        }
        if (adjunto2Nombre != null && !adjunto2Nombre.isEmpty()) {
            creaBitacoraElem.addChildElement("Adjunto02_attachmentName", "urn").addTextNode(adjunto2Nombre);
            creaBitacoraElem.addChildElement("Adjunto02_attachmentData", "urn").addTextNode(adjunto2Data);
        }
        if (adjunto3Nombre != null && !adjunto3Nombre.isEmpty()) {
            creaBitacoraElem.addChildElement("Adjunto03_attachmentName", "urn").addTextNode(adjunto3Nombre);
            creaBitacoraElem.addChildElement("Adjunto03_attachmentData", "urn").addTextNode(adjunto3Data);
        }

        return soapMessage;
    }

    private static SOAPMessage createSOAPMessage() throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        return messageFactory.createMessage();
    }

    public static void CreaBitacoraRemedy(String tipoOperacion, String nombreProveedor, String idTicketInterno, String notas, String adjunto1Nombre, String adjunto1Data, String adjunto2Nombre, String adjunto2Data, String adjunto3Nombre, String adjunto3Data) {
        try {
            SOAPMessage soapMessage = createSoapEnvelopeCreaBitacora(username, password, tipoOperacion, nombreProveedor, idTicketInterno, notas, adjunto1Nombre, adjunto1Data, adjunto2Nombre, adjunto2Data, adjunto3Nombre, adjunto3Data);
            SOAPMessage soapResponse = callWebService(soapEndpointUrl, soapAction, username, password, soapMessage);

            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);

            System.out.println();
        } catch (SOAPException | IOException e) {
            e.printStackTrace();
        }
    }
}
