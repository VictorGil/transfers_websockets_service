package net.devaction.kafka.transferswebsocketsservice.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class SerializationTester {
    private static final Logger log = LoggerFactory.getLogger(SerializationTester.class);

    private ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        new SerializationTester().run();
    }

    private void run() {
        run1();
    }

    private void run1() {
        MessageWrapper messageWrapper = new MessageWrapper(MessageType.BALANCE_DATA_REQUEST.name(), "whatever");
        log.info("Going to serialize: {}", messageWrapper);
        String json = null;
        try {
            json = mapper.writeValueAsString(messageWrapper);
        } catch (JsonProcessingException ex) {
            log.error(" {}", ex, ex);
        }
        log.info("JSON result: {}", json);
    }

    private void run2() {
        MessageWrapper messageWrapper = new MessageWrapper(MessageType.BALANCE_DATA_REQUEST.name(), "whatever");
        log.info("Going to serialize: {}", messageWrapper);
        String json = null;
        try {
            json = mapper.writeValueAsString(messageWrapper);
        } catch (JsonProcessingException ex) {
            log.error(" {}", ex, ex);
        }
        log.info("JSON result: {}", json);
    }
}