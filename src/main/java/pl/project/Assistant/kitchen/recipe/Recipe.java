package pl.project.Assistant.kitchen.recipe;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.kitchen.Nutrition;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kit_recipe")
@Getter
@Setter
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,length = 150)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Integer servings;

    @Embedded
    private Nutrition nutrition;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User owner;


    private Integer prepTimeMinutes;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ingredient> ingredients = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "kit_recipe_steps",joinColumns = @JoinColumn(name="recipe_id"))
    @OrderColumn(name = "step_order")
    @Column(name = "content",nullable = false,length  =1000)
    private List<String> steps = new ArrayList<>();

    @Column(nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }

    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
        ingredient.setRecipe(this);
    }
    public void deleteIngredient(Ingredient ingredient){
        ingredients.remove(ingredient);
        ingredient.setRecipe(null);
    }







}
