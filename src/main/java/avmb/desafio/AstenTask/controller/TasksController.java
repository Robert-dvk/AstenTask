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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Tarefas", description = "Gerenciamento completo de tarefas, comentários e logs de tempo")
public class TasksController {
    private final TaskService taskService;
    private final CommentService commentService;
    private final TimeLogService timeLogService;

    public TasksController(TaskService taskService, CommentService commentService, TimeLogService timeLogService) {
        this.taskService = taskService;
        this.commentService = commentService;
        this.timeLogService = timeLogService;
    }

    @Operation(summary = "Buscar tarefa por ID", description = "Retorna os detalhes de uma tarefa específica")
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable Long id) {
        TaskResponseDTO task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    @Operation(summary = "Atualizar tarefa", description = "Atualiza as informações da tarefa pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO dto) {
        TaskResponseDTO updated = taskService.updateTask(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletar tarefa", description = "Remove a tarefa especificada pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok("Task successfully deleted");
    }

    @Operation(summary = "Atualizar status da tarefa", description = "Modifica o status da tarefa")
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskResponseDTO> updateStatus(@PathVariable Long id, @RequestBody @Valid TaskStatusUpdateDTO dto) {
        return ResponseEntity.ok(taskService.updateTaskStatus(id, dto));
    }

    @Operation(summary = "Atribuir tarefa a usuário", description = "Define ou altera o responsável pela tarefa")
    @PutMapping("/{id}/assign")
    public ResponseEntity<TaskResponseDTO> assignTask(@PathVariable Long id, @RequestBody @Valid TaskAssigneeUpdateDTO dto) {
        return ResponseEntity.ok(taskService.assignTask(id, dto));
    }

    @Operation(summary = "Criar comentário na tarefa", description = "Adiciona um comentário em uma tarefa específica")
    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long taskId, @RequestBody @Valid CommentRequestDTO dto) {
        CommentResponseDTO createdComment = commentService.createComment(taskId, dto);
        return ResponseEntity.status(201).body(createdComment);
    }

    @Operation(summary = "Listar comentários da tarefa", description = "Retorna comentários paginados de uma tarefa")
    @GetMapping("/{taskId}/comments")
    public ResponseEntity<Page<CommentResponseDTO>> getCommentsByTask(@PathVariable Long taskId, Pageable pageable) {
        Page<CommentResponseDTO> comments = commentService.getCommentByTask(taskId, pageable);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Criar log de tempo", description = "Registra tempo dedicado a uma tarefa")
    @PostMapping("/{taskId}/time-logs")
    public ResponseEntity<TimeLogResponseDTO> createTimeLog(@PathVariable Long taskId, @RequestBody TimeLogRequestDTO dto) {
        TimeLogResponseDTO createdTimeLog = timeLogService.createTimeLog(taskId, dto);
        return ResponseEntity.status(201).body(createdTimeLog);
    }

    @Operation(summary = "Listar logs de tempo da tarefa", description = "Retorna logs de tempo paginados associados a uma tarefa")
    @GetMapping("/{taskId}/time-logs")
    public ResponseEntity<Page<TimeLogResponseDTO>> getTimeLogsByTask(@PathVariable Long taskId, Pageable pageable) {
        return ResponseEntity.ok(timeLogService.getTimeLogByTask(taskId, pageable));
    }
}
