package pl.project.Assistant.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.project.Assistant.config.JwtService;
import pl.project.Assistant.exception.BadRequestException;
import pl.project.Assistant.exception.ConflictException;
import pl.project.Assistant.exception.UnauthorizedException;


@Service
public class UserService {
    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder encoder;

    public UserService(UserRepository repository,JwtService jwtService,PasswordEncoder encoder){
        this.repository = repository;
        this.jwtService = jwtService;
        this.encoder = encoder;
    }

    public AuthResponse register(RegisterRequest request){

        if(repository.findByEmail(request.getEmail()).isPresent()) {
            throw new ConflictException("Email already exist");
        }
        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new BadRequestException("Passwords do not match");
        }
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(User.RoleEnum.USER);

        repository.save(user);

        AuthResponse response = new AuthResponse();
        response.setEmail(request.getEmail());
        response.setRole(user.getRole().name());
        response.setToken(jwtService.generateToken(user));
        response.setId(user.getId());
        return response;


    }

    public AuthResponse login(LoginRequest request){

        User user = repository.findByEmail(request.getEmail())
                .orElseThrow(()->new UnauthorizedException("Login or password incorrect"));

        AuthResponse response = new AuthResponse();
        if(!encoder.matches(request.getPassword(),user.getPassword())){
            throw new UnauthorizedException("Login or password incorrect");
        }
        response.setEmail(request.getEmail());
        response.setRole(user.getRole().name());
        response.setToken(jwtService.generateToken(user));
        response.setId(user.getId());
        return  response;
    }




}
