package kata.bank.account.domain;

import kata.bank.account.exception.InsufficientFundsException;

import java.math.BigDecimal;
import java.util.Objects;

public record Withdrawal(BigDecimal amount) implements Operation {
    public Withdrawal {
        Objects.requireNonNull(amount);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void check(BigDecimal balance) {
        if (amount().compareTo(balance) > 0) {
            throw new InsufficientFundsException("amount : " + amount());
        }
    }

    @Override
    public BigDecimal applyOn(BigDecimal value) {
        return value.subtract(amount());
    }
}
