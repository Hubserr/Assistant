package pl.project.Assistant.task;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

@Data
public class TaskRequest {

    @NotBlank(message  ="Title cannot be blank")
    @Size(min=3, message = "Title has to have at least 3 letters")
    private String title;

    private String description;
    @Future
    private Date until;
    private boolean completed;



}
