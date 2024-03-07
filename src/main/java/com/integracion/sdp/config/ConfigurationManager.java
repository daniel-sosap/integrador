package com.integracion.sdp.config;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigurationManager {
    private static ConfigurationManager instance;
    private Properties properties;

    // Constructor privado para evitar la creación de instancias directamente
    private ConfigurationManager() {
        properties = new Properties();
        try {
            // Cargar las propiedades desde el archivo config.properties
            properties.load(new FileInputStream("config.properties"));
            System.out.println("Carga de propiedades con exito");
        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de la excepción adecuado
        }
    }

    // Método estático para obtener la única instancia de la clase
    public static ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }

    // Método para obtener el valor de una propiedad específica
    public String getProperty(String key) {
        return properties.getProperty(key);
    }


    // Otros métodos para acceder a otras propiedades si es necesario
}
