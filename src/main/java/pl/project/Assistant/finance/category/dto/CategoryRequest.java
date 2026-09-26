package pl.project.Assistant.finance.category.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {


    @NotBlank(message = "Category name can't be blank")
    @Size(max=100)
    private String name;




}
