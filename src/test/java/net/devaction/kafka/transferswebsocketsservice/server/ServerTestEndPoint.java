package net.devaction.kafka.transferswebsocketsservice.server;

import java.util.Set;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.devaction.entity.AccountBalanceEntity;
import net.devaction.kafka.transferswebsocketsservice.message.MessageType;
import net.devaction.kafka.transferswebsocketsservice.message.MessageWrapper;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
@ServerEndpoint(value = "/001")
public class ServerTestEndPoint {
    private static final Logger log = LoggerFactory.getLogger(ServerTestEndPoint.class);

    private final ObjectMapper mapper = new ObjectMapper();

    @OnOpen
    public void onOpen(final Session session, final EndpointConfig config) {
        log.debug("Session {} has been opened.", session.getId());
    }

    @OnMessage
    public void onMessage(final String message, final Session session) {
        log.debug("Message received: {}", message);

        log.debug("Going to send a message to all the open sessions");
        Set<Session> sessions = session.getOpenSessions();
        int i = 0;
        for (Session sessionAux: sessions) {
            if (sessionAux.isOpen()) {
                i++;

                //sendTextMessage1(sessionAux, i);
                sendTextMessage2(sessionAux);
                //sendTestBalance(sessionAux);

                log.trace("Message asynchronously being sent to {} session", sessionAux.getId());
            }
        }
    }

    // This method is used to test and debug the Angular frontend code
    private void sendTextMessage1(final Session session, final int i) {
        String text1 = String.format("Message #%d to server session %s", i, session.getId());

        final String text2 = "{"
                + "\"type\":\"BALANCE_DATA\","
                + "\"payload\":"
                    + "\"{\\\"accountId\\\":\\\"29822b097953\\\","
                    + "\\\"clientId\":\\\"a3086f3f404d\\\","
                    + "\\\"transferId\\\":\\\"INITIAL\\\","
                    + "\\\"balance\\\":0,"
                    + "\\\"version\\\":0}\"}";

        final String text3 = "{"
               // + "\"type\":\"BALANCE_DATA\","
                + "\"payload\":"
                    + "\"{\\\"accountId\\\":\\\"29822b097953\\\","
                    + "\\\"clientId\\\":\\\"a3086f3f404d\\\","
                    + "\\\"transferId\\\":\\\"INITIAL\\\","
                    + "\\\"balance\\\":0,"
                    + "\\\"version\\\":0}\"}";

        session.getAsyncRemote().sendText(text3);
    }

    // This method is used to test and debug the Angular frontend code
    private void sendTextMessage2(final Session session) {

        final String text1 = "{"
                + "\"type\":\"TRANSFER_DATA\","
                + "\"payload\":"
                    + "\"{\\\"id\\\":\\\"29822b097001\\\","
                    + "\\\"accountId\\\":\\\"a3086f3f4002\\\","
                    + "\\\"amount\\\":659.34,"
                    + "\\\"transferTS\\\":1566056434953"
                    + "}\"}";

        session.getAsyncRemote().sendText(text1);
    }

    private void sendTestBalance(final Session session) {
        String message;
        try {
            message = createBalanceMessage();
        } catch (JsonProcessingException ex) {
            return;
        }
        session.getAsyncRemote().sendText(message);
    }

    private String createBalanceMessage() throws JsonProcessingException {
        final AccountBalanceEntity balance = new AccountBalanceEntity("29822b097953", "a3086f3f404d");
        String balanceJson;
        try {
            balanceJson = mapper.writeValueAsString(balance);
        } catch (JsonProcessingException ex) {
            log.error("Unable to serialize {} to JSON: {}",
                    AccountBalanceEntity.class.getSimpleName(),
                    balance, ex);
            throw ex;
        }

        final MessageWrapper message = new MessageWrapper(MessageType.BALANCE_DATA_REQUEST.name(),
                balanceJson);

        String messageJson;
        try {
            messageJson = mapper.writeValueAsString(message);
        } catch (JsonProcessingException ex) {
            log.error("Unable to serialize {} to JSON: {}",
                    MessageWrapper.class.getSimpleName(),
                    message, ex);
            throw ex;
        }

        return messageJson;
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Session {} threw an error: {}", session.getId(), throwable, throwable);
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        log.debug("Session {} has been closed.", session.getId());
    }
}
