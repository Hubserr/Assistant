package pl.project.Assistant.task;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private Date until;
    private LocalDateTime createdAt;


}
