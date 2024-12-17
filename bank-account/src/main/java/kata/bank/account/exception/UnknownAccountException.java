package kata.bank.account.exception;

public class UnknownAccountException extends BankAccountException {
    public UnknownAccountException(String message) {
        super(message);
    }
}
