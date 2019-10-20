package net.devaction.kafka.transferconsumer;

import net.devaction.kafka.avro.Transfer;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public interface TransferUpdateProcessor {

    public void process(Transfer transfer);

}
