package pl.project.Assistant.kitchen.recipe;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.kitchen.product.Product;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe,Long>, JpaSpecificationExecutor<Recipe> {

    Optional<Recipe> findByIdAndOwner(Long id, User user);
    Page<Recipe> findAll(Specification<Recipe> spec, Pageable pageable);


    @EntityGraph(attributePaths = {"ingredients", "ingredients.product"})
    Optional<Recipe> findWithIngredientsByIdAndOwner(Long id, User owner);

    boolean existsByIngredientsProduct(Product product);

}
