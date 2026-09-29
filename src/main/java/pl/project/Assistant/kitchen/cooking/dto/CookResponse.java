package pl.project.Assistant.kitchen.cooking.dto;

import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.fridge.dto.FridgeItemResponse;

import java.util.List;

@Getter
@Setter
public class CookResponse {

    private List<FridgeItemResponse> fridge;
    private List<ShortageResponse> shortages;
}
