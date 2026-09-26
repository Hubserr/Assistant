package pl.project.Assistant.finance.transaction;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.project.Assistant.auth.CurrentUserProvider;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.exception.ResourceNotFoundException;
import pl.project.Assistant.finance.category.Category;
import pl.project.Assistant.finance.category.CategoryService;
import pl.project.Assistant.finance.transaction.dto.TransactionMapper;
import pl.project.Assistant.finance.transaction.dto.TransactionRequest;
import pl.project.Assistant.finance.transaction.dto.TransactionResponse;

import java.time.LocalDate;

@Service
public class TransactionService {
    private final TransactionRepository repository;
    private final CurrentUserProvider currentUserProvider;
    private final CategoryService categoryService;


    public TransactionService(TransactionRepository repository, CurrentUserProvider currentUserProvider, CategoryService categoryService){
        this.repository = repository;
        this.currentUserProvider = currentUserProvider;
        this.categoryService = categoryService;

    }

    @Transactional
    public TransactionResponse addTransaction(TransactionRequest request){
        User user = currentUserProvider.getCurrentUser();
        Transaction transaction = new Transaction();

        transaction.setOwner(user);

        transaction.setTitle(request.getTitle());
        transaction.setAmount(request.getAmount());
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setCategory(resolveCategory(request.getCategoryId(),user));
        transaction.setDescription(request.getDescription());
        transaction.setType(request.getType());

        return TransactionMapper.toResponse(repository.save(transaction));

    }
    @Transactional(readOnly = true)
    public Page<TransactionResponse> getTransactions(
            String title, Long categoryId, TransactionType type, LocalDate transactionDateFrom, LocalDate transactionDateTo, Pageable pageable)
    {
        User user = currentUserProvider.getCurrentUser();

        Specification<Transaction> spec =
                (root, query, cb) -> cb.equal(root.get("owner"),user);

                if(transactionDateFrom!=null){
                    spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("transactionDate"),transactionDateFrom));
                }
                if(transactionDateTo!=null){
                    spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("transactionDate"),transactionDateTo));
                }
                if(type!=null){
                    spec = spec.and((root, query, cb) -> cb.equal(root.get("type"),type));
                }
                if(categoryId!=null){
                    spec = spec.and((root, query, cb) -> cb.equal(root.get("category").get("id"),categoryId));
                }


                return repository.findAll(spec,pageable).map(TransactionMapper::toResponse);

    }

    @Transactional
    public TransactionResponse updateTransaction(Long id,TransactionRequest request){
        User user = currentUserProvider.getCurrentUser();
        Transaction transaction = repository.findByIdAndOwner(id,user).orElseThrow(()->new ResourceNotFoundException("Transaction",id));


        transaction.setTitle(request.getTitle());
        transaction.setAmount(request.getAmount());
        transaction.setCategory(resolveCategory(request.getCategoryId(),user));
        transaction.setTransactionDate(request.getTransactionDate());
        transaction.setDescription(request.getDescription());
        transaction.setType(request.getType());

        return TransactionMapper.toResponse(repository.save(transaction));
    }
    @Transactional
    public void delete(Long id){
        User user = currentUserProvider.getCurrentUser();
        Transaction transaction = repository.findByIdAndOwner(id,user).orElseThrow(()->new ResourceNotFoundException("Transaction",id));

        repository.delete(transaction);
    }


    private Category resolveCategory(Long categoryId, User user){
        if(categoryId==null){
            return null;
        }
        return  categoryService.getOwnedCategory(categoryId,user);
    }












}
