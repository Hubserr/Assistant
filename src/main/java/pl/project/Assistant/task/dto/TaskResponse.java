package pl.project.Assistant.task.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime until;
    private LocalDateTime createdAt;


}
