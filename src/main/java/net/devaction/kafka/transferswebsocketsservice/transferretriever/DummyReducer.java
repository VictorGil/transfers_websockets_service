package net.devaction.kafka.transferswebsocketsservice.transferretriever;

import org.apache.kafka.streams.kstream.Reducer;

import net.devaction.kafka.avro.Transfer;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class DummyReducer implements Reducer<Transfer>{

    @Override
    public Transfer apply(Transfer value1, Transfer value2) {
        return value2;
    }
}
