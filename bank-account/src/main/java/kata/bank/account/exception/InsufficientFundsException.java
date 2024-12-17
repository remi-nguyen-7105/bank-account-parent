package kata.bank.account.exception;

public class InsufficientFundsException extends BankAccountException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
