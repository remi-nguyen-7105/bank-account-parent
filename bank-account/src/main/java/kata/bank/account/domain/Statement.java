package kata.bank.account.domain;

import kata.bank.account.utils.MoneyHelper;
import org.javamoney.moneta.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Statement {

    private final List<Operation> operations = new ArrayList<>();

    private Statement previous;
    // closingDate allow to manage Statement history
    // a "closed" statement is one that is previous to another Statement
    // see Statement>nextStatement()
    // using a closingDate fit better with Statement>getDate() than using a status
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

    private void check(Operation operation) {
        Objects.requireNonNull(operation);
        if (closingDate != null) {
            throw new IllegalStateException("new operation can not be process on close statement");
        }
        operation.check(this.getBalance());
    }

    protected void process(Operation operation) {
        check(operation);
        operations.add(operation);
    }

    // amount result from the operations sum in the statement
    public Money getAmount() {
        Money amount = Money.of(BigDecimal.ZERO, MoneyHelper.DEFAULT_CURRENCY_CODE);
        for (Operation operation : operations) {
            amount = operation.applyOn(amount);
        }
        return amount;
    }

    // balance represents available funds in account after processing :
    // - all operations in related statement
    // - all other past operations (=> delegate to previous)
    public Money getBalance() {
        // balance could be store/cache on a "close" statement for optimisation
        // better approach would be to create a Page/Block class of Operation with a fix max size
        Money balance = previous == null ? Money.of(BigDecimal.ZERO, MoneyHelper.DEFAULT_CURRENCY_CODE) : previous.getBalance();
        return balance.add(getAmount());
    }

    public String toString() {
        String template = "date :  %s  amount :  %s balance : %s";
        return String.format(template, getDate(), getAmount(), getBalance());
    }
}
