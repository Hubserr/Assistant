package pl.project.Assistant.task;


import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service){
        this.service = service;
    }

    @GetMapping
    public Page<TaskResponse> getTasks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean completed,
            @org.springdoc.core.annotations.ParameterObject Pageable pageable){
        return service.getTasks(search,completed,pageable).map(TaskMapper::toResponse);
    }
    @PostMapping
    public TaskResponse add(@Valid @RequestBody TaskRequest request){
        Task task = TaskMapper.toEntity(request);
        Task saved = service.addTask(task);
        return TaskMapper.toResponse(saved);
    }

    @PutMapping({"/{id}"})
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request){
        Task task = TaskMapper.toEntity(request);
        Task saved = service.updateTask(id,task);
        return TaskMapper.toResponse(saved);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){

        service.removeTask(id);
    }



}
