package kata.bank.account.utils;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTest {

    @Test
    public void test_Money() {
        var zero = Money.of(BigDecimal.ZERO, "EUR");
        var amount100 = Money.of(new BigDecimal("100.00"), "EUR");
        var amount = zero.add(amount100);
        assertEquals(amount, amount100);
        amount = amount.add(amount100.negate());
        assertEquals(amount, zero);
    }
}
