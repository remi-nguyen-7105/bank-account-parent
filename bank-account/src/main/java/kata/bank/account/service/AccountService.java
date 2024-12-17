package kata.bank.account.service;

import java.math.BigDecimal;


public interface AccountService {
    // US 1
    void saveMoney(String clientId, BigDecimal amount);

    // US 2
    void retrieveSavings(String clientId, BigDecimal amount);

    // requirement
    String printAccountStatement(String clientId);

    void startNextStatement(String clientId);
}
