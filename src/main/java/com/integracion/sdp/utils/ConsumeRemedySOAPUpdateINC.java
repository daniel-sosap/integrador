package com.integracion.sdp.utils;

import jakarta.xml.soap.*;

public class ConsumeRemedySOAPUpdateINC {

    public static void main(String args[]) {
        String soapEndpointUrl = "http://192.168.1.81:9090/arsys/services/ARService?server=arsdev&webService=GestionDeIncidentes";
        String soapAction = "urn:GestionDeIncidentes/Actualiza_Incidentes";
        String username = "Demo";
        String password = "123";
        callSoapWebService(soapEndpointUrl, soapAction, username, password);
    }

    private static void createSoapEnvelope(SOAPMessage soapMessage, String username, String password) throws SOAPException {
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
        userNameElem.addTextNode(username);
        SOAPElement passwordElem = authInfoElem.addChildElement("password", "urn");
        passwordElem.addTextNode(password);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement actualizaIncidentesElem = soapBody.addChildElement("Actualiza_Incidentes", "urn");
        actualizaIncidentesElem.addChildElement("TipoDeOperacion", "urn").addTextNode("A_INC");
        actualizaIncidentesElem.addChildElement("NombreProveedor", "urn").addTextNode("MESA_SERVICIO_SENHA2");
        actualizaIncidentesElem.addChildElement("IDTicketInterno", "urn").addTextNode("INC0001INTLJ");
        actualizaIncidentesElem.addChildElement("EstadoNuevo", "urn").addTextNode("2");
        actualizaIncidentesElem.addChildElement("MotivoEstado", "urn").addTextNode("");
        actualizaIncidentesElem.addChildElement("Resolucion", "urn").addTextNode("Acttualizacion desde Intellij");
    }

    private static void callSoapWebService(String soapEndpointUrl, String soapAction, String username, String password) {
        try {
            // Creating SOAP connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Sending request
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction, username, password), soapEndpointUrl);

            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);
            System.out.println();
            soapConnection.close();
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
    }

    private static SOAPMessage createSOAPRequest(String soapAction, String username, String password) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();
        createSoapEnvelope(soapMessage, username, password);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);

        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println("\n");
        return soapMessage;
    }
}
