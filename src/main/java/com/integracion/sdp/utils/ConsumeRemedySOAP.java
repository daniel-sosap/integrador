package com.integracion.sdp.utils;

import jakarta.xml.soap.*;

public class ConsumeRemedySOAP {

    public static void main(String args[]) {
        String soapEndpointUrl = "http://192.168.1.81:9090/arsys/services/ARService?server=arsdev&webService=test_webservices";
        String soapAction = "urn:test_webservices/actualizaINC";
        String username = "Demo";
        String password = "123";
        callSoapWebService(soapEndpointUrl, soapAction, username, password);
    }

    private static void createSoapEnvelope(SOAPMessage soapMessage, String username, String password) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();
        String myNamespace = "urn";
        String myNamespaceURI = "urn:test_webservices";
        // SOAP Envelope
        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);

        // SOAP Header
        SOAPHeader soapHeader = envelope.getHeader();
        if (soapHeader == null)
            soapHeader = envelope.addHeader();
        SOAPElement authInfoElem = soapHeader.addChildElement("AuthenticationInfo", myNamespace);
        SOAPElement userNameElem = authInfoElem.addChildElement("userName", myNamespace);
        userNameElem.addTextNode(username);
        SOAPElement passwordElem = authInfoElem.addChildElement("password", myNamespace);
        passwordElem.addTextNode(password);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement soapBodyElem = soapBody.addChildElement("actualizaINC", myNamespace);

        // Colocando Parametros request parameters
        soapBodyElem.addChildElement("idsdp", myNamespace).addTextNode("idsdp123");
        soapBodyElem.addChildElement("descripcion__c", myNamespace).addTextNode("Actualizacion desde cliente soap desde java");
        soapBodyElem.addChildElement("id_categorización__c", myNamespace).addTextNode("");
        soapBodyElem.addChildElement("Adjunto_1__c_attachmentName", myNamespace).addTextNode("");
        soapBodyElem.addChildElement("Adjunto_1__c_attachmentData", myNamespace).addTextNode("");
        soapBodyElem.addChildElement("Adjunto_1__c_attachmentOrigSize", myNamespace).addTextNode("");
        soapBodyElem.addChildElement("Operacion__c", myNamespace).addTextNode("actualizacion");
        soapBodyElem.addChildElement("EstatusSDP__c", myNamespace).addTextNode("Assigned");
    }

    private static void callSoapWebService(String soapEndpointUrl, String soapAction, String username, String password) {
        try {
            // Creando conexion a  SOAP
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Envio de la peticion
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
