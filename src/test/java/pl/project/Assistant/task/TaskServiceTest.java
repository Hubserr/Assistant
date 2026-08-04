package pl.project.Assistant.task;



import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.project.Assistant.auth.User;
import pl.project.Assistant.auth.UserRepository;
import pl.project.Assistant.exception.AccessDeniedException;
import pl.project.Assistant.exception.ResourceNotFoundException;

import java.lang.module.ResolutionException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private static final String TEST_EMAIL = "test@test.com";

    @BeforeEach
    void setUpSecurityContext() {
        var authentication = new UsernamePasswordAuthenticationToken(TEST_EMAIL, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void addTask_shouldSetOwnerAndSaveTask() {
        User user = new User();
        user.setEmail(TEST_EMAIL);

        Task newTask = new Task();
        newTask.setTitle("Test task");

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.save(newTask)).thenReturn(newTask);

        Task result = taskService.addTask(newTask);

        assertEquals(user, result.getOwner());
        verify(taskRepository).save(newTask);
    }

    @Test
    void updateTask_shouldUpdateTask() {
        User user = new User();
        user.setEmail(TEST_EMAIL);

        Task existingTask = new Task();
        existingTask.setOwner(user);
        existingTask.setTitle("Title");

        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");
        updatedTask.setCompleted(true);
        updatedTask.setDescription("Description");

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        Task result = taskService.updateTask(1L, updatedTask);

        assertEquals("New Title", result.getTitle());
        assertEquals(true, result.isCompleted());
        assertEquals("Description", result.getDescription());
        verify(taskRepository).save(existingTask);

    }

    @Test
    void updateTask_taskNotFound() {
        User user = new User();
        user.setEmail(TEST_EMAIL);

        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");
        updatedTask.setCompleted(true);
        updatedTask.setDescription("Description");

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taskService.updateTask(999L,updatedTask) );
    }

    @Test
    void updateTask_accesDenied() {
        User user = new User();
        User user2 = new User();
        user.setEmail(TEST_EMAIL);
        user2.setEmail("mail");

        Task existingTask = new Task();
        existingTask.setOwner(user2);
        existingTask.setTitle("Title");

        Task updatedTask = new Task();
        updatedTask.setTitle("New Title");
        updatedTask.setCompleted(true);
        updatedTask.setDescription("Description");

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        assertThrows(AccessDeniedException.class, () -> taskService.updateTask(1L,updatedTask));
        verify(taskRepository,never()).save(any());

    }

    @Test
    void removeTask_succes(){
        User user = new User();
        user.setEmail(TEST_EMAIL);

        Task existingTask = new Task();
        existingTask.setOwner(user);
        existingTask.setTitle("Title");

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        taskService.removeTask(1L);

        verify(taskRepository).deleteById(1L);

    }

    @Test
    void removeTask_taskNotFound(){
        User user = new User();
        user.setEmail(TEST_EMAIL);

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()->taskService.removeTask(999L));

    }
    @Test
    void removeTask_accesDenied() {
        User user = new User();
        User user2 = new User();
        user.setEmail(TEST_EMAIL);
        user2.setEmail("mail");

        Task existingTask = new Task();
        existingTask.setOwner(user2);
        existingTask.setTitle("Title");

        when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(user));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        assertThrows(AccessDeniedException.class, () -> taskService.removeTask(1L));
        verify(taskRepository,never()).save(any());

    }


}