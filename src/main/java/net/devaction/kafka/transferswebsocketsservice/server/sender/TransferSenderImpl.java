package net.devaction.kafka.transferswebsocketsservice.server.sender;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.devaction.entity.TransferEntity;
import net.devaction.kafka.transferswebsocketsservice.message.MessageType;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class TransferSenderImpl implements TransferSender{
    private static final Logger log = LoggerFactory.getLogger(TransferSenderImpl.class);

    private final ObjectMapper mapper = new ObjectMapper();

    private final MessageSender messageSender;

    public TransferSenderImpl(MessageSender messageSender){
        this.messageSender = messageSender;
    }

    @Override
    public void send(TransferEntity transfer, Session session){
        String json;

        try{
            json = mapper.writeValueAsString(transfer);
        } catch (JsonProcessingException ex){
            log.error("Unable to serialize {} to JSON: {}",
                    TransferEntity.class.getSimpleName(),
                    transfer, ex);
            return;
        }

        MessageWrapper message = new MessageWrapper(MessageType.TRANSFER_DATA_RESPONSE.name(),
                json);

        messageSender.send(message, session);
    }
}
