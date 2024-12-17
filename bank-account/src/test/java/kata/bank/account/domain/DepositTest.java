package kata.bank.account.domain;


import kata.bank.account.utils.MoneyHelper;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class DepositTest {

    private Money money(BigDecimal amount) {
        return Money.of(amount, MoneyHelper.DEFAULT_CURRENCY_CODE);
    }

    private Money money(String amount) {
        return money(new BigDecimal(amount));
    }

    @Test
    public void whenCreate_ok() {
        var amount = money("1000.00");
        assertDoesNotThrow(() -> new Deposit(amount));
    }

    @Test
    public void whenCreate_amountMustNotBeNull() {
        assertThrows(NullPointerException.class, () -> new Deposit(null));
    }

    @Test
    public void whenCreate_amountMustNotBeNegative() {
        var amount = money("-1000.00");
        assertThrows(IllegalArgumentException.class, () -> new Deposit(amount));
    }

    @Test
    public void whenCreate_amountMustNotBeZero() {
        var amount = money(BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class, () -> new Deposit(amount));
    }

    @Test
    public void givenDeposit_whenCheck_ok() {
        Deposit deposit = new Deposit(money("1000.00"));
        Money money_zero = money(BigDecimal.ZERO);
        Money money_1000 = money("1000.00");
        Money money_minus1000 = money("-1000.00");

        assertDoesNotThrow(() -> deposit.check(money_zero));
        assertDoesNotThrow(() -> deposit.check(money_1000));
        assertDoesNotThrow(() -> deposit.check(money_minus1000));
    }

    @Test
    public void givenDeposit_whenApplyOn_calculationOk_0() {
        Money money = money("1000.00");
        Deposit deposit = new Deposit(money);
        assertEquals(money, deposit.applyOn(money(BigDecimal.ZERO)));
    }


    @Test
    public void givenDeposit_whenApplyOn_calculationOk_1() {
        Money money = money("1000");
        Deposit deposit = new Deposit(money);
        assertEquals(money("1001.00"), deposit.applyOn(money(BigDecimal.ONE)));
    }
}
