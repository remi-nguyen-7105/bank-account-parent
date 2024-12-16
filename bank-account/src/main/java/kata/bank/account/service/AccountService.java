package kata.bank.account.service;

import kata.bank.account.domain.Deposit;
import kata.bank.account.domain.Withdrawal;


public interface AccountService {
    // US 1
    void saveMoney(String clientId, Deposit deposit);

    // US 2
    void retrieveSavings(String clientId, Withdrawal withdrawal);

    // requirement
    String printAccountStatement(String clientId);

    void startNextStatement(String clientId);

}
