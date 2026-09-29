package pl.project.Assistant.kitchen.cooking;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.project.Assistant.auth.CurrentUserProvider;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.exception.ResourceNotFoundException;
import pl.project.Assistant.kitchen.cooking.dto.CookResponse;
import pl.project.Assistant.kitchen.cooking.dto.ShortageResponse;
import pl.project.Assistant.kitchen.fridge.FridgeService;
import pl.project.Assistant.kitchen.recipe.AvailabilityCalculator;
import pl.project.Assistant.kitchen.recipe.Ingredient;
import pl.project.Assistant.kitchen.recipe.IngredientAvailability;
import pl.project.Assistant.kitchen.recipe.Recipe;
import pl.project.Assistant.kitchen.recipe.RecipeRepository;
import pl.project.Assistant.kitchen.shopping.ShoppingListService;
import pl.project.Assistant.kitchen.shopping.dto.ShoppingListItemResponse;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CookingService {

    private final RecipeRepository recipeRepository;
    private final FridgeService fridgeService;
    private final ShoppingListService shoppingListService;
    private final CurrentUserProvider currentUserProvider;

    public CookingService(RecipeRepository recipeRepository,
                          FridgeService fridgeService,
                          ShoppingListService shoppingListService,
                          CurrentUserProvider currentUserProvider) {
        this.recipeRepository = recipeRepository;
        this.fridgeService = fridgeService;
        this.shoppingListService = shoppingListService;
        this.currentUserProvider = currentUserProvider;
    }

    @Transactional
    public List<ShoppingListItemResponse> addMissingToShoppingList(Long recipeId) {
        User user = currentUserProvider.getCurrentUser();
        Recipe recipe = findOwnedWithIngredients(recipeId, user);
        Map<Long, BigDecimal> stock = fridgeService.getStockMap(user);

        for (Ingredient ingredient : recipe.getIngredients()) {
            BigDecimal available = AvailabilityCalculator.availableAmount(ingredient, stock);
            IngredientAvailability status = AvailabilityCalculator.statusOf(ingredient.getAmount(), available);
            if (status == IngredientAvailability.INSUFFICIENT || status == IngredientAvailability.MISSING) {
                shoppingListService.addOrIncrease(ingredient.getProduct(), ingredient.getAmount().subtract(available), user);
            }
        }

        return shoppingListService.getShoppingList();
    }

    @Transactional
    public CookResponse cook(Long recipeId) {
        User user = currentUserProvider.getCurrentUser();
        Recipe recipe = findOwnedWithIngredients(recipeId, user);

        List<ShortageResponse> shortages = new ArrayList<>();
        for (Ingredient ingredient : recipe.getIngredients()) {
            BigDecimal consumed = fridgeService.consume(ingredient.getProduct(), ingredient.getAmount(), user);
            if (consumed.compareTo(ingredient.getAmount()) < 0) {
                ShortageResponse shortage = new ShortageResponse();
                shortage.setProductName(ingredient.getProduct().getName());
                shortage.setUnit(ingredient.getProduct().getUnit());
                shortage.setMissingAmount(ingredient.getAmount().subtract(consumed));
                shortages.add(shortage);
            }
        }

        CookResponse response = new CookResponse();
        response.setFridge(fridgeService.getItemsInFridge());
        response.setShortages(shortages);
        return response;
    }

    private Recipe findOwnedWithIngredients(Long id, User user) {
        return recipeRepository.findWithIngredientsByIdAndOwner(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe", id));
    }
}
