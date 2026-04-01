package pl.project.Assistant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository){
        this.repository = repository;
    }

    public Page<Task> getTasks(String search, Boolean completed, Pageable pageable){
        Specification<Task> spec = (root, query, cb) -> cb.conjunction();

        if(search!=null&&!search.isEmpty()){
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("title"), "%" + search.toLowerCase()+"%"));
        }
        if(completed!=null){
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("completed"),completed));
        }
        return repository.findAll(spec, pageable);
    }
    @Transactional
    public Task updateTask(Long id,Task updatedTask){
        return repository.findById(id).map(task->
        {task.setTitle(updatedTask.getTitle());
        task.setCompleted(updatedTask.isCompleted());
        task.setDescription(updatedTask.getDescription());
        task.setUntil(updatedTask.getUntil());
        return repository.save(task);
        }).orElseThrow(()->new RuntimeException("Nie znaleziono zadania o id: " +id));
    }
    public Task addTask(Task task){
        return repository.save(task);
    }

    public void removeTask(Long id){
        repository.deleteById(id);
    }
}
