package kata.bank.account.domain;


import kata.bank.account.exception.InsufficientFundsException;
import kata.bank.account.utils.MoneyHelper;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class WithdrawTest {

    private Money money(BigDecimal amount) {
        return Money.of(amount, MoneyHelper.DEFAULT_CURRENCY_CODE);
    }

    private Money money(String amount) {
        return money(new BigDecimal(amount));
    }

    @Test
    public void whenCreate_ok() {
        var amount = money("1000.00");
        assertDoesNotThrow(() -> new Withdrawal(amount));
    }

    @Test
    public void whenCreate_amountMustNotBeNull() {
        assertThrows(NullPointerException.class, () -> new Withdrawal(null));
    }

    @Test
    public void whenCreate_amountMustNotBeNegative() {
        var amount = money("-1000.00");
        assertThrows(IllegalArgumentException.class, () -> new Withdrawal(amount));
    }

    @Test
    public void whenCreate_amountMustNotBeZero() {
        var amount = money(BigDecimal.ZERO);
        assertThrows(IllegalArgumentException.class, () -> new Withdrawal(amount));
    }

    @Test
    public void givenWithdrawal_whenCheckWithEnoughFunds_ok() {
        Withdrawal withdrawal = new Withdrawal(money("1000.00"));
        Money money_1001 = money("1001.00");
        Money money_1000 = money("1000");
        Money money_1000_00 = money("1000.00");

        assertDoesNotThrow(() -> withdrawal.check(money_1001));
        assertDoesNotThrow(() -> withdrawal.check(money_1000));
        assertDoesNotThrow(() -> withdrawal.check(money_1000_00));
    }

    @Test
    public void givenWithdrawal_whenCheckWithInsufficientFunds_ko() {
        Withdrawal withdrawal = new Withdrawal(money("2000.00"));

        assertThrows(InsufficientFundsException.class, () -> withdrawal.check(money("1000.00")));
    }

    @Test
    public void givenWithdrawal_whenApplyOn_calculationOk_0() {
        Money money = money("1000.00");
        Withdrawal withdrawal = new Withdrawal(money);
        assertEquals(money.negate(), withdrawal.applyOn(money(BigDecimal.ZERO)));
    }
}
