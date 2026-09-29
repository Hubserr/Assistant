package pl.project.Assistant.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskRequest {

    @NotBlank(message  ="Title cannot be blank")
    @Size(min=3, message = "Title has to have at least 3 letters")
    private String title;

    @Size(max = 255)
    private String description;
    private LocalDateTime until;
    private boolean completed;



}
