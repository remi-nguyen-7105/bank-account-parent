package kata.bank.account.domain;

import org.javamoney.moneta.Money;

import java.util.Objects;

public record Deposit(Money amount) implements Operation {
    public Deposit {
        Objects.requireNonNull(amount);
        if (amount.isNegativeOrZero()) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Money applyOn(Money money) {
        return money.add(amount());
    }
}
