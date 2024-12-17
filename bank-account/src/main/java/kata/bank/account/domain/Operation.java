package kata.bank.account.domain;

import java.math.BigDecimal;

public sealed interface Operation permits Deposit, Withdrawal {
    default void check(BigDecimal balance) {
        // no check
    }

    BigDecimal applyOn(BigDecimal value);
}
