package pl.project.Assistant.kitchen.shopping;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.kitchen.product.Product;

import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository extends JpaRepository<ShoppingListItem,Long> {

    @EntityGraph(attributePaths = "product")
    List<ShoppingListItem> findAllByOwnerOrderByProductNameAsc(User owner);
    Optional<ShoppingListItem> findByIdAndOwner(Long id, User owner);
    Optional<ShoppingListItem> findByOwnerAndProduct(User owner, Product product);
}
