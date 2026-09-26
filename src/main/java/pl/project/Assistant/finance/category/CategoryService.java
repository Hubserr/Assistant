package pl.project.Assistant.finance.category;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.project.Assistant.auth.CurrentUserProvider;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.exception.ConflictException;
import pl.project.Assistant.exception.ResourceNotFoundException;
import pl.project.Assistant.finance.category.dto.CategoryMapper;
import pl.project.Assistant.finance.category.dto.CategoryRequest;
import pl.project.Assistant.finance.category.dto.CategoryResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CurrentUserProvider currentUserProvider;

    public CategoryService(CategoryRepository categoryRepository, CurrentUserProvider currentUserProvider){
        this.categoryRepository = categoryRepository;
        this.currentUserProvider = currentUserProvider;
    }
    @Transactional
    public CategoryResponse addCategory(CategoryRequest request){
        User user = currentUserProvider.getCurrentUser();
        if(categoryRepository.existsByOwnerAndNameIgnoreCase(user, request.getName().trim())){
            throw new ConflictException("Category already exists");
        }
        Category category = new Category();
        category.setName(request.getName().trim());
        category.setOwner(user);
        return CategoryMapper.toResponse(categoryRepository.save(category));
    }
    @Transactional(readOnly = true)
    public List<CategoryResponse> getCategories(){
        User user = currentUserProvider.getCurrentUser();
        return categoryRepository.findAllByOwnerOrderByNameAsc(user).stream()
                .map(CategoryMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Transactional
    public CategoryResponse updateCategory(Long id,CategoryRequest updatedCategory){
        User user = currentUserProvider.getCurrentUser();
        Category category = categoryRepository.findByIdAndOwner(id,user).orElseThrow(()->new ResourceNotFoundException("Category",id));
        if(categoryRepository.existsByOwnerAndNameIgnoreCaseAndIdNot(user, updatedCategory.getName().trim(), id)){
            throw new ConflictException("Category already exists");
        }


        category.setName(updatedCategory.getName().trim());
        return CategoryMapper.toResponse(categoryRepository.save(category));

    }
    @Transactional
    public void deleteCategory(Long id){
        User user = currentUserProvider.getCurrentUser();
        Category category = categoryRepository.findByIdAndOwner(id,user).orElseThrow(()->new ResourceNotFoundException("Category",id));

            categoryRepository.delete(category);

    }

    public Category getOwnedCategory(Long id, User user){
        return categoryRepository.findByIdAndOwner(id,user).orElseThrow(()-> new ResourceNotFoundException("Category",id));
    }

}
