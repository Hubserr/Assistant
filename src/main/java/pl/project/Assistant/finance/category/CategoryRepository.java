package pl.project.Assistant.finance.category;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.project.Assistant.auth.User;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    List<Category> findAllByOwnerOrderByNameAsc(User owner);

    Optional<Category> findByIdAndOwner(Long id,User owner);

    boolean existsByOwnerAndNameIgnoreCase(User owner, String name);
    boolean existsByOwnerAndNameIgnoreCaseAndIdNot(User owner, String name,Long id);

}
