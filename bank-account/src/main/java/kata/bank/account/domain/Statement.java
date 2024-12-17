package kata.bank.account.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Statement {

    private final List<Operation> operations = new ArrayList<>();

    private Statement previous;
    // closingDate allow to manage Statement history
    // a "closed" statement is that is previous to another Statement
    // see Statement>nextStatement()
    // using closingDate works better with Statement>getDate() than using a status
    private LocalDateTime closingDate;

    public Statement() {
    }

    private Statement(Statement previous) {
        Objects.requireNonNull(previous);
        if (previous.closingDate == null) {
            throw new IllegalArgumentException();
        }
        this.previous = previous;
    }

    // using Statement>closingDate when available is consistent with
    // usual Statement semantic (like monthly Statements etc...°
    public LocalDateTime getDate() {
        return closingDate == null ? LocalDateTime.now() : closingDate;
    }

    protected LocalDateTime getClosingDate() {
        return closingDate;
    }

    protected Statement nextStatement() {
        closingDate = LocalDateTime.now();
        return new Statement(this);
    }

    protected void add(Operation operation) {
        Objects.requireNonNull(operation);
        if (closingDate != null) {
            throw new IllegalStateException("operation can not be add on close statement");
        }
        operation.check(this.getBalance());
        operations.add(operation);
    }

    // amount result from the operations sum in the statement
    public BigDecimal getAmount() {
        BigDecimal amount = BigDecimal.ZERO;
        for (Operation operation : operations) {
            amount = operation.applyOn(amount);
        }
        return amount;
    }

    // balance represents available funds in account after processing :
    // - all operations in related statement
    // - all other past operations (-> delegate to previous)
    public BigDecimal getBalance() {
        BigDecimal balance = previous == null ? BigDecimal.ZERO : previous.getBalance();
        return balance.add(getAmount());
    }
}
