package kata.bank.account.domain;

import kata.bank.account.exception.InsufficientFundsException;
import org.javamoney.moneta.Money;

import java.util.Objects;

public record Withdrawal(Money amount) implements Operation {
    public Withdrawal {
        Objects.requireNonNull(amount);
        if (amount.isNegativeOrZero()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void check(Money balance) {
        if (amount().isGreaterThan(balance)) {
            throw new InsufficientFundsException("amount : " + amount());
        }
    }

    @Override
    public Money applyOn(Money value) {
        return value.subtract(amount());
    }
}
