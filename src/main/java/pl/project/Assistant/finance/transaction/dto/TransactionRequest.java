package pl.project.Assistant.finance.transaction.dto;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.finance.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class TransactionRequest {

    @NotBlank
    @Size(max=100)
    private String title;
    @Size(max=255)
    private String description;
    @NotNull
    @Positive
    @Digits(integer =17, fraction = 2)
    private BigDecimal amount;
    @NotNull
    private LocalDate transactionDate;
    private Long categoryId;
    @NotNull
    private TransactionType type;




}
