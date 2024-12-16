package kata.bank.account.domain;

import java.util.Objects;

public class Account {
    private final String clientId;

    public Account(String clientId) {
        Objects.requireNonNull(clientId);
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }
}
