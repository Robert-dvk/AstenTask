package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.project.ProjectRequestDTO;
import avmb.desafio.AstenTask.model.project.Project;
import avmb.desafio.AstenTask.model.project.ProjectResponseDTO;
import avmb.desafio.AstenTask.model.task.TaskRequestDTO;
import avmb.desafio.AstenTask.model.task.TaskResponseDTO;
import avmb.desafio.AstenTask.service.ProjectService;
import avmb.desafio.AstenTask.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
@Tag(name = "Projetos", description = "Operações para criar, listar, atualizar e deletar projetos e tarefas relacionadas")
public class ProjectController {
    private final ProjectService projectService;
    private final TaskService taskService;

    public ProjectController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Operation(summary = "Criar projeto", description = "Cria um novo projeto")
    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody @Valid ProjectRequestDTO dto) {
        return ResponseEntity.status(201).body(projectService.createProject(dto));
    }

    @Operation(summary = "Listar projetos", description = "Lista projetos paginados, ordenados pela data de criação decrescente")
    @GetMapping
    public ResponseEntity<Page<Project>> listProjects() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Project> projects = projectService.listProjects(pageable);
        return ResponseEntity.ok(projects);
    }

    @Operation(summary = "Buscar projeto por ID", description = "Retorna os detalhes do projeto pelo seu ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        ProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }

    @Operation(summary = "Deletar projeto", description = "Remove um projeto pelo seu ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProjectById(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project successfully deleted");
    }

    @Operation(summary = "Atualizar projeto", description = "Atualiza os dados de um projeto existente")
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long id, @RequestBody @Valid ProjectRequestDTO dto) {
        ProjectResponseDTO updated = projectService.updateProject(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Criar tarefa em projeto", description = "Cria uma nova tarefa vinculada a um projeto")
    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<TaskResponseDTO> createTask(@PathVariable Long projectId, @RequestBody @Valid TaskRequestDTO dto) {
        TaskResponseDTO createdTask = taskService.createTask(projectId, dto);
        return ResponseEntity.status(201).body(createdTask);
    }

    @Operation(summary = "Listar tarefas de projeto", description = "Lista tarefas de um projeto, paginadas")
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<Page<TaskResponseDTO>> getTaskByProject(@PathVariable Long projectId, Pageable pageable) {
        Page<TaskResponseDTO> tasks = taskService.getTaskByProject(projectId, pageable);
        return ResponseEntity.ok(tasks);
    }
}
