package kata.bank.account.domain;

import org.javamoney.moneta.Money;

public sealed interface Operation permits Deposit, Withdrawal {
    default void check(Money balance) {
        // no check
    }

    Money applyOn(Money value);
}
