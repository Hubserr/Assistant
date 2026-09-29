package pl.project.Assistant.kitchen.fridge.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.product.Unit;

import java.math.BigDecimal;

@Getter
@Setter
public class FridgeItemRequest {

    @NotBlank
    @Size(max=100)
    private String productName;

    private Unit unit;
    @NotNull
    @Positive
    private BigDecimal amount;


}
