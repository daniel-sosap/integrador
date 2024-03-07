package com.integracion.sdp.utils;

import com.integracion.sdp.config.ConfigurationManager;
import jakarta.xml.soap.*;
import org.springframework.http.converter.json.GsonBuilderUtils;

import java.io.IOException;

public class ConsumeRemedyWSDLEstadoINC {
    static ConfigurationManager configManager = ConfigurationManager.getInstance();

    static  String soapEndpointUrl = "http://"+configManager.getProperty("config.remedy.server.midtier")+":"+configManager.getProperty("config.remedy.puerto")+ "/arsys/services/ARService?server="+configManager.getProperty("config.remedy.server.ars")+"&webService=GestionDeIncidentes";
    static  String soapAction = "urn:GestionDeIncidentes/Actualiza_Incidentes";
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

    public static SOAPMessage createSoapEnvelopeUpdateINC(String username, String password, String tipoOperacion, String nombreProveedor, String idTicketInterno, String estadoNuevo, String motivoEstado, String resolucion) throws SOAPException {
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
        userNameElem.addTextNode(username);
        SOAPElement passwordElem = authInfoElem.addChildElement("password", "urn");
        passwordElem.addTextNode(password);

        // SOAP Body
        SOAPBody soapBody = envelope.getBody();
        SOAPElement actualizaIncidentesElem = soapBody.addChildElement("Actualiza_Incidentes", "urn");
        actualizaIncidentesElem.addChildElement("TipoDeOperacion", "urn").addTextNode(tipoOperacion);
        actualizaIncidentesElem.addChildElement("NombreProveedor", "urn").addTextNode(nombreProveedor);
        actualizaIncidentesElem.addChildElement("IDTicketInterno", "urn").addTextNode(idTicketInterno);
        actualizaIncidentesElem.addChildElement("EstadoNuevo", "urn").addTextNode(estadoNuevo);
        actualizaIncidentesElem.addChildElement("MotivoEstado", "urn").addTextNode(motivoEstado);
        actualizaIncidentesElem.addChildElement("Resolucion", "urn").addTextNode(resolucion);

        return soapMessage;
    }

    private static SOAPMessage createSOAPMessage() throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        return messageFactory.createMessage();
    }

    public static void ActualizaIncidenteRemedy(String tipoOperacion, String  nombreProveedor, String idTicketInterno, String estadoNuevo, String motivoEstado, String  resolucion) {



        try {
            SOAPMessage soapMessage = createSoapEnvelopeUpdateINC(username, password, tipoOperacion, nombreProveedor, idTicketInterno, estadoNuevo, motivoEstado, resolucion);
            SOAPMessage soapResponse = callWebService(soapEndpointUrl, soapAction, username, password, soapMessage);

            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            soapResponse.writeTo(System.out);

            System.out.println();
        } catch (SOAPException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
