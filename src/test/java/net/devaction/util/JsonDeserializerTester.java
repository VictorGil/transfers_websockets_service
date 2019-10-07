package net.devaction.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.devaction.entity.AccountBalanceEntity;
import net.devaction.kafka.avro.AccountBalance;
/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class JsonDeserializerTester {
    private static final Logger log = LoggerFactory.getLogger(JsonDeserializerTester.class);

    private final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        new JsonDeserializerTester().run();
    }

    private void run() {
        //run1();
        run2();
    }

    private void run1() {
        AccountBalanceEntity balance = new AccountBalanceEntity("test-id", "test-client-id");
        String json = null;

        try {
            json = mapper.writeValueAsString(balance);
        } catch (JsonProcessingException ex) {
            log.error("{}", ex, ex);
            return;
        }
        log.debug("AccountBalanceEntity object:\n {}\n JSON representation:\n {}",
                balance, json);
    }

    private void run2() {
        final String filename = "accountBalance_01.json.txt";

        JsonDeserializer deserializer = new JsonDeserializer();

        AccountBalanceEntity accountBalanceEntity = null;
        try {
            accountBalanceEntity = deserializer.deserializeFromFile(filename,
                    AccountBalanceEntity.class);
        } catch (Exception e) {
            return;
        }

        log.debug("Deserialized object:\n {}\nSource JSON file: {}",
                accountBalanceEntity, filename);
    }
}
