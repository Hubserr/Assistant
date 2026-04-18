package pl.project.Assistant.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "You must enter e-mail!")
    @Email
    private String email;

    @NotBlank(message = "You must enter password")
    private String password;

    @NotBlank(message = "You must re-enter password")
    private String confirmPassword;



}
