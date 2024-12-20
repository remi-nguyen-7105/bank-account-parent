package kata.bank.account.domain;

import kata.bank.account.utils.MoneyHelper;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest {

    private Money money(BigDecimal amount) {
        return Money.of(amount, MoneyHelper.DEFAULT_CURRENCY_CODE);
    }

    @Test
    public void whenCreate_ok() {
        String clientId = "clientId";
        assertDoesNotThrow(() -> new Account(clientId));
    }

    @Test
    public void whenCreate_clientIdMustNotBeNull() {
        assertThrows(NullPointerException.class, () -> new Account(null));
    }

    @Test
    public void givenAccount_whenGetClientId_ok() {
        String clientId = "clientId";
        Account account = new Account(clientId);
        assertEquals(clientId, account.getClientId());
    }

    @Test
    public void givenNewAccount_whenGetBalanceZero() {
        String clientId = "clientId";
        Account account = new Account(clientId);
        assertEquals(money(BigDecimal.ZERO), account.getBalance());
    }

    @Test
    public void givenAccount_whenStartNextStatement_ok() {
        String clientId = "clientId";
        Account account = new Account(clientId);
        assertDoesNotThrow(account::startNextStatement);
    }
}
