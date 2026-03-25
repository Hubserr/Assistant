package pl.project.Assistant;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService service;

    public TaskController(TaskService service){
        this.service = service;
    }

    @GetMapping
    public List<Task> getAll(){
        return service.getAllTask();
    }
    @PostMapping
    public Task add(@Valid @RequestBody Task task){
        return service.addTask(task);
    }
    @PutMapping({"/{id}"})
    public Task update(@PathVariable Long id, @Valid @RequestBody Task task){
        return service.updateTask(id,task);
    }
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        service.removeTask(id);
    }



}
