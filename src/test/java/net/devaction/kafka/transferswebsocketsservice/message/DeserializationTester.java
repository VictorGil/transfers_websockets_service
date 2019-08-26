package net.devaction.kafka.transferswebsocketsservice.message;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class DeserializationTester{
    private static final Logger log = LoggerFactory.getLogger(
            DeserializationTester.class);
    
    private ObjectMapper mapper = new ObjectMapper();
    
    public static void main(String[] args) {
        new DeserializationTester().run();
    }
    
    private void run() {
        String json = "{\"type\":\"BALANCE_REQUEST\",\"payload\":\"whatever\"}";
        log.info("JSON string to be deserialized: {}", json);
        MessageWrapper messageWrapper = null;
        
        try{
            messageWrapper = mapper.readValue(json, MessageWrapper.class);
        } catch (IOException ex){
            log.error("{}", ex, ex);;
        }
        
        log.info("Deserialized object: {}", messageWrapper);
    }
}
