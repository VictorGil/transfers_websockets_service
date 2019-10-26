package net.devaction.kafka.transferswebsocketsservice.message.incoming;

/**
 * @author Víctor Gil
 *
 * since August 2019
 */
public class TransferDataSubscriptionRequest implements Request {
    private String accountId;

    // Jackson needs this constructor
    public TransferDataSubscriptionRequest() {
    }

    public TransferDataSubscriptionRequest(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [accountId=" + accountId + "]";
    }

    @Override
    public int hashCode() {
        return 31 + ((accountId == null) ? 0 : accountId.hashCode());
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

        TransferDataSubscriptionRequest other = (TransferDataSubscriptionRequest) obj;

        if (accountId == null) {
            if (other.accountId != null) {
                return false;
            }
        } else if (!accountId.equals(other.accountId)) {
            return false;
        }

        return true;
    }
}
