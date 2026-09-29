package pl.project.Assistant.finance.transaction;


import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.project.Assistant.finance.transaction.dto.TransactionRequest;
import pl.project.Assistant.finance.transaction.dto.TransactionResponse;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/v1/finance/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @GetMapping
    public Page<TransactionResponse> getTransactions(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false)  TransactionType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateTo,
            @ParameterObject @PageableDefault(sort = "transactionDate",direction =  Sort.Direction.DESC) Pageable pageable)
    {
        return transactionService.getTransactions(categoryId,type,dateFrom,dateTo,pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransactionResponse addTransaction(@Valid @RequestBody TransactionRequest request){
    return  transactionService.addTransaction(request);    
    }

    @PutMapping("/{id}")
    public TransactionResponse updateTransaction(@PathVariable Long id, @Valid @RequestBody TransactionRequest request){
        return  transactionService.updateTransaction(id,request);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable Long id){
        transactionService.delete(id);
    }







}
