package pl.project.Assistant.auth;


import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final UserService service;

    public AuthController(UserService service){
        this.service = service;
    }
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request){
       return service.login(request);

    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request){
        return service.register(request);
    }



}
