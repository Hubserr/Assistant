package pl.project.Assistant.finance.transaction.dto;

import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.finance.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Getter
@Setter
public class TransactionResponse {

    private Long id;
    private String title;
    private String description;
    private BigDecimal amount;
    private LocalDate transactionDate;
    private LocalDateTime creationDate;
    private Long categoryId;
    private String categoryName;
    private TransactionType type;

}
