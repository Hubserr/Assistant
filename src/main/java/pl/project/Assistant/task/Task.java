package pl.project.Assistant.task;

import jakarta.persistence.*;
import lombok.Data;
import pl.project.Assistant.auth.User;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private boolean completed;
    private Date until;
    @ManyToOne
    @JoinColumn(name= "user_id")
    private User owner;
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
    }


}
