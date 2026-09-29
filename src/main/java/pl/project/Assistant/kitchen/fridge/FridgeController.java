package pl.project.Assistant.kitchen.fridge;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.project.Assistant.kitchen.fridge.dto.FridgeItemRequest;
import pl.project.Assistant.kitchen.fridge.dto.FridgeItemResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/kitchen/fridge")
public class FridgeController {

    private final FridgeService fridgeService;

    public FridgeController(FridgeService fridgeService){
        this.fridgeService = fridgeService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FridgeItemResponse addFridgeItem(@Valid @RequestBody FridgeItemRequest request){
        return  fridgeService.addItemToFridge(request);
    }

    @GetMapping
    public List<FridgeItemResponse> getItemsInFridge(){
        return  fridgeService.getItemsInFridge();
    }

    @PutMapping("/{id}")
    public FridgeItemResponse updateItemInFridge(@PathVariable Long id, @Valid @RequestBody FridgeItemRequest request){
        return  fridgeService.updateItemInFridge(id,request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItemFromFridge(@PathVariable Long id){
        fridgeService.deleteItemFromFridge(id);
    }







}
