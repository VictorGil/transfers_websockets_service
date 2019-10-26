package net.devaction.kafka.transferswebsocketsservice.processor.dispatcher;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class SessionsRemover {
    private static final Logger log = LoggerFactory.getLogger(SessionsRemover.class);

    private static SessionsRemover SINGLETON;

    private final BalanceUpdatesDispatcher balanceUpdatesDispatcher;
    private final TransferDataDispatcher transferDataDispatcher;

    public SessionsRemover(BalanceUpdatesDispatcher balanceUpdatesDispatcher,
            TransferDataDispatcher transferDataDispatcher) {

        if (SINGLETON != null) {
            String errorMessage = "An instance of this singleton already exists";
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }

        this.balanceUpdatesDispatcher = balanceUpdatesDispatcher;
        this.transferDataDispatcher = transferDataDispatcher;

        // this is not nice but we gotta do it
        // because we cannot instantiate the ServerEndPoint class
        SINGLETON = this;
    }

    public static SessionsRemover getSingleton() {
        if (SINGLETON == null) {
            String errorMessage = "An instance of this class does not exist";
            log.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
        return SINGLETON;
    }

    public void remove(Session session) {
        balanceUpdatesDispatcher.removeSession(session);
        transferDataDispatcher.removeSession(session);
    }
}
