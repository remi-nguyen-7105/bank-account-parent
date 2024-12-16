package kata.bank.account.domain;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DepositTest {

    @Test
    public void whenCreate_ok() {
        var amount = new BigDecimal("1000.00");
        assertDoesNotThrow(() -> new Deposit(amount));
    }

    @Test
    public void whenCreate_amountMustNotBeNull() {
        assertThrows(NullPointerException.class, () -> new Deposit(null));
    }

    @Test
    public void whenCreate_amountMustNotBeNegative() {
        var amount = new BigDecimal("-1000.00");
        assertThrows(IllegalArgumentException.class, () -> new Deposit(amount));
    }

    @Test
    public void whenCreate_amountMustNotBeZero() {
        var amount = BigDecimal.ZERO;
        assertThrows(IllegalArgumentException.class, () -> new Deposit(amount));
    }
}
