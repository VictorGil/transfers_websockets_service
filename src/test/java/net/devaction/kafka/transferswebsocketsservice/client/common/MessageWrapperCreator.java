package net.devaction.kafka.transferswebsocketsservice.client.common;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.devaction.kafka.transferswebsocketsservice.message.MessageType;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.Request;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class MessageWrapperCreator {
    private static final Logger log = LoggerFactory.getLogger(MessageWrapperCreator.class);

    private final ObjectMapper mapper = new ObjectMapper();

    public MessageWrapper create(Request request, MessageType messageType) throws IOException {
        final String json;

        try {
            json = mapper.writeValueAsString(request);
            log.debug("{} JSON string:\n {}",
                    request.getClass().getSimpleName(), json);

        } catch (JsonProcessingException ex) {
            log.error("Unable to serialize Java object: {}", request, ex);
            throw ex;
        }

        return new MessageWrapper(messageType.name(), json);
    }
}
