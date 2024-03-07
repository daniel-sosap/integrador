package com.integracion.sdp.utils;

import jakarta.xml.soap.*;

import java.io.IOException;

public class ConsumeRemedyWSDLBitacoraINC {

    private static final String SOAP_ENDPOINT_URL = "http://192.168.1.81:9090/arsys/services/ARService?server=servidor_ars&webService=GestionDeIncidentes";
    private static final String SOAP_ACTION = "urn:GestionDeIncidentes/Crea_Bitacora";
    private static final String USERNAME = "Demo";
    private static final String PASSWORD = "123";

    public static void callRemedyService(String notas, String rutaArchivo1, String rutaArchivo2, String rutaArchivo3) {
        try {
            SOAPMessage soapMessage = createSoapEnvelope(notas, rutaArchivo1, rutaArchivo2, rutaArchivo3);
            SOAPMessage soapResponse = callWebService(SOAP_ENDPOINT_URL, SOAP_ACTION, USERNAME, PASSWORD, soapMessage);

            // Manejar la respuesta si es necesario
        } catch (SOAPException | IOException e) {
            e.printStackTrace();
        }
    }

    private static SOAPMessage createSoapEnvelope(String notas, String rutaArchivo1, String rutaArchivo2, String rutaArchivo3) throws SOAPException {
        SOAPMessage soapMessage = createSOAPMessage();
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String myNamespaceURI = "urn:GestionDeIncidentes";

        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration("urn", myNamespaceURI);

        // SOAP Header
        SOAPHeader soapHeader = envelope.getHeader();
        if (soapHeader == null)
            soapHeader = envelope.addHeader();
        SOAPElement authInfoElem = soapHeader.addChildElement("AuthenticationInfo", "urn");
        SOAPElement userNameElem = authInfoElem.addChildElement("userName", "urn");
        userNameElem.addTextNode(USERNAME);
        SOAPElement passwordElem = authInfoElem.addChildElement("password", "urn");
        passwordElem.addTextNode(PASSWORD);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement creaBitacoraElem = soapBody.addChildElement("Crea_Bitacora", "urn");
        creaBitacoraElem.addChildElement("TipoDeOperacion", "urn").addTextNode("Bitacora");
        creaBitacoraElem.addChildElement("NombreProveedor", "urn").addTextNode("SAT");
        creaBitacoraElem.addChildElement("IDTicketInterno", "urn").addTextNode("INC00001");
        creaBitacoraElem.addChildElement("Notas", "urn").addTextNode(notas);

        // Adjuntos
        if (rutaArchivo1 != null) {
            creaBitacoraElem.addChildElement("Adjunto_01_Dato", "urn").addTextNode(rutaArchivo1);
        }
        if (rutaArchivo2 != null) {
            creaBitacoraElem.addChildElement("Adjunto_02_Dato", "urn").addTextNode(rutaArchivo2);
        }
        if (rutaArchivo3 != null) {
            creaBitacoraElem.addChildElement("Adjunto_03_Dato", "urn").addTextNode(rutaArchivo3);
        }

        return soapMessage;
    }

    private static SOAPMessage createSOAPMessage() throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        return messageFactory.createMessage();
    }

    private static SOAPMessage callWebService(String soapEndpointUrl, String soapAction, String username, String password, SOAPMessage soapMessage) throws SOAPException, IOException {
        SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
        SOAPConnection soapConnection = soapConnectionFactory.createConnection();

        // Sending request
        SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, username, password, soapMessage), soapEndpointUrl);
        System.out.println("Response SOAP Message:" + soapResponse);

        // Close connection
        soapConnection.close();

        return soapResponse;
    }

    private static SOAPMessage createSOAPRequest(String soapAction, String username, String password, SOAPMessage soapMessage) throws SOAPException {
        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        return soapMessage;
    }
}
