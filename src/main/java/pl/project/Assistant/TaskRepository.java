package pl.project.Assistant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TaskRepository  extends JpaRepository<Task,Long>, JpaSpecificationExecutor<Task> {

}
