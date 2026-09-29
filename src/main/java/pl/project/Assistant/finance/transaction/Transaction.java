package pl.project.Assistant.finance.transaction;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.finance.category.Category;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
@Entity
@Getter
@Setter
@Table(name = "fin_transaction")
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    @Column(nullable = false)
    private String title;
    private String description;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDate transactionDate;
    private LocalDateTime creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;


    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;

    @PrePersist
    protected void onCreate(){
        this.creationDate = LocalDateTime.now();
    }













}
