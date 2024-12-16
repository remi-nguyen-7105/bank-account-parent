package kata.bank.account.domain;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WithdrawTest {

    @Test
    public void whenCreate_ok() {
        var amount = new BigDecimal("1000.00");
        assertDoesNotThrow(() -> new Withdrawal(amount));
    }

    @Test
    public void whenCreate_amountMustNotBeNull() {
        assertThrows(NullPointerException.class, () -> new Withdrawal(null));
    }

    @Test
    public void whenCreate_amountMustNotBeNegative() {
        var amount = new BigDecimal("-1000.00");
        assertThrows(IllegalArgumentException.class, () -> new Withdrawal(amount));
    }

    @Test
    public void whenCreate_amountMustNotBeZero() {
        var amount = BigDecimal.ZERO;
        assertThrows(IllegalArgumentException.class, () -> new Withdrawal(amount));
    }
}
