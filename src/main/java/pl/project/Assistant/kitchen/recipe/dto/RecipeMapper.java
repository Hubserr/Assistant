package pl.project.Assistant.kitchen.recipe.dto;

import pl.project.Assistant.kitchen.dto.NutritionMapper;
import pl.project.Assistant.kitchen.recipe.Ingredient;
import pl.project.Assistant.kitchen.recipe.Recipe;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RecipeMapper {

    private RecipeMapper() {
    }

    public static RecipeResponse toResponse(Recipe recipe){
        RecipeResponse response = new RecipeResponse();

        response.setId(recipe.getId());
        response.setName(recipe.getName());
        response.setDescription(recipe.getDescription());
        response.setServings(recipe.getServings());
        response.setNutrition(NutritionMapper.toDto(recipe.getNutrition()));
        response.setPrepTimeMinutes(recipe.getPrepTimeMinutes());
        response.setIngredients(recipe.getIngredients().stream()
                .map(RecipeMapper::toIngredientResponse)
                .collect(Collectors.toList()));
        response.setSteps(new ArrayList<>(recipe.getSteps()));
        response.setCreatedAt(recipe.getCreatedAt());

        return response;

    }
    public static RecipeSummaryResponse toSummary(Recipe recipe) {
        RecipeSummaryResponse response = new RecipeSummaryResponse();
        response.setId(recipe.getId());
        response.setName(recipe.getName());
        response.setDescription(recipe.getDescription());
        response.setServings(recipe.getServings());
        response.setPrepTimeMinutes(recipe.getPrepTimeMinutes());
        if (recipe.getNutrition() != null) {
            response.setCalories(recipe.getNutrition().getCalories());
        }
        return response;
    }

    private static IngredientResponse toIngredientResponse(Ingredient ingredient) {
        IngredientResponse response = new IngredientResponse();
        response.setProductId(ingredient.getProduct().getId());
        response.setProductName(ingredient.getProduct().getName());
        response.setUnit(ingredient.getProduct().getUnit());
        response.setAmount(ingredient.getAmount());
        return response;
    }


}
