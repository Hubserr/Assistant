package pl.project.Assistant.kitchen.product;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.project.Assistant.auth.User;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findByIdAndOwner(Long id, User owner);
    List<Product> findAllByOwnerOrderByNameAsc(User owner);
    boolean existsByOwnerAndNameIgnoreCase(User owner, String name);
    boolean existsByOwnerAndNameIgnoreCaseAndIdNot(User owner, String name,Long id);

}
