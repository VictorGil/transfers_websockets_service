package net.devaction.kafka.transferswebsocketsservice.processor.balanceupdatesproducer;

import java.util.Collection;
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
public class UpdatesDispatcher{
    
    private static final Logger log = LoggerFactory.getLogger(UpdatesDispatcher.class);
    
    private final Map<String, HashSet<Session>> sessionsMap = new HashMap<String, HashSet<Session>>();
    
    private final AccountBalanceSender sender; 
    
    public UpdatesDispatcher(AccountBalanceSender sender) {
        this.sender = sender;
    }
    
    public void dispatch(AccountBalanceEntity balance) {
        HashSet<Session> sessions = sessionsMap.get(balance.getAccountId());
        
        if (sessions != null) {
            for (Session session : sessions) {
                sender.send(balance, session, MessageType.BALANCE_DATA_UPDATE);
            }
        }
    }

    public synchronized void addSession(String accountId, Session session) {        
        log.trace("Going to register new accountId-session pair, accountId: {}, session id: {}"
                + ", current entries:\n{}",
                accountId, session.getId(), sessionsMapToString());
        
        if (sessionsMap.get(accountId) == null) {
            addNewEntry(accountId, session);
            return;
        }
        
        sessionsMap.get(accountId).add(session);
    }
    
    void addNewEntry(String accountId, Session session) {
        HashSet<Session> newSet= new HashSet<>();
        newSet.add(session);
        sessionsMap.put(accountId, newSet);
    }
    
    public synchronized void removeSession(Session session) {
        Collection<HashSet<Session>> sets = sessionsMap.values();
        
        for (HashSet<Session> set : sets) {
            
            // A websockets session is subscribed to one account id
            // at most
            if (set.remove(session)){
                return;
            }
        }
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
