package pl.project.Assistant.kitchen.fridge;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.kitchen.product.Product;

import java.math.BigDecimal;


@Entity
@Table(name = "kit_fridge_item")
@Getter
@Setter
public class FridgeItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Product product;
    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal amount;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;














}
