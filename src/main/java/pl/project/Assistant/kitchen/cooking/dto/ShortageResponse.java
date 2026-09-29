package pl.project.Assistant.kitchen.cooking.dto;

import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.product.Unit;

import java.math.BigDecimal;

@Getter
@Setter
public class ShortageResponse {

    private String productName;
    private Unit unit;
    private BigDecimal missingAmount;
}
