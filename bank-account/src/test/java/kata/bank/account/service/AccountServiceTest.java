package kata.bank.account.service;

import kata.bank.account.domain.Account;
import kata.bank.account.domain.Deposit;
import kata.bank.account.exception.UnknownAccountException;
import kata.bank.account.utils.MoneyHelper;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    Repository<String, Account> accountRepository;

    private Money money(String value) {
        return money(new BigDecimal(value));
    }

    private Money money(BigDecimal value) {
        return Money.of(value, MoneyHelper.EUR_CURRENCY_CODE);
    }

    @Test
    public void givenNonExistingAccountId_whenPrintStatement_ko() {
        String clientId = "clientId";
        when(accountRepository.findById(clientId)).thenReturn(Optional.empty());
        AccountService accountService = new AccountServiceImpl(accountRepository);

        assertThrows(UnknownAccountException.class, () -> accountService.printAccountStatement(clientId));
    }

    @Test
    public void givenExistingAccountId_whenPrintStatement_ok() {
        String clientId = "clientId";
        Account account = new Account(clientId);
        when(accountRepository.findById(clientId)).thenReturn(Optional.of(account));
        AccountService accountService = new AccountServiceImpl(accountRepository);

        String result = accountService.printAccountStatement(clientId);
        assertTrue(result.endsWith("  amount :  EUR 0 balance : EUR 0"));
    }

    @Test
    public void givenNonExistingAccount_whenSavingMoney_ko() {
        String clientId = "clientId";
        when(accountRepository.findById(clientId)).thenReturn(Optional.empty());
        BigDecimal amount = new BigDecimal("1000.00");
        AccountService accountService = new AccountServiceImpl(accountRepository);

        assertThrows(UnknownAccountException.class, () -> accountService.saveMoney(clientId, amount));
    }

    @Test
    public void givenExistingAccount_whenSavingMoney_Ok() {
        String clientId = "clientId";
        Account account = new Account(clientId);
        BigDecimal amount = new BigDecimal("1000.00");
        when(accountRepository.findById(account.getClientId())).thenReturn(Optional.of(account));
        AccountService accountService = new AccountServiceImpl(accountRepository);

        accountService.saveMoney(account.getClientId(), amount);

        assertEquals(money(amount), account.getBalance());
    }

    @Test
    public void givenNonExistingAccount_whenRetrieveSaving_ko() {
        String clientId = "clientId";
        BigDecimal amount = new BigDecimal("1000.00");
        when(accountRepository.findById(clientId)).thenReturn(Optional.empty());

        AccountService accountService = new AccountServiceImpl(accountRepository);
        assertThrows(UnknownAccountException.class, () -> accountService.retrieveSavings(clientId, amount));
    }

    @Test
    public void givenExistingAccount_whenRetrieveSaving_ok() {
        String clientId = "clientId";
        Account account = new Account(clientId);
        Deposit deposit = new Deposit(money("1001.00"));
        account.process(deposit);
        BigDecimal value = new BigDecimal("1000.00");
        when(accountRepository.findById(clientId)).thenReturn(Optional.of(account));

        AccountService accountService = new AccountServiceImpl(accountRepository);

        accountService.retrieveSavings(clientId, value);

        assertEquals(money("1"), account.getBalance());
    }

    @Test
    public void givenExistingAccount_whenNextStatement_Ok() {
        Account account = new Account("clientId");
        Deposit deposit = new Deposit(money("1000.00"));
        account.process(deposit);

        when(accountRepository.findById(account.getClientId())).thenReturn(Optional.of(account));

        AccountService accountService = new AccountServiceImpl(accountRepository);

        assertEquals(money("1000"), account.getBalance());
        accountService.startNextStatement(account.getClientId());
        assertEquals(money("1000"), account.getBalance());
    }
}
