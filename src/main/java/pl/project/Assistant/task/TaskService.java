package pl.project.Assistant.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.project.Assistant.auth.CurrentUserProvider;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.exception.AccessDeniedException;
import pl.project.Assistant.exception.ResourceNotFoundException;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final CurrentUserProvider currentUserProvider;
    public TaskService(TaskRepository taskRepository,CurrentUserProvider currentUserProvider){
        this.taskRepository = taskRepository;
        this.currentUserProvider = currentUserProvider;
    }

    public Page<Task> getTasks(String search, Boolean completed, Pageable pageable){

        User user = currentUserProvider.getCurrentUser();

        Specification<Task> spec = (root, query, cb) ->
                cb.equal(root.get("owner"),user);

        if(search!=null&&!search.isEmpty()){
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("title"), "%" + search.toLowerCase()+"%"));
        }
        if(completed!=null){
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("completed"),completed));
        }
        return taskRepository.findAll(spec, pageable);
    }
    @Transactional
    public Task updateTask(Long id,Task updatedTask){
        User user = currentUserProvider.getCurrentUser();
        Task task = taskRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Task",id));

        if(!task.getOwner().equals(user)){
            throw new AccessDeniedException("Access denied!");
        }

        task.setTitle(updatedTask.getTitle());
        task.setCompleted(updatedTask.isCompleted());
        task.setDescription(updatedTask.getDescription());
        task.setUntil(updatedTask.getUntil());
        return taskRepository.save(task);

    }

    public Task addTask(Task task){
        User user = currentUserProvider.getCurrentUser();
        task.setOwner(user);
        return taskRepository.save(task);
    }

    public void removeTask(Long id){
        User user = currentUserProvider.getCurrentUser();
        Task task = taskRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Task",id));
        if(!task.getOwner().equals(user)) {
            throw new AccessDeniedException("Access denied!");
        }
        taskRepository.deleteById(id);
    }
}
