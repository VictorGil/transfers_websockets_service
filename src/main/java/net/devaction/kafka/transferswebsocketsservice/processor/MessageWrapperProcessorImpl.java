package net.devaction.kafka.transferswebsocketsservice.processor;

import java.io.IOException;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.devaction.kafka.transferswebsocketsservice.message.MessageType;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.AccountBalanceRequest;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.AccountBalanceSubscriptionRequest;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferDataRequest;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class MessageWrapperProcessorImpl implements MessageWrapperProcessor {
    private static final Logger log = LoggerFactory.getLogger(MessageWrapperProcessorImpl.class);

    private final AccountBalanceRequestProcessor accountBalanceRequestProcessor;
    private final TransferInfoRequestProcessor transferInfoRequestProcessor;
    private final AccountBalanceSubscriptionRequestProcessor accountBalanceSubscriptionRequestProcessor;

    private final ObjectMapper mapper;

    public MessageWrapperProcessorImpl(
            AccountBalanceRequestProcessor accountBalanceRequestProcessor,
            AccountBalanceSubscriptionRequestProcessor accountBalanceSubscriptionRequestProcessor,
            TransferInfoRequestProcessor transferInfoRequestProcessor) {

        this.accountBalanceRequestProcessor = accountBalanceRequestProcessor;
        this.transferInfoRequestProcessor = transferInfoRequestProcessor;
        this.accountBalanceSubscriptionRequestProcessor = accountBalanceSubscriptionRequestProcessor;

        mapper = new ObjectMapper();
    }

    @Override
    public void process(MessageWrapper messageWrapper, Session session) {

        if (messageWrapper.getType()
                .equalsIgnoreCase(MessageType.BALANCE_DATA_REQUEST.name())) {
            AccountBalanceRequest accountBalanceRequest;

            try {
                accountBalanceRequest = mapper.readValue(messageWrapper.getPayload(),
                        AccountBalanceRequest.class);
            } catch (IOException ex) {
                log.error("Unable to deserialize {} message payload: {}",
                        MessageType.BALANCE_DATA_REQUEST.name(),
                        messageWrapper, ex);
                return;
            }

            accountBalanceRequestProcessor.process(accountBalanceRequest, session);
        }

        if (messageWrapper.getType()
                .equalsIgnoreCase(MessageType.BALANCE_DATA_SUBSCRIPTION.name())) {
            AccountBalanceSubscriptionRequest accountBalanceSubscriptionRequest;

            try {
                accountBalanceSubscriptionRequest = mapper.readValue(messageWrapper.getPayload(),
                        AccountBalanceSubscriptionRequest.class);
            } catch (IOException ex) {
                log.error("Unable to deserialize {} message payload: {}",
                        MessageType.BALANCE_DATA_SUBSCRIPTION.name(),
                        messageWrapper, ex);
                return;
            }

            accountBalanceSubscriptionRequestProcessor.process(accountBalanceSubscriptionRequest, session);
        }

        TransferDataRequest transferInfoRequest;
        if (messageWrapper.getType()
                .equalsIgnoreCase(MessageType.TRANSFER_DATA_REQUEST.name())) {
            try {
                transferInfoRequest = mapper.readValue(messageWrapper.getPayload(),
                        TransferDataRequest.class);
            } catch (IOException ex) {
                log.error("Unable to deserialize {} message payload: {}",
                        MessageType.TRANSFER_DATA_REQUEST.name(),
                        messageWrapper);
                return;
            }

            transferInfoRequestProcessor.process(transferInfoRequest, session);
        }
    }
}
