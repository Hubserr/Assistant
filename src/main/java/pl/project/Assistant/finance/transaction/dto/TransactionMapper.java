package pl.project.Assistant.finance.transaction.dto;


import pl.project.Assistant.finance.transaction.Transaction;

public class TransactionMapper {


    public static TransactionResponse toResponse(Transaction transaction){
        TransactionResponse response = new TransactionResponse();

        response.setId(transaction.getId());
        response.setTitle(transaction.getTitle());
        response.setAmount(transaction.getAmount());
        if(transaction.getCategory()!=null){
        response.setCategoryId(transaction.getCategory().getId());
        response.setCategoryName(transaction.getCategory().getName());
        }
        response.setTransactionDate(transaction.getTransactionDate());
        response.setCreationDate(transaction.getCreationDate());
        response.setType(transaction.getType());
        response.setDescription(transaction.getDescription());

        return  response;

    }



}
