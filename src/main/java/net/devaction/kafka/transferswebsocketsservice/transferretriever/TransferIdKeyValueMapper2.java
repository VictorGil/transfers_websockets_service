package net.devaction.kafka.transferswebsocketsservice.transferretriever;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.avro.Transfer;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class TransferIdKeyValueMapper2 implements KeyValueMapper<String, Transfer, KeyValue<String, Transfer>> {
    private static final Logger log = LoggerFactory.getLogger(TransferIdKeyValueMapper2.class);

    @Override
    public KeyValue<String, Transfer> apply(String accountIdKey, Transfer transfer){
        
        if (accountIdKey == null) {
            log.error("Assertion error, the accountId is null for the Transfer message, "
                    + "this should never happen, Transfer (value): {}", transfer);
            return null;
        }
        
        if (transfer == null) { 
            log.error("Assertion error, the value of the Transfer message is null, "
                    + "this should never happen, accountId (key): {}", accountIdKey);
            return null;
        }

        if (transfer.getId() == null) { 
            log.error("Assertion error, the id is null in this Transfer message, "
                    + "this should never happen, accountId (key): {}", accountIdKey);
            return null;
        }
        
        return new KeyValue<>(transfer.getId(), transfer);
    }
}
