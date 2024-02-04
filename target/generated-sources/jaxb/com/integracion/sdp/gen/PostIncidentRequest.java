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
 *         <element name="Incident" type="{http://integracion.com/sdp/gen}Incident"/>
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
    "incident"
})
@XmlRootElement(name = "postIncidentRequest")
public class PostIncidentRequest {

    @XmlElement(name = "Incident", required = true)
    protected Incident incident;

    /**
     * Obtiene el valor de la propiedad incident.
     * 
     * @return
     *     possible object is
     *     {@link Incident }
     *     
     */
    public Incident getIncident() {
        return incident;
    }

    /**
     * Define el valor de la propiedad incident.
     * 
     * @param value
     *     allowed object is
     *     {@link Incident }
     *     
     */
    public void setIncident(Incident value) {
        this.incident = value;
    }

}
