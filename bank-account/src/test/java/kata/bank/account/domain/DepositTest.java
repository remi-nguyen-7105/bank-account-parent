package kata.bank.account.domain;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void givenDeposit_whenCheck_ok() {
        Deposit deposit = new Deposit(new BigDecimal("1000.00"));

        assertDoesNotThrow(() -> deposit.check(BigDecimal.ZERO));
        assertDoesNotThrow(() -> deposit.check(new BigDecimal("1000.00")));
        assertDoesNotThrow(() -> deposit.check(new BigDecimal("-1000.00")));
    }

    @Test
    public void givenDeposit_whenApplyOn_calculationOk_0() {
        BigDecimal value = new BigDecimal("1000.00");
        Deposit deposit = new Deposit(value);
        assertEquals(value, deposit.applyOn(BigDecimal.ZERO));
    }


    /* rounding calculation problem
    @Test
    public void givenDeposit_whenApplyOn_calculationOk_1() {
        BigDecimal value = new BigDecimal("1000");
        Deposit deposit = new Deposit(value);
        assertEquals(new BigDecimal("1001.00"), deposit.applyOn(BigDecimal.ONE));
    }

     */
}
