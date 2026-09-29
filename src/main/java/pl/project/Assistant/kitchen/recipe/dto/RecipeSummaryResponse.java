package pl.project.Assistant.kitchen.recipe.dto;

import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.recipe.RecipeAvailability;

@Getter
@Setter
public class RecipeSummaryResponse {
    private Long id;
    private String name;
    private String description;
    private Integer servings;
    private Integer prepTimeMinutes;
    private Integer calories;
    private RecipeAvailability availability;
    private int missingIngredientsCount;
}