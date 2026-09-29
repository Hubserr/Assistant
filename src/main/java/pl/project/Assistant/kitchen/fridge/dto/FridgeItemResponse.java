package pl.project.Assistant.kitchen.fridge.dto;


import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.product.Unit;

import java.math.BigDecimal;

@Getter
@Setter
public class FridgeItemResponse {

    private Long id;
    private Long productId;
    private String productName;
    private Unit unit;

    private BigDecimal amount;




}
