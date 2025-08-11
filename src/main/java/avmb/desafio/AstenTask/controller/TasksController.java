package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.comment.CommentRequestDTO;
import avmb.desafio.AstenTask.model.comment.CommentResponseDTO;
import avmb.desafio.AstenTask.model.task.TaskAssigneeUpdateDTO;
import avmb.desafio.AstenTask.model.task.TaskRequestDTO;
import avmb.desafio.AstenTask.model.task.TaskResponseDTO;
import avmb.desafio.AstenTask.model.task.TaskStatusUpdateDTO;
import avmb.desafio.AstenTask.model.timeLog.TimeLogRequestDTO;
import avmb.desafio.AstenTask.model.timeLog.TimeLogResponseDTO;
import avmb.desafio.AstenTask.service.CommentService;
import avmb.desafio.AstenTask.service.TaskService;
import avmb.desafio.AstenTask.service.TimeLogService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TasksController {
    private final TaskService taskService;
    private final CommentService commentService;
    private final TimeLogService timeLogService;

    public TasksController(TaskService taskService, CommentService commentService, TimeLogService timeLogService) {
        this.taskService = taskService;
        this.commentService = commentService;
        this.timeLogService = timeLogService;
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
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task successfully deleted");
    }
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody @Valid TaskStatusUpdateDTO dto) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, dto));
    }
    @PutMapping("/{id}/assign")
    public ResponseEntity<?> assignTask(@PathVariable Long id, @RequestBody @Valid TaskAssigneeUpdateDTO dto) {
        return ResponseEntity.ok(taskService.assignTask(id, dto));
    }
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long taskId, @RequestBody @Valid CommentRequestDTO dto) {
        CommentResponseDTO createdComment = commentService.createComment(taskId, dto);
        return ResponseEntity.status(201).body(createdComment);
    }
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByTask(@PathVariable Long taskId, Pageable pageable) {
        Page<CommentResponseDTO> tasks = commentService.getCommentByTask(taskId, pageable);
        return ResponseEntity.ok(tasks);
    }
    @PostMapping("/{taskId}/time-logs")
    public ResponseEntity<TimeLogResponseDTO>  createTimeLog(@PathVariable Long taskId, @RequestBody TimeLogRequestDTO dto) {
        TimeLogResponseDTO createdTimeLog = timeLogService.createTimeLog(taskId, dto);
        return ResponseEntity.status(201).body(createdTimeLog);
    }
    @GetMapping("/{taskId}/time-logs")
    public ResponseEntity<Page<TimeLogResponseDTO>> getTimeLogsByTask(@PathVariable Long taskId, Pageable pageable) {
        return ResponseEntity.ok(timeLogService.getTimeLogByTask(taskId, pageable));
    }
}
