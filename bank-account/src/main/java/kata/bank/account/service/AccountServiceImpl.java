package kata.bank.account.service;

import kata.bank.account.domain.Account;
import kata.bank.account.domain.Deposit;
import kata.bank.account.domain.Withdrawal;
import kata.bank.account.exception.UnknownAccountException;
import kata.bank.account.utils.MoneyHelper;
import org.javamoney.moneta.Money;

import java.math.BigDecimal;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {

    private final Repository<String, Account> accountRepository;

    public AccountServiceImpl(Repository<String, Account> accountRepository) {
        this.accountRepository = accountRepository;
    }

    private Account getAccountByClientId(String clientId) throws UnknownAccountException {
        Optional<Account> optional = accountRepository.findById(clientId);
        if (optional.isEmpty()) {
            throw new UnknownAccountException(clientId);
        }
        return optional.get();
    }

    @Override
    public void saveMoney(String clientId, BigDecimal amount) {
        Account account = getAccountByClientId(clientId);
        Deposit deposit = new Deposit(Money.of(amount, MoneyHelper.DEFAULT_CURRENCY_CODE));
        account.process(deposit);
    }

    @Override
    public void retrieveSavings(String clientId, BigDecimal amount) {
        Account account = getAccountByClientId(clientId);
        Withdrawal withdrawal = new Withdrawal(Money.of(amount, MoneyHelper.DEFAULT_CURRENCY_CODE));
        account.process(withdrawal);
    }

    @Override
    public String printAccountStatement(String clientId) {
        Account account = getAccountByClientId(clientId);
        return account.printCurrentStatement();
    }

    @Override
    public void startNextStatement(String clientId) {
        Account account = getAccountByClientId(clientId);
        account.startNextStatement();
    }
}
