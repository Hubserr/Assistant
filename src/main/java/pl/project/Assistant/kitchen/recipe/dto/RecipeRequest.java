package pl.project.Assistant.kitchen.recipe.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.dto.NutritionDto;

import java.util.List;

@Getter
@Setter
public class RecipeRequest {


    @NotBlank
    @Size(max = 150)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    @Positive
    private Integer servings;

    @Positive
    private Integer prepTimeMinutes;

    @Valid
    private NutritionDto nutrition;

    @NotEmpty
    private List<@NotBlank @Size(max = 1000) String> steps;

    @NotEmpty
    private List<@NotNull @Valid IngredientRequest> ingredients;

}
