package net.devaction.kafka.transferswebsocketsservice.client.sender;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.kafka.transferswebsocketsservice.client.common.MessageWrapperCreator;
import net.devaction.kafka.transferswebsocketsservice.message.MessageType;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.AccountBalanceRequest;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.AccountBalanceSubscriptionRequest;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferDataRequest;
import net.devaction.kafka.transferswebsocketsservice.message.incoming.TransferDataSubscriptionRequest;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TestRequestSenderMain {
    private static final Logger log = LoggerFactory.getLogger(TestRequestSenderMain.class);

    private final MessageWrapperCreator messageWrapperCreator = new MessageWrapperCreator();
    private final TestRequestSender sender = new TestRequestSender();

    public static void main(String[] args) {
        new TestRequestSenderMain().run();
    }

    private void run() {
        MessageWrapper messageWrapper = null;

        try {
            // messageWrapper = createBalanceRequestMessageWrapper();
            // messageWrapper = createBalanceSubscriptionRequestMessageWrapper();
            // messageWrapper = createTransferDataRequestMessageWrapper();
            messageWrapper = createTransferDataSubscriptionRequestMessageWrapper();
        } catch (IOException ex) {
            log.info("Exiting");
            return;
        }

        sender.run(messageWrapper);
    }

    private MessageWrapper createBalanceRequestMessageWrapper() throws IOException {
        AccountBalanceRequest request = createAccountBalanceRequest();

        return messageWrapperCreator.create(request, MessageType.BALANCE_DATA_REQUEST);
    }

    private MessageWrapper createBalanceSubscriptionRequestMessageWrapper() throws IOException {
        AccountBalanceRequest request = createAccountBalanceRequest();

        return messageWrapperCreator.create(request, MessageType.BALANCE_DATA_SUBSCRIPTION);
    }

    private MessageWrapper createTransferDataRequestMessageWrapper() throws IOException {
        TransferDataRequest request = createTransferDataRequest();

        return  messageWrapperCreator.create(request, MessageType.TRANSFER_DATA_REQUEST);
    }

    private MessageWrapper createTransferDataSubscriptionRequestMessageWrapper() throws IOException {
        TransferDataSubscriptionRequest request = createTransferDataSubscriptionRequest();

        return  messageWrapperCreator.create(request, MessageType.TRANSFER_DATA_SUBSCRIPTION);
    }

    private AccountBalanceRequest createAccountBalanceRequest() {

        // AccountBalanceRequest request = new AccountBalanceRequest("28a090daa001");
        AccountBalanceRequest request = new AccountBalanceRequest("28a090daa002");
        // AccountBalanceRequest request = new AccountBalanceRequest("28a090daa003");
        return request;
    }

    private TransferDataRequest createTransferDataRequest() {
        return new TransferDataRequest("5eb0f2dfd9c3"); // transfer id
        //return new TransferDataRequest("28a090daa001"); // account id --> won't work, as expected
    }

    private TransferDataSubscriptionRequest createTransferDataSubscriptionRequest() {
        // return new TransferDataSubscriptionRequest("28a090daa001"); // account id
        return new TransferDataSubscriptionRequest("28a090daa002"); // account id
    }
}
