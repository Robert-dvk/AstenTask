package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.task.TaskAssigneeUpdateDTO;
import avmb.desafio.AstenTask.model.task.TaskRequestDTO;
import avmb.desafio.AstenTask.model.task.TaskResponseDTO;
import avmb.desafio.AstenTask.model.task.TaskStatusUpdateDTO;
import avmb.desafio.AstenTask.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    private final TaskService taskService;
    public TasksController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTaskById(@PathVariable Long id) {
        TaskResponseDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO dto) {
        TaskResponseDTO updated = taskService.updateTask(id, dto);
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody @Valid TaskStatusUpdateDTO dto) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, dto));
    }
    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignTask(@PathVariable Long id, @RequestBody @Valid TaskAssigneeUpdateDTO dto) {
        return ResponseEntity.ok(taskService.assignTask(id, dto));
    }
}
