package pl.project.Assistant.kitchen.fridge.dto;

import pl.project.Assistant.kitchen.fridge.FridgeItem;

public class FridgeItemMapper {

    public static FridgeItemResponse toResponse(FridgeItem item){
        FridgeItemResponse fridgeItemResponse = new FridgeItemResponse();
        fridgeItemResponse.setId(item.getId());
        fridgeItemResponse.setProductId(item.getProduct().getId());
        fridgeItemResponse.setProductName(item.getProduct().getName());
        fridgeItemResponse.setAmount(item.getAmount());
        fridgeItemResponse.setUnit(item.getProduct().getUnit());

        return fridgeItemResponse;

    }
}
