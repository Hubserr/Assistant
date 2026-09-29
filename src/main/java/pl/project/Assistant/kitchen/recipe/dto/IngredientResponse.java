package pl.project.Assistant.kitchen.recipe.dto;

import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.product.Unit;

import java.math.BigDecimal;

@Getter
@Setter
public class IngredientResponse {

    private Long productId;
    private String productName;
    private Unit unit;
    private BigDecimal amount;
}
