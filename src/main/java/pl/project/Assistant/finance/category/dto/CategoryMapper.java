package pl.project.Assistant.finance.category.dto;

import lombok.NoArgsConstructor;
import pl.project.Assistant.finance.category.Category;


public class CategoryMapper {


    public static CategoryResponse toResponse(Category category){
        CategoryResponse response = new CategoryResponse();

        response.setId(category.getId());
        response.setName(category.getName());

        return response;


    }

}
