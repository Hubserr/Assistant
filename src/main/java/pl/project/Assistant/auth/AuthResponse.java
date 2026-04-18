package pl.project.Assistant.auth;


import lombok.Data;

@Data
public class AuthResponse {

    private Long id;
    private String email;
    private String token;
    private String role;





}
