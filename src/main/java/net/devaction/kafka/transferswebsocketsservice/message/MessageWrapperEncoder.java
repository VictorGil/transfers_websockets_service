package net.devaction.kafka.transferswebsocketsservice.message;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class MessageWrapperEncoder implements Encoder.Text<MessageWrapper>{
    private static final Logger log = LoggerFactory.getLogger(MessageWrapperEncoder.class);

    private final ObjectMapper mapper = new ObjectMapper();
    
    @Override
    public void init(EndpointConfig config){
        // Nothing to do      
    }
    
    @Override
    public String encode(MessageWrapper messageWrapper) throws EncodeException{
        log.trace("MessageWrapper to be encoded: {}", messageWrapper);
        String json = null;
        try{
            json = mapper.writeValueAsString(messageWrapper);
        } catch (JsonProcessingException ex){
            log.error("Unable to serialize {} object: {}", 
                    MessageWrapper.class.getSimpleName(), 
                    messageWrapper, ex);
        }
        
        log.trace("Encoded JSON: {}", json);
        return json;
    }
    
    @Override
    public void destroy(){
    // Nothing to do.
    }
}
