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
 *         <element name="id_cliente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="cliente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="descripcion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="id_categorizacion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="resumen" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="impacto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="urgencia" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="prioridad" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="id_agente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="rfc_corto_cliente" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="rfc_corto_contacto" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="direccion" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="id_activo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="id_ticket_padre" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         <element name="fecha_envio" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         <element name="token" type="{http://www.w3.org/2001/XMLSchema}string"/>
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
    "idCliente",
    "cliente",
    "descripcion",
    "idCategorizacion",
    "resumen",
    "impacto",
    "urgencia",
    "prioridad",
    "idAgente",
    "rfcCortoCliente",
    "rfcCortoContacto",
    "direccion",
    "idActivo",
    "idTicketPadre",
    "fechaEnvio",
    "token"
})
public class Incident {

    @XmlElement(name = "id_cliente", required = true)
    protected String idCliente;
    @XmlElement(required = true)
    protected String cliente;
    @XmlElement(required = true)
    protected String descripcion;
    @XmlElement(name = "id_categorizacion", required = true)
    protected String idCategorizacion;
    @XmlElement(required = true)
    protected String resumen;
    @XmlElement(required = true)
    protected String impacto;
    @XmlElement(required = true)
    protected String urgencia;
    @XmlElement(required = true)
    protected String prioridad;
    @XmlElement(name = "id_agente", required = true)
    protected String idAgente;
    @XmlElement(name = "rfc_corto_cliente", required = true)
    protected String rfcCortoCliente;
    @XmlElement(name = "rfc_corto_contacto", required = true)
    protected String rfcCortoContacto;
    @XmlElement(required = true)
    protected String direccion;
    @XmlElement(name = "id_activo")
    protected String idActivo;
    @XmlElement(name = "id_ticket_padre")
    protected String idTicketPadre;
    @XmlElement(name = "fecha_envio", required = true)
    protected String fechaEnvio;
    @XmlElement(required = true)
    protected String token;

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
     * Obtiene el valor de la propiedad cliente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCliente() {
        return cliente;
    }

    /**
     * Define el valor de la propiedad cliente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCliente(String value) {
        this.cliente = value;
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
     * Obtiene el valor de la propiedad resumen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResumen() {
        return resumen;
    }

    /**
     * Define el valor de la propiedad resumen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResumen(String value) {
        this.resumen = value;
    }

    /**
     * Obtiene el valor de la propiedad impacto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImpacto() {
        return impacto;
    }

    /**
     * Define el valor de la propiedad impacto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImpacto(String value) {
        this.impacto = value;
    }

    /**
     * Obtiene el valor de la propiedad urgencia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrgencia() {
        return urgencia;
    }

    /**
     * Define el valor de la propiedad urgencia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrgencia(String value) {
        this.urgencia = value;
    }

    /**
     * Obtiene el valor de la propiedad prioridad.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrioridad() {
        return prioridad;
    }

    /**
     * Define el valor de la propiedad prioridad.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrioridad(String value) {
        this.prioridad = value;
    }

    /**
     * Obtiene el valor de la propiedad idAgente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdAgente() {
        return idAgente;
    }

    /**
     * Define el valor de la propiedad idAgente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdAgente(String value) {
        this.idAgente = value;
    }

    /**
     * Obtiene el valor de la propiedad rfcCortoCliente.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcCortoCliente() {
        return rfcCortoCliente;
    }

    /**
     * Define el valor de la propiedad rfcCortoCliente.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcCortoCliente(String value) {
        this.rfcCortoCliente = value;
    }

    /**
     * Obtiene el valor de la propiedad rfcCortoContacto.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRfcCortoContacto() {
        return rfcCortoContacto;
    }

    /**
     * Define el valor de la propiedad rfcCortoContacto.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRfcCortoContacto(String value) {
        this.rfcCortoContacto = value;
    }

    /**
     * Obtiene el valor de la propiedad direccion.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDireccion() {
        return direccion;
    }

    /**
     * Define el valor de la propiedad direccion.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDireccion(String value) {
        this.direccion = value;
    }

    /**
     * Obtiene el valor de la propiedad idActivo.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdActivo() {
        return idActivo;
    }

    /**
     * Define el valor de la propiedad idActivo.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdActivo(String value) {
        this.idActivo = value;
    }

    /**
     * Obtiene el valor de la propiedad idTicketPadre.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdTicketPadre() {
        return idTicketPadre;
    }

    /**
     * Define el valor de la propiedad idTicketPadre.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdTicketPadre(String value) {
        this.idTicketPadre = value;
    }

    /**
     * Obtiene el valor de la propiedad fechaEnvio.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEnvio() {
        return fechaEnvio;
    }

    /**
     * Define el valor de la propiedad fechaEnvio.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEnvio(String value) {
        this.fechaEnvio = value;
    }

    /**
     * Obtiene el valor de la propiedad token.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getToken() {
        return token;
    }

    /**
     * Define el valor de la propiedad token.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setToken(String value) {
        this.token = value;
    }

}
