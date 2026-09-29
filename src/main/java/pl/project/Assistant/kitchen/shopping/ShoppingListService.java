package pl.project.Assistant.kitchen.shopping;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.project.Assistant.auth.CurrentUserProvider;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.exception.ConflictException;
import pl.project.Assistant.exception.ResourceNotFoundException;
import pl.project.Assistant.kitchen.product.Product;
import pl.project.Assistant.kitchen.product.ProductService;
import pl.project.Assistant.kitchen.shopping.dto.ShoppingListItemMapper;
import pl.project.Assistant.kitchen.shopping.dto.ShoppingListItemRequest;
import pl.project.Assistant.kitchen.shopping.dto.ShoppingListItemResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ProductService productService;

    public ShoppingListService(ShoppingListRepository shoppingListRepository, CurrentUserProvider currentUserProvider, ProductService productService){
        this.shoppingListRepository = shoppingListRepository;
        this.currentUserProvider = currentUserProvider;
        this.productService = productService;

    }

    @Transactional
    public ShoppingListItemResponse addItemToShoppingList(ShoppingListItemRequest request){
        User user = currentUserProvider.getCurrentUser();
        Product product = productService.getOwnedProduct(request.getProductId(), user);

        ShoppingListItem item = shoppingListRepository.findByOwnerAndProduct(user,product).orElse(null);
        if(item!=null){
            item.setAmount(item.getAmount().add(request.getAmount()));
        } else {
            item = new ShoppingListItem();
            item.setProduct(product);
            item.setAmount(request.getAmount());
            item.setOwner(user);
        }
        return ShoppingListItemMapper.toResponse(shoppingListRepository.save(item));



    }

    @Transactional(readOnly = true)
    public List<ShoppingListItemResponse> getShoppingList(){
        User user = currentUserProvider.getCurrentUser();
        return shoppingListRepository.findAllByOwnerOrderByProductNameAsc(user).stream()
                .map(ShoppingListItemMapper::toResponse)
                .collect(Collectors.toList());

    }
    @Transactional
    public ShoppingListItemResponse updateItemInShoppingList(Long id, ShoppingListItemRequest request){
        User user = currentUserProvider.getCurrentUser();
        ShoppingListItem item = shoppingListRepository.findByIdAndOwner(id,user).orElseThrow(()-> new ResourceNotFoundException("Item",id));
        Product product = productService.getOwnedProduct(request.getProductId(), user);

        shoppingListRepository.findByOwnerAndProduct(user,product)
                .filter(other -> !other.getId().equals(item.getId()))
                .ifPresent(other -> { throw new ConflictException("Product is already on the shopping list"); });

        item.setProduct(product);
        item.setAmount(request.getAmount());

        return ShoppingListItemMapper.toResponse(shoppingListRepository.save(item));
    }
    @Transactional
    public void deleteItemFromShoppingList(Long id){
        User user = currentUserProvider.getCurrentUser();
        ShoppingListItem item = shoppingListRepository.findByIdAndOwner(id,user).orElseThrow(()-> new ResourceNotFoundException("Item",id));
        shoppingListRepository.delete(item);

    }




}
