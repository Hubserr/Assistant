package pl.project.Assistant.kitchen.recipe.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.product.Unit;

import java.math.BigDecimal;

@Getter
@Setter
public class IngredientRequest {


    @NotBlank
    @Size(max=100)
    private String productName;

    private Unit unit;

    @NotNull
    @Positive
    @Digits(integer = 8, fraction = 2)
    private BigDecimal amount;
}
