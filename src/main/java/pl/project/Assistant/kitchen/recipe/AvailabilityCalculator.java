package pl.project.Assistant.kitchen.recipe;

import java.math.BigDecimal;
import java.util.Map;

public class AvailabilityCalculator {

    private AvailabilityCalculator() {
    }

    public static IngredientAvailability statusOf(BigDecimal required, BigDecimal available) {
        if (available.compareTo(BigDecimal.ZERO) == 0) {
            return IngredientAvailability.MISSING;
        }
        if (available.compareTo(required) >= 0) {
            return IngredientAvailability.AVAILABLE;
        }
        return IngredientAvailability.INSUFFICIENT;
    }

    public static BigDecimal availableAmount(Ingredient ingredient, Map<Long, BigDecimal> stock) {
        return stock.getOrDefault(ingredient.getProduct().getId(), BigDecimal.ZERO);
    }
}