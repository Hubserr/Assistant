package pl.project.Assistant.kitchen.product;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.project.Assistant.auth.User;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Optional<Product> findByIdAndOwner(Long id, User owner);
    List<Product> findAllByOwnerOrderByNameAsc(User owner);
    Optional<Product> findByOwnerAndNameIgnoreCase(User owner, String name);

    List<Product> findTop15ByOwnerAndNameContainingIgnoreCaseOrderByNameAsc(User owner,String name);
    boolean existsByOwnerAndNameIgnoreCase(User owner, String name);
    boolean existsByOwnerAndNameIgnoreCaseAndIdNot(User owner, String name,Long id);

}
