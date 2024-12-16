package kata.bank.account.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface Statement {

    LocalDateTime getDate();

    BigDecimal getAmount();

    BigDecimal getBalance();
}
