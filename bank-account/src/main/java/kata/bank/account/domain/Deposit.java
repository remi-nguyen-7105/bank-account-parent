package kata.bank.account.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record Deposit(BigDecimal amount) implements Operation {
    public Deposit {
        Objects.requireNonNull(amount);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public BigDecimal applyOn(BigDecimal value) {
        return value.add(amount());
    }
}
