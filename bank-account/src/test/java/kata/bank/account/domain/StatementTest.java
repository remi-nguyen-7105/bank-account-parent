package kata.bank.account.domain;

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

}
