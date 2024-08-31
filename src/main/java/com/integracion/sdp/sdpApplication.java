package com.integracion.sdp;

import com.integracion.sdp.client.ConsumeSDP;
import com.integracion.sdp.config.ConfigurationManager;
import com.integracion.sdp.config.PropertiesReader;
import jakarta.xml.soap.SOAPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


@SpringBootApplication
@EnableScheduling
public class sdpApplication {
	static ConfigurationManager configManager = ConfigurationManager.getInstance();

	@Autowired
	private Environment environment;

	@Autowired
	private ResourceLoader resourceLoader;

	@Bean
	public Properties integradorProperties() throws IOException {
		String propertiesFilename = environment.getProperty("custom.properties.filename");
		PropertiesReader propertiesReader = new PropertiesReader(resourceLoader, propertiesFilename);
		return propertiesReader.loadProperties();
	}

	public static void main(String[] args) throws SOAPException {
		SpringApplication.run(sdpApplication.class, args);

		System.out.println("version 140324_1200");
		//ConsumeSDP consumeSDP = new ConsumeSDP();

		String env = configManager.getProperty("config.ambiente");
		System.out.println("Ambiente detectado: " + env);


	}



}
