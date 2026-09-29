package pl.project.Assistant.kitchen.shopping.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ShoppingListItemRequest {

    @NotNull
    private Long productId;
    @NotNull
    @Positive
    private BigDecimal amount;


}
