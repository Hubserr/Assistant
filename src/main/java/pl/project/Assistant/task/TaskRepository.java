package pl.project.Assistant.task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pl.project.Assistant.auth.User;

import java.util.Optional;

public interface TaskRepository  extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {

    Optional<Task> findByIdAndOwner(Long id, User owner);
}
