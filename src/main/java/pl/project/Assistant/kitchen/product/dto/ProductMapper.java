package pl.project.Assistant.kitchen.product.dto;

import pl.project.Assistant.kitchen.product.Product;


public class ProductMapper {

    public static ProductResponse toResponse(Product product){
        ProductResponse response = new ProductResponse();

        response.setId(product.getId());
        response.setProductName(product.getName());
        response.setUnit(product.getUnit());

        return response;


    }


}
