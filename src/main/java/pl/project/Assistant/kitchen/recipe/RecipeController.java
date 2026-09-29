package pl.project.Assistant.kitchen.recipe;

import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.project.Assistant.kitchen.recipe.dto.RecipeRequest;
import pl.project.Assistant.kitchen.recipe.dto.RecipeResponse;
import pl.project.Assistant.kitchen.recipe.dto.RecipeSummaryResponse;

@RestController
@RequestMapping("/api/v1/kitchen/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeResponse addRecipe(@Valid @RequestBody RecipeRequest request) {
        return recipeService.addRecipe(request);
    }

    @GetMapping
    public Page<RecipeSummaryResponse> getRecipes(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer maxPrepTimeMinutes,
            @RequestParam(required = false) RecipeAvailability availability,
            @ParameterObject @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return recipeService.getRecipes(name, maxPrepTimeMinutes, availability, pageable);
    }

    @GetMapping("/{id}")
    public RecipeResponse getRecipe(@PathVariable Long id) {
        return recipeService.getRecipe(id);
    }

    @PutMapping("/{id}")
    public RecipeResponse updateRecipe(@PathVariable Long id, @Valid @RequestBody RecipeRequest request) {
        return recipeService.updateRecipe(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
    }
}