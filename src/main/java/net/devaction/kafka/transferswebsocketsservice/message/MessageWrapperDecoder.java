package net.devaction.kafka.transferswebsocketsservice.message;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class MessageWrapperDecoder implements Decoder.Text<MessageWrapper>{
    private static final Logger log = LoggerFactory.getLogger(MessageWrapperDecoder.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void init(EndpointConfig config) {
        // Nothing to do
    }

    @Override
    public void destroy() {
        // Nothing to do
    }

    @Override
    public MessageWrapper decode(String jsonString) throws DecodeException{
        log.trace("JSON to be decoded: {}", jsonString);
        MessageWrapper messageWrapper = null;
        try{
            messageWrapper = mapper.readValue(jsonString, MessageWrapper.class);
        } catch (IOException ex) {
            log.error("Unable to deserialize JSON {} object: {}",
                    MessageWrapper.class.getSimpleName(), jsonString, ex);
        }

        return messageWrapper;
    }

    @Override
    public boolean willDecode(String jsonString) {
        return true;
    }
}
