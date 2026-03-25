package pl.project.Assistant;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository){
        this.repository = repository;
    }

    public List<Task> getAllTask(){
        return repository.findAll(Sort.by(Sort.Direction.DESC,"createdAt"));
    }
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
