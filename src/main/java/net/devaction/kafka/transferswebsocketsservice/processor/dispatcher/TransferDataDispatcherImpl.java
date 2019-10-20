package net.devaction.kafka.transferswebsocketsservice.processor.dispatcher;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.devaction.entity.TransferEntity;
import net.devaction.kafka.transferswebsocketsservice.server.sender.TransferDataUpdateSender;

/**
 * @author Víctor Gil
 *
 * since October 2019
 */
public class TransferDataDispatcherImpl implements TransferDataDispatcher {

    private static final Logger log = LoggerFactory.getLogger(TransferDataDispatcherImpl.class);

    private final Map<String, HashSet<Session>> sessionsMap = new HashMap<String, HashSet<Session>>();

    private final TransferDataUpdateSender sender;

    public TransferDataDispatcherImpl(TransferDataUpdateSender sender) {
        this.sender = sender;
    }

    @Override
    public void addSession(String accountId, Session session) {
        if (log.isTraceEnabled()) {
            log.trace("Going to register new accountId-session pair, accountId: {}, session id: {}"
                    + ", current entries:\n {}",
                    accountId, session.getId(), sessionsMapToString());
        }

        removeExistingMapping(session);

        // TODO we should check here that the accountId exists in Kafka (i.e., it is valid).

        if (!sessionsMap.containsKey(accountId)) {
            addNewEntry(accountId, session);
            return;
        }

        sessionsMap.get(accountId).add(session);
    }

    @Override
    public void dispatch(TransferEntity transfer) {
        HashSet<Session> sessions = sessionsMap.get(transfer.getAccountId());

        if (sessions != null) {
            for (Session session : sessions) {
                sender.send(transfer, session);
            }
        }
    }

    void removeExistingMapping(Session session) {
        Set<Entry<String, HashSet<Session>>> entries = sessionsMap.entrySet();
        for (Entry<String, HashSet<Session>> entry : entries) {
            if (entry.getValue().remove(session)) {
                log.trace("Previous mapping of session {} removed", session.getId());
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
        Set<Entry<String, HashSet<Session>>> entries = sessionsMap.entrySet();
        sb.append("Number of entries: " + entries.size() + "\n");
        for (Entry<String, HashSet<Session>> entry : entries) {
            sb.append("\nAccount id: " + entry.getKey() + "\n");
            HashSet<Session> sessions = entry.getValue();
            for (Session session : sessions) {
                sb.append("    " + session.getId() + "\n");
            }
        }
        sb.append("\n");

        return sb.toString();
    }
}
