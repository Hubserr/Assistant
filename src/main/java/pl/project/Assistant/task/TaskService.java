package pl.project.Assistant.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.auth.UserRepository;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public TaskService(TaskRepository taskRepository,UserRepository userRepository){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    public Page<Task> getTasks(String search, Boolean completed, Pageable pageable){

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));

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
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        Task task = taskRepository.findById(id).orElseThrow(()->new RuntimeException("Task not found"));

        if(!task.getOwner().equals(user)){
            throw new RuntimeException("Access denied!");
        }

        task.setTitle(updatedTask.getTitle());
        task.setCompleted(updatedTask.isCompleted());
        task.setDescription(updatedTask.getDescription());
        task.setUntil(updatedTask.getUntil());
        return taskRepository.save(task);

    }

    public Task addTask(Task task){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        task.setOwner(user);
        return taskRepository.save(task);
    }

    public void removeTask(Long id){
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        Task task = taskRepository.findById(id).orElseThrow(()->new RuntimeException("Task not found"));
        if(!task.getOwner().equals(user)) {
            throw new RuntimeException("Access denied!");
        }
        taskRepository.deleteById(id);
    }
}
