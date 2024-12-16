package kata.bank.account.domain;

public sealed interface Operation permits Deposit, Withdrawal {
}
