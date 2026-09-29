package pl.project.Assistant.kitchen.recipe.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class IngredientRequest {


    @NotNull
    private Long productId;

    @NotNull
    @Positive
    @Digits(integer = 8, fraction = 2)
    private BigDecimal amount;
}
