package kata.bank.account.service;

import kata.bank.account.domain.Client;
import kata.bank.account.domain.Deposit;
import kata.bank.account.domain.Withdrawal;


public interface AccountService {
    // US 1
    void saveMoney(Client client, Deposit deposit);

    // US 2
    void retrieveSavings(Client client, Withdrawal withdrawal);

    // requirement
    String printAccountStatement(Client client);

}
