package com.integracion.sdp.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Component
public class PropertiesReader {

    private final ResourceLoader resourceLoader;
    private final String propertiesFilename;

    public PropertiesReader(ResourceLoader resourceLoader, @Value("${custom.properties.filename}") String propertiesFilename) {
        this.resourceLoader = resourceLoader;
        this.propertiesFilename = propertiesFilename;
    }

    public Properties loadProperties() throws IOException {
        InputStream inputStream = resourceLoader.getResource("file:./" + propertiesFilename).getInputStream();
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}



