package kata.bank.account.domain;


import kata.bank.account.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void givenWithdrawal_whenCheckWithEnoughFunds_ok() {
        Withdrawal withdrawal = new Withdrawal(new BigDecimal("1000.00"));

        assertDoesNotThrow(() -> withdrawal.check(new BigDecimal("1001.00")));
        assertDoesNotThrow(() -> withdrawal.check(new BigDecimal("1000")));
        assertDoesNotThrow(() -> withdrawal.check(new BigDecimal("1000.00")));
    }

    @Test
    public void givenWithdrawal_whenCheckWithInsufficientFunds_ko() {
        Withdrawal withdrawal = new Withdrawal(new BigDecimal("2000.00"));

        assertThrows(InsufficientFundsException.class, () -> withdrawal.check(new BigDecimal("1000.00")));
    }

    @Test
    public void givenWithdrawal_whenApplyOn_calculationOk_0() {
        BigDecimal value = new BigDecimal("1000.00");
        Withdrawal withdrawal = new Withdrawal(value);
        assertEquals(value.negate(), withdrawal.applyOn(BigDecimal.ZERO));
    }
}
