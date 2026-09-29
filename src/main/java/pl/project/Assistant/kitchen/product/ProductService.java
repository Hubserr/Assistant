package pl.project.Assistant.kitchen.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.project.Assistant.auth.CurrentUserProvider;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.exception.ConflictException;
import pl.project.Assistant.exception.ResourceNotFoundException;
import pl.project.Assistant.kitchen.product.dto.ProductMapper;
import pl.project.Assistant.kitchen.product.dto.ProductRequest;
import pl.project.Assistant.kitchen.product.dto.ProductResponse;
import pl.project.Assistant.kitchen.recipe.RecipeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CurrentUserProvider currentUserProvider;
    private final RecipeRepository recipeRepository;
    public ProductService(ProductRepository productRepository, CurrentUserProvider currentUserProvider,RecipeRepository recipeRepository){
        this.productRepository = productRepository;
        this.currentUserProvider = currentUserProvider;
        this.recipeRepository = recipeRepository;

    }

    @Transactional
    public ProductResponse addProduct(ProductRequest request){
        User user = currentUserProvider.getCurrentUser();
        String name = request.getProductName().trim();
        if(productRepository.existsByOwnerAndNameIgnoreCase(user, name)){
            throw new ConflictException("Product already exists");
        }
        Product product = new Product();
        product.setName(name);
        product.setUnit(request.getUnit());
        product.setOwner(user);

        return ProductMapper.toResponse(productRepository.save(product));

    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProducts(){
        User user = currentUserProvider.getCurrentUser();

        return productRepository.findAllByOwnerOrderByNameAsc(user).stream()
                .map(ProductMapper::toResponse)
                .collect(Collectors.toList());


    }
    @Transactional
    public ProductResponse updateProduct(Long id,ProductRequest request){
        User user = currentUserProvider.getCurrentUser();
        String name = request.getProductName().trim();
        if(productRepository.existsByOwnerAndNameIgnoreCaseAndIdNot(user, name, id)){
            throw new ConflictException("Product already exists");
        }
        Product product = productRepository.findByIdAndOwner(id,user).orElseThrow(()->new ResourceNotFoundException("Product",id));
        product.setName(name);
        product.setUnit(request.getUnit());
        return ProductMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id){
        User user = currentUserProvider.getCurrentUser();
        Product product = productRepository.findByIdAndOwner(id,user).orElseThrow(()->new ResourceNotFoundException("Product",id));

        if(recipeRepository.existsByIngredientsProduct(product)){
            throw new ConflictException(("Product is used in recipes"));

        }
        productRepository.delete(product);

    }

    public Product getOwnedProduct(Long id, User user){
        return productRepository.findByIdAndOwner(id,user).orElseThrow(()-> new ResourceNotFoundException("Product",id));
    }







}
