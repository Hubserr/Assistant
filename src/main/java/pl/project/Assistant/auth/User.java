package pl.project.Assistant.auth;

import jakarta.persistence.*;

import lombok.Data;

@Entity
@Table(name="users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleEnum role;


    public enum RoleEnum{
        USER,
        ADMIN
    }
}
