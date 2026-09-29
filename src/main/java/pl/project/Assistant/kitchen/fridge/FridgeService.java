package pl.project.Assistant.kitchen.fridge;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.project.Assistant.auth.CurrentUserProvider;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.exception.ConflictException;
import pl.project.Assistant.exception.ResourceNotFoundException;
import pl.project.Assistant.kitchen.fridge.dto.FridgeItemMapper;
import pl.project.Assistant.kitchen.fridge.dto.FridgeItemRequest;
import pl.project.Assistant.kitchen.fridge.dto.FridgeItemResponse;
import pl.project.Assistant.kitchen.product.Product;
import pl.project.Assistant.kitchen.product.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FridgeService {

    private final FridgeRepository fridgeRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ProductService productService;

    public FridgeService(FridgeRepository fridgeRepository, CurrentUserProvider currentUserProvider, ProductService productService){
        this.fridgeRepository = fridgeRepository;
        this.currentUserProvider = currentUserProvider;
        this.productService = productService;

    }
    @Transactional
    public FridgeItemResponse addItemToFridge(FridgeItemRequest request){
        User user = currentUserProvider.getCurrentUser();
        Product product = productService.getOwnedProduct(request.getProductId(),user);

        FridgeItem item = fridgeRepository.findByOwnerAndProduct(user,product).orElse(null);
        if(item!=null){
            item.setAmount(item.getAmount().add(request.getAmount()));
        } else {
            item = new FridgeItem();
            item.setProduct(product);
            item.setAmount(request.getAmount());
            item.setOwner(user);
        }
        return FridgeItemMapper.toResponse(fridgeRepository.save(item));



    }
    @Transactional(readOnly = true)
    public List<FridgeItemResponse> getItemsInFridge(){
        User user = currentUserProvider.getCurrentUser();
        return fridgeRepository.findAllByOwnerOrderByProductNameAsc(user).stream()
                .map(FridgeItemMapper::toResponse)
                .collect(Collectors.toList());

    }
    @Transactional
    public FridgeItemResponse updateItemInFridge(Long id,FridgeItemRequest request){
        User user = currentUserProvider.getCurrentUser();
        FridgeItem fridgeItem = fridgeRepository.findByIdAndOwner(id,user).orElseThrow(()-> new ResourceNotFoundException("Item",id));

        Product product = productService.getOwnedProduct(request.getProductId(), user);

        fridgeRepository.findByOwnerAndProduct(user,product)
                .filter(other -> !other.getId().equals(fridgeItem.getId()))
                .ifPresent(other -> { throw new ConflictException("Product is already in the fridge"); });

        fridgeItem.setProduct(product);
        fridgeItem.setAmount(request.getAmount());


        return FridgeItemMapper.toResponse(fridgeRepository.save(fridgeItem));
    }
    @Transactional
    public void deleteItemFromFridge(Long id){
        User user = currentUserProvider.getCurrentUser();
        FridgeItem fridgeItem = fridgeRepository.findByIdAndOwner(id,user).orElseThrow(()-> new ResourceNotFoundException("Item",id));
        fridgeRepository.delete(fridgeItem);

    }




}
