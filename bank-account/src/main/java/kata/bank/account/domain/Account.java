package kata.bank.account.domain;

import org.javamoney.moneta.Money;

import java.util.Objects;

public class Account {
    private final String clientId;

    private Statement currentStatement = new Statement();

    public Account(String clientId) {
        Objects.requireNonNull(clientId);
        this.clientId = clientId;
    }

    public String getClientId() {
        return clientId;
    }

    public Money getBalance() {
        return currentStatement.getBalance();
    }

    public void startNextStatement() {
        currentStatement = currentStatement.nextStatement();
    }

    public void process(Operation operation) {
        currentStatement.process(operation);
    }
}
