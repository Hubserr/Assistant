package pl.project.Assistant.kitchen.recipe.dto;

import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.product.Unit;
import pl.project.Assistant.kitchen.recipe.IngredientAvailability;

import java.math.BigDecimal;

@Getter
@Setter
public class IngredientResponse {

    private Long productId;
    private String productName;
    private IngredientAvailability availability;
    private BigDecimal availableAmount;
    private Unit unit;
    private BigDecimal amount;
}
