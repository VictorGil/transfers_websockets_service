package net.devaction.kafka.transferswebsocketsservice.message.incoming;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Víctor Gil
 *
 * since October 2019
 *
 * The client who sent this message wants to be notified of
 * accountBalance messages related to the specified account ID
 * coming from Kafka.
 */
public class AccountBalanceSubscriptionRequest {
    private static final Logger log = LoggerFactory.getLogger(AccountBalanceSubscriptionRequest.class);

    private String accountId;

    // Jackson needs this constructor
    public AccountBalanceSubscriptionRequest() { }

    public AccountBalanceSubscriptionRequest(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        AccountBalanceSubscriptionRequest other = (AccountBalanceSubscriptionRequest) obj;
        if (accountId == null) {
            if (other.accountId != null) {
                return false;
            }
        } else if (!accountId.equals(other.accountId)) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "AccountBalanceSubscriptionRequest [accountId=" + accountId + "]";
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}
