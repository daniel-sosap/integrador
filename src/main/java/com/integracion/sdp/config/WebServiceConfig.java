package com.integracion.sdp.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    
    @Bean(name = "incidents")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema incidentsSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("incidentPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://AMINTUBSRVITSM1A/sdp/gen");
        wsdl11Definition.setSchema(incidentsSchema);
        return wsdl11Definition;
    }
    
    @Bean
    public XsdSchema incidentsSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/incidents.xsd"));
    }

    @Bean(name = "workorders")
    public DefaultWsdl11Definition workordersWsdl11Definition(XsdSchema workordersSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("workorderPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://AMINTUBSRVITSM1A/sdp/gen");
        wsdl11Definition.setSchema(workordersSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema workordersSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/workorders.xsd"));
    }


}
