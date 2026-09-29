package pl.project.Assistant.kitchen.product.dto;


import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.kitchen.product.Unit;

@Getter
@Setter
public class ProductResponse {

    private Long id;
    private String productName;
    private Unit unit;


}
