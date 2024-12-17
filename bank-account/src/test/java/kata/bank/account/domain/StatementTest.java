package kata.bank.account.domain;

import kata.bank.account.exception.InsufficientFundsException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class StatementTest {

    @Test
    public void whenCreate_ok() {
        assertDoesNotThrow(Statement::new);
    }

    @Test
    public void givenNewStatement_thenBalanceIsZero() {
        Statement statement = new Statement();
        assertEquals(BigDecimal.ZERO, statement.getBalance());
    }

    @Test
    public void givenNewStatement_thenAmountIsZero() {
        Statement statement = new Statement();
        assertEquals(BigDecimal.ZERO, statement.getAmount());
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
        assertThrows(NullPointerException.class, () -> statement.add(null));
    }

    @Test
    public void givenStatement_whenAddDeposit_ok() {
        Statement statement = new Statement();
        Deposit deposit = new Deposit(BigDecimal.ONE);

        assertDoesNotThrow(() -> statement.add(deposit));
    }

    @Test
    public void givenStatement_whenAddWithdrawal_WithSufficientFund_ok() {
        Statement statement = new Statement();
        Deposit deposit_1001 = new Deposit(new BigDecimal("1001.00"));
        Withdrawal withdrawal_1 = new Withdrawal(BigDecimal.ONE);
        Withdrawal withdrawal_1000 = new Withdrawal(new BigDecimal("1000.00"));

        statement.add(deposit_1001);

        assertDoesNotThrow(() -> statement.add(withdrawal_1000));
        assertDoesNotThrow(() -> statement.add(withdrawal_1));
    }

    @Test
    public void givenStatement_whenAddWithdrawal_WithoutSufficientFund_ko() {
        Statement statement = new Statement();
        Deposit deposit_1001 = new Deposit(new BigDecimal("1001.00"));
        Withdrawal withdrawal_10 = new Withdrawal(BigDecimal.TEN);
        Withdrawal withdrawal_1000 = new Withdrawal(new BigDecimal("1000.00"));

        statement.add(deposit_1001);

        assertDoesNotThrow(() -> statement.add(withdrawal_1000));
        assertThrows(InsufficientFundsException.class, () -> statement.add(withdrawal_10));
    }

    @Test
    public void givenStatement_afterAddingMultipleOperation_thenAmount_ok_0() {
        Statement statement = new Statement();
        Deposit deposit_1000 = new Deposit(new BigDecimal("1000.00"));
        Deposit deposit_1001 = new Deposit(new BigDecimal("1001.00"));
        Withdrawal withdrawal_10 = new Withdrawal(BigDecimal.TEN);
        Withdrawal withdrawal_1000 = new Withdrawal(new BigDecimal("1000.00"));

        statement.add(deposit_1000);
        statement.add(deposit_1001);
        statement.add(withdrawal_1000);
        statement.add(withdrawal_10);

        assertEquals(new BigDecimal("991.00"), statement.getAmount());
    }

    /* rounding problem
    @Test
    public void givenStatement_afterAddingMultipleOperation_thenAmount_ok_1() {
        Statement statement = new Statement();
        Deposit deposit_1000 = new Deposit(new BigDecimal("1000.00"));
        Deposit deposit_1001 = new Deposit(new BigDecimal("1001.00"));
        Withdrawal withdrawal_10 = new Withdrawal(BigDecimal.TEN);
        Withdrawal withdrawal_1000 = new Withdrawal(new BigDecimal("1000.00"));

        statement.add(deposit_1000);
        statement.add(deposit_1001);
        statement.add(withdrawal_1000);
        statement.add(withdrawal_10);

        assertEquals(new BigDecimal("991"), statement.getAmount());

    }
    */

    @Test
    public void givenStatementWithoutPrevious_afterAddingMultipleOperation_thenAmountEqualBalance() {
        Statement statement = new Statement();
        Deposit deposit_1000 = new Deposit(new BigDecimal("1000.00"));
        Deposit deposit_1001 = new Deposit(new BigDecimal("1001.00"));
        Withdrawal withdrawal_10 = new Withdrawal(BigDecimal.TEN);
        Withdrawal withdrawal_1000 = new Withdrawal(new BigDecimal("1000.00"));

        statement.add(deposit_1000);
        assertEquals(statement.getBalance(), statement.getAmount());
        statement.add(deposit_1001);
        assertEquals(statement.getBalance(), statement.getAmount());
        statement.add(withdrawal_1000);
        assertEquals(statement.getBalance(), statement.getAmount());
        statement.add(withdrawal_10);
        assertEquals(statement.getBalance(), statement.getAmount());
    }

    @Test
    public void givenPreviousStatement_whenAddingOperation_ko() {
        Statement previous = new Statement();
        previous.nextStatement();
        Deposit deposit = new Deposit(new BigDecimal("1000.00"));

        assertThrows(IllegalStateException.class, () -> previous.add(deposit));
    }

    @Test
    public void givenChainedStatements_whenAddingOperationAcrossTheChainedStatement_thenBalance_ok() {
        Statement statement = new Statement();
        Deposit deposit_1000 = new Deposit(new BigDecimal("1000.00"));
        Deposit deposit_1001 = new Deposit(new BigDecimal("1001.00"));
        statement.add(deposit_1000);
        statement.add(deposit_1001);

        Statement next = statement.nextStatement();
        Withdrawal withdrawal_10 = new Withdrawal(BigDecimal.TEN);
        Withdrawal withdrawal_1000 = new Withdrawal(new BigDecimal("1000.00"));
        next.add(withdrawal_1000);

        assertEquals(new BigDecimal("1001.00"), next.getBalance());
        next.add(withdrawal_10);
        assertEquals(new BigDecimal("991.00"), next.getBalance());
    }
}
