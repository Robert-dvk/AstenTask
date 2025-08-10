package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.project.ProjectRequestDTO;
import avmb.desafio.AstenTask.model.project.ProjectResponseDTO;
import avmb.desafio.AstenTask.model.task.TaskRequestDTO;
import avmb.desafio.AstenTask.model.task.TaskResponseDTO;
import avmb.desafio.AstenTask.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    private final TaskService taskService;
    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        Optional<TaskResponseDTO> task = taskService.getTaskById(id);
        if (task.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Task with id " + id + " not found");
        }
        return ResponseEntity.ok(task.get());
    }
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO dto) {
        TaskResponseDTO updated = taskService.updateTask(id, dto);
        return ResponseEntity.ok(updated);
    }
}
