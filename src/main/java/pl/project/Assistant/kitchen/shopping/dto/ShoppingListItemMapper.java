package pl.project.Assistant.kitchen.shopping.dto;

import pl.project.Assistant.kitchen.shopping.ShoppingListItem;

public class ShoppingListItemMapper {

    public static ShoppingListItemResponse toResponse(ShoppingListItem item){
        ShoppingListItemResponse shoppingListItemResponse= new ShoppingListItemResponse();
        shoppingListItemResponse.setId(item.getId());
        shoppingListItemResponse.setProductId(item.getProduct().getId());
        shoppingListItemResponse.setProductName(item.getProduct().getName());
        shoppingListItemResponse.setAmount(item.getAmount());
        shoppingListItemResponse.setUnit(item.getProduct().getUnit());

        return shoppingListItemResponse;

    }
}
