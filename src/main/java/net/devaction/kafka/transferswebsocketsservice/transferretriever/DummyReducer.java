package net.devaction.kafka.transferswebsocketsservice.transferretriever;

import org.apache.kafka.streams.kstream.Reducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.avro.Transfer;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class DummyReducer implements Reducer<Transfer>{
    private static final Logger log = LoggerFactory.getLogger(DummyReducer.class);

    @Override
    public Transfer apply(Transfer value1, Transfer value2){
        return value2;
    }
}
