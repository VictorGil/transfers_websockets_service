package net.devaction.kafka.transferswebsocketsservice.processor;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.entity.TransferEntity;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferInfoRequest;
import net.devaction.kafka.transferswebsocketsservice.server.sender.TransferSender;
import net.devaction.kafka.transferswebsocketsservice.transferretriever.TransferRetriever;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class TransferInfoRequestProcessorImpl 
        implements TransferInfoRequestProcessor{
    
    private static final Logger log = LoggerFactory.getLogger(TransferInfoRequestProcessorImpl.class);

    private final TransferRetriever retriever;
    private final TransferSender sender;
    
    public TransferInfoRequestProcessorImpl(TransferRetriever retriever, TransferSender sender){
        this.retriever = retriever;
        this.sender = sender;
    }

    @Override
    public void process(TransferInfoRequest request, Session session){
        log.trace("Session id: {}. Going to process the following request: {}", 
                session.getId(), request); 
        
        TransferEntity transfer = retriever.retrieve(request.getTransferId());
        sender.send(transfer, session);
    }
}
