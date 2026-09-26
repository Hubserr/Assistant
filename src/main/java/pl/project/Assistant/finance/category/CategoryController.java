package pl.project.Assistant.finance.category;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import pl.project.Assistant.finance.category.dto.CategoryRequest;
import pl.project.Assistant.finance.category.dto.CategoryResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/finance/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService){
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<CategoryResponse> getCategories(){
        return  categoryService.getCategories();
    }

    @PostMapping
    public CategoryResponse add(@Valid @RequestBody CategoryRequest request){
        return categoryService.addCategory(request);
    }

    @PutMapping("/{id}")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request){
        return categoryService.updateCategory(id,request);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }









}
