package pl.project.Assistant.kitchen.shopping.dto;


import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.product.Unit;

import java.math.BigDecimal;

@Getter
@Setter
public class ShoppingListItemResponse {

    private Long id;
    private Long productId;
    private String productName;

    private Unit unit;
    private BigDecimal amount;




}
