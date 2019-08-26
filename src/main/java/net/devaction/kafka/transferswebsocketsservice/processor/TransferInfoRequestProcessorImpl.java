package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.transferswebsocketsservice.message.TransferInfoRequest;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class TransferInfoRequestProcessorImpl 
        implements TransferInfoRequestProcessor{
    
    private static final Logger log = LoggerFactory.getLogger(TransferInfoRequestProcessorImpl.class);

    @Override
    public void process(TransferInfoRequest request, Session session){
        log.trace("Session id: {}. Going to process the following request: {}", 
                session.getId(), request); 
        
        // TODO
    }
}
