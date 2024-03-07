//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v4.0.2 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
//


package com.integracion.sdp.gen;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>{@code
 * <complexType>
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="IncidentResponse" type="{http://AMINTUBSRVITSM1A/sdp/gen}IncidentResponse"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "incidentResponse"
})
@XmlRootElement(name = "postIncidentResponse")
public class PostIncidentResponse {

    @XmlElement(name = "IncidentResponse", required = true)
    protected IncidentResponse incidentResponse;

    /**
     * Obtiene el valor de la propiedad incidentResponse.
     * 
     * @return
     *     possible object is
     *     {@link IncidentResponse }
     *     
     */
    public IncidentResponse getIncidentResponse() {
        return incidentResponse;
    }

    /**
     * Define el valor de la propiedad incidentResponse.
     * 
     * @param value
     *     allowed object is
     *     {@link IncidentResponse }
     *     
     */
    public void setIncidentResponse(IncidentResponse value) {
        this.incidentResponse = value;
    }

}
