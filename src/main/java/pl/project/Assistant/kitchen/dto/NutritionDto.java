package pl.project.Assistant.kitchen.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class NutritionDto {

    @PositiveOrZero
    private Integer calories;

    @PositiveOrZero
    private BigDecimal protein;

    @PositiveOrZero
    private BigDecimal fat;

    @PositiveOrZero
    private BigDecimal carbs;
}