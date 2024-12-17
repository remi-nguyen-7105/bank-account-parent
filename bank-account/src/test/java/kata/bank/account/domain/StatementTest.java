package kata.bank.account.domain;

import kata.bank.account.exception.InsufficientFundsException;
import kata.bank.account.utils.MoneyHelper;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class StatementTest {

    private Money money(BigDecimal amount) {
        return Money.of(amount, MoneyHelper.DEFAULT_CURRENCY_CODE);
    }

    private Money money(String amount) {
        return money(new BigDecimal(amount));
    }

    @Test
    public void whenCreate_ok() {
        assertDoesNotThrow(Statement::new);
    }

    @Test
    public void givenNewStatement_thenBalanceIsZero() {
        Statement statement = new Statement();
        assertEquals(money(BigDecimal.ZERO), statement.getBalance());
    }

    @Test
    public void givenNewStatement_thenAmountIsZero() {
        Statement statement = new Statement();
        assertEquals(money(BigDecimal.ZERO), statement.getAmount());
    }

    @Test
    public void givenNewStatement_thenClosingDateIsNull() {
        Statement statement = new Statement();
        assertNull(statement.getClosingDate());
    }

    @Test
    public void givenStatement_whenNextStatement_ok() {
        Statement statement = new Statement();
        assertDoesNotThrow(statement::nextStatement);
    }

    @Test
    public void givenStatement_whenNextStatement_NextStatementClosingDateIsNull() {
        Statement statement = new Statement();
        Statement next = statement.nextStatement();
        assertNull(next.getClosingDate());
    }

    @Test
    public void givenStatement_whenNextStatement_StatementHasClosingDate() {
        Statement statement = new Statement();
        statement.nextStatement();
        assertNotNull(statement.getClosingDate());
    }

    @Test
    public void givenStatement_whenGetDate_ok() {
        Statement statement = new Statement();
        assertDoesNotThrow(statement::getDate);
    }

    @Test
    public void givenStatement_whenNextStatement_NextStatementGetDateAfterStatementGetDate() throws InterruptedException {
        Statement statement = new Statement();
        Statement next = statement.nextStatement();

        Thread.sleep(10L); // force a short but significant enough delay
        LocalDateTime nextDate = next.getDate();
        LocalDateTime statementDate = statement.getDate();

        assertTrue(statementDate.isBefore(nextDate));
    }

    @Test
    public void givenStatement_whenAdd_thenOperationMustNotBeNull() {
        Statement statement = new Statement();
        assertThrows(NullPointerException.class, () -> statement.process(null));
    }

    @Test
    public void givenStatement_whenAddDeposit_ok() {
        Statement statement = new Statement();
        Deposit deposit = new Deposit(money(BigDecimal.ONE));

        assertDoesNotThrow(() -> statement.process(deposit));
    }

    @Test
    public void givenStatement_whenAddWithdrawal_WithSufficientFund_ok() {
        Statement statement = new Statement();
        Deposit deposit_1001 = new Deposit(money("1001.00"));
        Withdrawal withdrawal_1 = new Withdrawal(money(BigDecimal.ONE));
        Withdrawal withdrawal_1000 = new Withdrawal(money("1000.00"));

        statement.process(deposit_1001);

        assertDoesNotThrow(() -> statement.process(withdrawal_1000));
        assertDoesNotThrow(() -> statement.process(withdrawal_1));
    }

    @Test
    public void givenStatement_whenAddWithdrawal_WithoutSufficientFund_ko() {
        Statement statement = new Statement();
        Deposit deposit_1001 = new Deposit(money("1001.00"));
        Withdrawal withdrawal_10 = new Withdrawal(money(BigDecimal.TEN));
        Withdrawal withdrawal_1000 = new Withdrawal(money("1000.00"));

        statement.process(deposit_1001);

        assertDoesNotThrow(() -> statement.process(withdrawal_1000));
        assertThrows(InsufficientFundsException.class, () -> statement.process(withdrawal_10));
    }

    @Test
    public void givenStatement_afterAddingMultipleOperation_thenAmount_ok_0() {
        Statement statement = new Statement();
        Deposit deposit_1000 = new Deposit(money("1000.00"));
        Deposit deposit_1001 = new Deposit(money("1001.00"));
        Withdrawal withdrawal_10 = new Withdrawal(money(BigDecimal.TEN));
        Withdrawal withdrawal_1000 = new Withdrawal(money("1000.00"));

        statement.process(deposit_1000);
        statement.process(deposit_1001);
        statement.process(withdrawal_1000);
        statement.process(withdrawal_10);

        assertEquals(money("991.00"), statement.getAmount());
    }

    @Test
    public void givenStatement_afterAddingMultipleOperation_thenAmount_ok_1() {
        Statement statement = new Statement();
        Deposit deposit_1000 = new Deposit(money("1000.00"));
        Deposit deposit_1001 = new Deposit(money("1001.00"));
        Withdrawal withdrawal_10 = new Withdrawal(money(BigDecimal.TEN));
        Withdrawal withdrawal_1000 = new Withdrawal(money("1000.00"));

        statement.process(deposit_1000);
        statement.process(deposit_1001);
        statement.process(withdrawal_1000);
        statement.process(withdrawal_10);

        assertEquals(money("991"), statement.getAmount());

    }

    @Test
    public void givenStatementWithoutPrevious_afterAddingMultipleOperation_thenAmountEqualBalance() {
        Statement statement = new Statement();
        Deposit deposit_1000 = new Deposit(money("1000.00"));
        Deposit deposit_1001 = new Deposit(money("1001.00"));
        Withdrawal withdrawal_10 = new Withdrawal(money(BigDecimal.TEN));
        Withdrawal withdrawal_1000 = new Withdrawal(money("1000.00"));

        statement.process(deposit_1000);
        assertEquals(statement.getBalance(), statement.getAmount());
        statement.process(deposit_1001);
        assertEquals(statement.getBalance(), statement.getAmount());
        statement.process(withdrawal_1000);
        assertEquals(statement.getBalance(), statement.getAmount());
        statement.process(withdrawal_10);
        assertEquals(statement.getBalance(), statement.getAmount());
    }

    @Test
    public void givenPreviousStatement_whenAddingOperation_ko() {
        Statement previous = new Statement();
        previous.nextStatement();
        Deposit deposit = new Deposit(money("1000.00"));

        assertThrows(IllegalStateException.class, () -> previous.process(deposit));
    }

    @Test
    public void givenChainedStatements_whenAddingOperationAcrossTheChainedStatement_thenBalance_ok() {
        Statement statement = new Statement();
        Deposit deposit_1000 = new Deposit(money("1000.00"));
        Deposit deposit_1001 = new Deposit(money("1001.00"));
        statement.process(deposit_1000);
        statement.process(deposit_1001);

        Statement next = statement.nextStatement();
        Withdrawal withdrawal_10 = new Withdrawal(money(BigDecimal.TEN));
        Withdrawal withdrawal_1000 = new Withdrawal(money("1000.00"));
        next.process(withdrawal_1000);

        assertEquals(money("1001.00"), next.getBalance());
        next.process(withdrawal_10);
        assertEquals(money("991.00"), next.getBalance());
    }
}
