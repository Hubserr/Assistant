package pl.project.Assistant.kitchen.cooking;

import org.springframework.web.bind.annotation.*;
import pl.project.Assistant.kitchen.cooking.dto.CookResponse;
import pl.project.Assistant.kitchen.shopping.dto.ShoppingListItemResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kitchen/recipes")
public class CookingController {

    private final CookingService cookingService;

    public CookingController(CookingService cookingService) {
        this.cookingService = cookingService;
    }

    @PostMapping("/{id}/missing-to-shopping-list")
    public List<ShoppingListItemResponse> addMissingToShoppingList(@PathVariable Long id) {
        return cookingService.addMissingToShoppingList(id);
    }

    @PostMapping("/{id}/cook")
    public CookResponse cook(@PathVariable Long id) {
        return cookingService.cook(id);
    }
}
