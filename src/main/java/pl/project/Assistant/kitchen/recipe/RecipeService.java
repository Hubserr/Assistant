package pl.project.Assistant.kitchen.recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.project.Assistant.auth.CurrentUserProvider;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.exception.BadRequestException;
import pl.project.Assistant.exception.ResourceNotFoundException;
import pl.project.Assistant.kitchen.dto.NutritionMapper;
import pl.project.Assistant.kitchen.fridge.FridgeService;
import pl.project.Assistant.kitchen.product.Product;
import pl.project.Assistant.kitchen.product.ProductService;
import pl.project.Assistant.kitchen.recipe.dto.IngredientRequest;
import pl.project.Assistant.kitchen.recipe.dto.RecipeMapper;
import pl.project.Assistant.kitchen.recipe.dto.RecipeRequest;
import pl.project.Assistant.kitchen.recipe.dto.RecipeResponse;
import pl.project.Assistant.kitchen.recipe.dto.RecipeSummaryResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ProductService productService;
    private final FridgeService fridgeService;

    public RecipeService(RecipeRepository recipeRepository,
                         CurrentUserProvider currentUserProvider,
                         ProductService productService,
                         FridgeService fridgeService) {
        this.recipeRepository = recipeRepository;
        this.currentUserProvider = currentUserProvider;
        this.productService = productService;
        this.fridgeService = fridgeService;
    }

    @Transactional
    public RecipeResponse addRecipe(RecipeRequest request) {
        User user = currentUserProvider.getCurrentUser();

        Recipe recipe = new Recipe();
        recipe.setOwner(user);
        applyRequest(recipe, request, user);

        Recipe saved = recipeRepository.save(recipe);
        Map<Long, BigDecimal> stock = fridgeService.getStockMap(user);
        return RecipeMapper.toResponse(saved, stock);
    }

    @Transactional(readOnly = true)
    public Page<RecipeSummaryResponse> getRecipes(String name, Integer maxPrepTimeMinutes, Pageable pageable) {
        User user = currentUserProvider.getCurrentUser();

        Specification<Recipe> spec = (root, query, cb) -> cb.equal(root.get("owner"), user);

        if (name != null) {
            String pattern = "%" + name.trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.like(cb.lower(root.get("name")), pattern));
        }
        if (maxPrepTimeMinutes != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("prepTimeMinutes"), maxPrepTimeMinutes));
        }

        Map<Long, BigDecimal> stock = fridgeService.getStockMap(user);
        return recipeRepository.findAll(spec, pageable).map(recipe -> RecipeMapper.toSummary(recipe, stock));
    }

    @Transactional(readOnly = true)
    public RecipeResponse getRecipe(Long id) {
        User user = currentUserProvider.getCurrentUser();
        Recipe recipe = findOwnedWithIngredients(id, user);
        Map<Long, BigDecimal> stock = fridgeService.getStockMap(user);
        return RecipeMapper.toResponse(recipe, stock);
    }

    @Transactional
    public RecipeResponse updateRecipe(Long id, RecipeRequest request) {
        User user = currentUserProvider.getCurrentUser();
        Recipe recipe = findOwnedWithIngredients(id, user);

        applyRequest(recipe, request, user);

        Map<Long, BigDecimal> stock = fridgeService.getStockMap(user);
        return RecipeMapper.toResponse(recipe, stock);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        User user = currentUserProvider.getCurrentUser();
        Recipe recipe = getOwnedRecipe(id, user);
        recipeRepository.delete(recipe);
    }

    public Recipe getOwnedRecipe(Long id, User user) {
        return recipeRepository.findByIdAndOwner(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", id));
    }

    private Recipe findOwnedWithIngredients(Long id, User user) {
        return recipeRepository.findWithIngredientsByIdAndOwner(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", id));
    }

    private void applyRequest(Recipe recipe, RecipeRequest request, User user) {
        recipe.setName(request.getName().trim());
        recipe.setDescription(request.getDescription());
        recipe.setServings(request.getServings());
        recipe.setPrepTimeMinutes(request.getPrepTimeMinutes());
        recipe.setNutrition(NutritionMapper.toEntity(request.getNutrition()));

        recipe.getSteps().clear();
        recipe.getSteps().addAll(request.getSteps());

        new ArrayList<>(recipe.getIngredients()).forEach(recipe::deleteIngredient);

        Set<String> usedNames = new HashSet<>();
        for (IngredientRequest ingredientRequest : request.getIngredients()) {
            String productName = ingredientRequest.getProductName().trim();
            if (!usedNames.add(productName.toLowerCase())) {
                throw new BadRequestException("Product " + productName + " is listed more than once");
            }
            Product product = productService.findOrCreate(productName,ingredientRequest.getUnit(),user);
            recipe.addIngredient(new Ingredient(product, ingredientRequest.getAmount()));
        }
    }


}