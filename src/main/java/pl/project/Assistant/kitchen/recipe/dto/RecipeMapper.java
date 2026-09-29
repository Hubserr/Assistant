package pl.project.Assistant.kitchen.recipe.dto;

import pl.project.Assistant.kitchen.dto.NutritionMapper;
import pl.project.Assistant.kitchen.recipe.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class RecipeMapper {

    private RecipeMapper() {
    }

    public static RecipeResponse toResponse(Recipe recipe, Map<Long, BigDecimal> stock){
        RecipeResponse response = new RecipeResponse();

        response.setId(recipe.getId());
        response.setName(recipe.getName());
        response.setDescription(recipe.getDescription());
        response.setServings(recipe.getServings());
        response.setNutrition(NutritionMapper.toDto(recipe.getNutrition()));
        response.setPrepTimeMinutes(recipe.getPrepTimeMinutes());
        response.setIngredients(recipe.getIngredients().stream()
                .map(ingredient -> toIngredientResponse(ingredient,stock))
                .collect(Collectors.toList()));
        response.setSteps(new ArrayList<>(recipe.getSteps()));
        response.setCreatedAt(recipe.getCreatedAt());

        return response;

    }
    public static RecipeSummaryResponse toSummary(Recipe recipe, Map<Long, BigDecimal> stock) {
        RecipeSummaryResponse response = new RecipeSummaryResponse();
        response.setId(recipe.getId());
        response.setName(recipe.getName());
        response.setDescription(recipe.getDescription());
        response.setServings(recipe.getServings());
        response.setPrepTimeMinutes(recipe.getPrepTimeMinutes());
        int missing = (int) recipe.getIngredients().stream()
                .filter(ingredient -> AvailabilityCalculator.statusOf(
                        ingredient.getAmount(),
                        AvailabilityCalculator.availableAmount(ingredient, stock)) != IngredientAvailability.AVAILABLE)
                .count();
        response.setMissingIngredientsCount(missing);
        response.setAvailability(missing == 0 ? RecipeAvailability.READY : RecipeAvailability.MISSING_INGREDIENTS);
        if (recipe.getNutrition() != null) {
            response.setCalories(recipe.getNutrition().getCalories());
        }
        return response;
    }

    private static IngredientResponse toIngredientResponse(Ingredient ingredient, Map<Long, BigDecimal> stock) {
        BigDecimal available = AvailabilityCalculator.availableAmount(ingredient, stock);

        IngredientResponse response = new IngredientResponse();
        response.setProductId(ingredient.getProduct().getId());
        response.setProductName(ingredient.getProduct().getName());
        response.setUnit(ingredient.getProduct().getUnit());
        response.setAmount(ingredient.getAmount());
        response.setAvailableAmount(available);
        response.setAvailability(AvailabilityCalculator.statusOf(ingredient.getAmount(), available));
        return response;
    }


}
