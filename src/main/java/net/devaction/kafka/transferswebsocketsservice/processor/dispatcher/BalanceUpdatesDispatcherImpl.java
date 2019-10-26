package net.devaction.kafka.transferswebsocketsservice.processor.dispatcher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.entity.AccountBalanceEntity;
import net.devaction.kafka.transferswebsocketsservice.message.MessageType;
import net.devaction.kafka.transferswebsocketsservice.server.sender.AccountBalanceSender;

/**
 * @author Víctor Gil
 *
 * since September 2019
 */
public class BalanceUpdatesDispatcherImpl implements BalanceUpdatesDispatcher {

    private static final Logger log = LoggerFactory.getLogger(BalanceUpdatesDispatcherImpl.class);

    private final Map<String, HashSet<Session>> sessionsMap = new HashMap<String, HashSet<Session>>();

    private final AccountBalanceSender sender;

    public BalanceUpdatesDispatcherImpl(AccountBalanceSender sender) {
        this.sender = sender;
    }

    public synchronized void addSession(String accountId, Session session) {

        if (log.isTraceEnabled()) {
            log.trace("Going to register new accountId-session pair, accountId: {}, session id: {}"
                    + ", current entries:\n {}",
                    accountId, session.getId(), sessionsMapToString());
        }
        // So far a session can only be subscribed to updates on one account id
        removeSession(session);

        // TODO we should check here that the accountId exists in Kafka (i.e., it is valid).

        if (!sessionsMap.containsKey(accountId)) {
            addNewEntry(accountId, session);
            return;
        }

        sessionsMap.get(accountId).add(session);
    }

    public void dispatch(AccountBalanceEntity balance) {
        HashSet<Session> sessions = sessionsMap.get(balance.getAccountId());

        if (sessions != null) {
            for (Session session : sessions) {
                sender.send(balance, session, MessageType.BALANCE_DATA_UPDATE);
            }
        }
    }

    public synchronized void removeSession(Session session) {
        Set<Entry<String, HashSet<Session>>> entries = sessionsMap.entrySet();
        for (Entry<String, HashSet<Session>> entry : entries) {
            if (entry.getValue().remove(session)) {
                log.trace("Mapping of session {} has been removed", session.getId());
                if (entry.getValue().isEmpty()) {
                    sessionsMap.remove(entry.getKey());
                }
                return;
            }
        }
    }

    void addNewEntry(String accountId, Session session) {
        HashSet<Session> newSet = new HashSet<>();
        newSet.add(session);
        sessionsMap.put(accountId, newSet);
    }

    String sessionsMapToString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Number of entries: " + calculateNumberOfEntries() + "\n");

        for (Entry<String, HashSet<Session>> entry : sessionsMap.entrySet()) {
            sb.append("\n    Account id: " + entry.getKey() + "\n");
            HashSet<Session> sessions = entry.getValue();
            for (Session session : sessions) {
                sb.append("    Session id: " + session.getId() + "\n");
            }
        }
        sb.append("\n");

        return sb.toString();
    }

    private long calculateNumberOfEntries() {
        long count = 0;

        for (HashSet<Session> set : sessionsMap.values()) {
            count = count + set.size();
        }

        return count;
    }
}
