package pl.project.Assistant.kitchen.shopping;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.project.Assistant.kitchen.fridge.dto.FridgeItemResponse;
import pl.project.Assistant.kitchen.shopping.dto.CheckoutRequest;
import pl.project.Assistant.kitchen.shopping.dto.ShoppingListItemRequest;
import pl.project.Assistant.kitchen.shopping.dto.ShoppingListItemResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kitchen/shopping-list")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService){
        this.shoppingListService = shoppingListService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingListItemResponse addItemToShoppingList(@Valid @RequestBody ShoppingListItemRequest request){
        return  shoppingListService.addItemToShoppingList(request);
    }

    @GetMapping
    public List<ShoppingListItemResponse> getItemsFromShoppingList(){
        return  shoppingListService.getShoppingList();
    }

    @PostMapping("/checkout")
    public List<FridgeItemResponse> checkout(@Valid @RequestBody CheckoutRequest request){
        return  shoppingListService.checkout(request);
    }

    @PutMapping("/{id}")
    public ShoppingListItemResponse updateItemInShoppingList(@PathVariable Long id, @Valid @RequestBody ShoppingListItemRequest request){
        return  shoppingListService.updateItemInShoppingList(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemFromShoppingList(@PathVariable Long id){
        shoppingListService.deleteItemFromShoppingList(id);
    }







}
