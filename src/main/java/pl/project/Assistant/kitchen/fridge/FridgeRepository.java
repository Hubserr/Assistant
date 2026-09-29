package pl.project.Assistant.kitchen.fridge;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.kitchen.product.Product;

import java.util.List;
import java.util.Optional;

public interface FridgeRepository extends JpaRepository<FridgeItem,Long> {

    @EntityGraph(attributePaths = "product")
    List<FridgeItem> findAllByOwnerOrderByProductNameAsc(User owner);
    Optional<FridgeItem> findByIdAndOwner(Long id, User owner);
    Optional<FridgeItem> findByOwnerAndProduct(User owner, Product product);
}
