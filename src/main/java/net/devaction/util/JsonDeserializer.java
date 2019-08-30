package net.devaction.util;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class JsonDeserializer{
    private static final Logger log = LoggerFactory.getLogger(JsonDeserializer.class);
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    public <T> T deserializeFromFile(String filename, Class<T> clazz) throws Exception{
        byte[] jsonBytes = null;        
        try {
            jsonBytes = readFromClasspath(filename);
        } catch (Exception ex) {
            log.error("Error when trying to read \"{}\" file from "
                    + "the classpath.", filename, ex);
            throw ex;
        }
        
        T objT = null;        
        try {
            objT = objectMapper.readValue(jsonBytes, clazz);
        } catch(IOException ex) {
            log.error("Error when trying to parse \"{}\" file content from "
                    + "the classpath.", filename, ex);
            throw ex;
        }
        
        return objT;
    }
    
    byte[] readFromClasspath(String filename) throws Exception{
        
        final URL url = this.getClass().getResource("/" + filename);
        if (url == null) {
            final String errorMessage = "Could not find file: " + filename;
            log.error(errorMessage);
            throw new IOException(errorMessage);
        }
        
        final URI uri = url.toURI();        
        Path path = Paths.get(uri);
        
        return Files.readAllBytes(path);
    }
}
