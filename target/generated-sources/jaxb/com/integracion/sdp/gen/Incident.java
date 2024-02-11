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
 *         <element name="adjunto_nombre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="adjunto_data" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/>
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
    "idCategorizacion",
    "adjuntoNombre",
    "adjuntoData"
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
    @XmlElement(name = "adjunto_nombre")
    protected String adjuntoNombre;
    @XmlElement(name = "adjunto_data")
    protected byte[] adjuntoData;

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

    /**
     * Obtiene el valor de la propiedad adjuntoNombre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdjuntoNombre() {
        return adjuntoNombre;
    }

    /**
     * Define el valor de la propiedad adjuntoNombre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdjuntoNombre(String value) {
        this.adjuntoNombre = value;
    }

    /**
     * Obtiene el valor de la propiedad adjuntoData.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getAdjuntoData() {
        return adjuntoData;
    }

    /**
     * Define el valor de la propiedad adjuntoData.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setAdjuntoData(byte[] value) {
        this.adjuntoData = value;
    }

}
