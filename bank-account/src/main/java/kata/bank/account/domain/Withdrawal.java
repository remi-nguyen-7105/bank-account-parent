package kata.bank.account.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record Withdrawal(BigDecimal amount) implements Operation {
    public Withdrawal {
        Objects.requireNonNull(amount);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException();
        }
    }
}
