package pl.project.Assistant.kitchen.recipe;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.project.Assistant.kitchen.product.Product;

import java.math.BigDecimal;

@Entity
@Table(name = "kit_recipe_ingredient")
@Setter
@Getter
@NoArgsConstructor
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="recipe_id")
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name="product_id")
    private Product product;



    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal amount;

    public Ingredient(Product product, BigDecimal amount){
        this.product = product;
        this.amount = amount;
    }












}
