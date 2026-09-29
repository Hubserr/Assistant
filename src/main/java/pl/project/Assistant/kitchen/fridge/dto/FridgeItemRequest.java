package pl.project.Assistant.kitchen.fridge.dto;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FridgeItemRequest {

    @NotNull
    private Long productId;
    @NotNull
    @Positive
    private BigDecimal amount;


}
