package pl.project.Assistant.kitchen.recipe.dto;

import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.dto.NutritionDto;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class RecipeResponse {

    private Long id;
    private String name;
    private String description;
    private Integer servings;
    private Integer prepTimeMinutes;
    private NutritionDto nutrition;
    private List<IngredientResponse> ingredients;
    private List<String> steps;
    private LocalDateTime createdAt;



}
