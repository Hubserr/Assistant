package pl.project.Assistant.kitchen.shopping;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.project.Assistant.auth.CurrentUserProvider;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.exception.BadRequestException;
import pl.project.Assistant.exception.ConflictException;
import pl.project.Assistant.exception.ResourceNotFoundException;
import pl.project.Assistant.kitchen.fridge.FridgeService;
import pl.project.Assistant.kitchen.fridge.dto.FridgeItemResponse;
import pl.project.Assistant.kitchen.product.Product;
import pl.project.Assistant.kitchen.product.ProductService;
import pl.project.Assistant.kitchen.shopping.dto.CheckoutItemRequest;
import pl.project.Assistant.kitchen.shopping.dto.CheckoutRequest;
import pl.project.Assistant.kitchen.shopping.dto.ShoppingListItemMapper;
import pl.project.Assistant.kitchen.shopping.dto.ShoppingListItemRequest;
import pl.project.Assistant.kitchen.shopping.dto.ShoppingListItemResponse;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ProductService productService;
    private final FridgeService fridgeService;

    public ShoppingListService(ShoppingListRepository shoppingListRepository, CurrentUserProvider currentUserProvider, ProductService productService, FridgeService fridgeService){
        this.shoppingListRepository = shoppingListRepository;
        this.currentUserProvider = currentUserProvider;
        this.productService = productService;
        this.fridgeService = fridgeService;

    }

    @Transactional
    public ShoppingListItemResponse addItemToShoppingList(ShoppingListItemRequest request){
        User user = currentUserProvider.getCurrentUser();
        Product product = productService.findOrCreate(request.getProductName(),request.getUnit(),user);

        return ShoppingListItemMapper.toResponse(addOrIncrease(product,request.getAmount(),user));



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
        Product product = productService.findOrCreate(request.getProductName(),request.getUnit(),user);

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
    @Transactional
    public List<FridgeItemResponse> checkout(CheckoutRequest request){
        User user = currentUserProvider.getCurrentUser();

        Set<Long> usedIds = new HashSet<>();
        for (CheckoutItemRequest itemRequest : request.getItems()) {
            if (!usedIds.add(itemRequest.getId())) {
                throw new BadRequestException("Item " + itemRequest.getId() + " is listed more than once");
            }
        }

        for (CheckoutItemRequest itemRequest : request.getItems()) {
            ShoppingListItem item = shoppingListRepository.findByIdAndOwner(itemRequest.getId(),user)
                    .orElseThrow(()-> new ResourceNotFoundException("Item",itemRequest.getId()));
            BigDecimal amount = itemRequest.getAmount() != null ? itemRequest.getAmount() : item.getAmount();
            fridgeService.addOrIncrease(item.getProduct(),amount,user);
            shoppingListRepository.delete(item);
        }

        return fridgeService.getItemsInFridge();
    }
    @Transactional
    public ShoppingListItem addOrIncrease(Product product, BigDecimal amount, User user){

        ShoppingListItem item = shoppingListRepository.findByOwnerAndProduct(user,product).orElse(null);

        if(item!=null){
            item.setAmount(item.getAmount().add(amount));
            return  item;
        }
        item = new ShoppingListItem();
        item.setProduct(product);
        item.setAmount(amount);
        item.setOwner(user);
        return  shoppingListRepository.save(item);
    }



}
