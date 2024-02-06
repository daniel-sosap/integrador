//
// Este archivo ha sido generado por Eclipse Implementation of JAXB v4.0.2 
// Visite https://eclipse-ee4j.github.io/jaxb-ri 
// Todas las modificaciones realizadas en este archivo se perderán si se vuelve a compilar el esquema de origen. 
//


package com.integracion.sdp.gen;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para Incident complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>{@code
 * <complexType name="Incident">
 *   <complexContent>
 *     <restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       <sequence>
 *         <element name="idsdp" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="id_cliente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="id_categorizacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       </sequence>
 *     </restriction>
 *   </complexContent>
 * </complexType>
 * }</pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Incident", propOrder = {
    "idsdp",
    "idCliente",
    "descripcion",
    "idCategorizacion"
})
public class Incident {

    @XmlElement(required = true)
    protected String idsdp;
    @XmlElement(name = "id_cliente", required = true)
    protected String idCliente;
    @XmlElement(required = true)
    protected String descripcion;
    @XmlElement(name = "id_categorizacion", required = true)
    protected String idCategorizacion;

    /**
     * Obtiene el valor de la propiedad idsdp.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdsdp() {
        return idsdp;
    }

    /**
     * Define el valor de la propiedad idsdp.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdsdp(String value) {
        this.idsdp = value;
    }

    /**
     * Obtiene el valor de la propiedad idCliente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCliente() {
        return idCliente;
    }

    /**
     * Define el valor de la propiedad idCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCliente(String value) {
        this.idCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad descripcion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Define el valor de la propiedad descripcion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescripcion(String value) {
        this.descripcion = value;
    }

    /**
     * Obtiene el valor de la propiedad idCategorizacion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdCategorizacion() {
        return idCategorizacion;
    }

    /**
     * Define el valor de la propiedad idCategorizacion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdCategorizacion(String value) {
        this.idCategorizacion = value;
    }

}
