package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.project.ProjectRequestDTO;
import avmb.desafio.AstenTask.model.project.Project;
import avmb.desafio.AstenTask.model.project.ProjectResponseDTO;
import avmb.desafio.AstenTask.model.task.TaskRequestDTO;
import avmb.desafio.AstenTask.model.task.TaskResponseDTO;
import avmb.desafio.AstenTask.service.ProjectService;
import avmb.desafio.AstenTask.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final TaskService taskService;

    public ProjectController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody ProjectRequestDTO dto) {
        return ResponseEntity.status(201).body(projectService.createProject(dto));
    }
    @GetMapping
    public ResponseEntity<Page<Project>> listProjects() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Project> projects = projectService.listProjects(pageable);
        return ResponseEntity.ok(projects);
    }
    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> getProjectById(@PathVariable Long id) {
        ProjectResponseDTO project = projectService.getProjectById(id);
        return ResponseEntity.ok(project);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProjectById(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project successfully deleted");
    }
    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponseDTO> updateProject(@PathVariable Long id, @RequestBody ProjectRequestDTO dto) {
        ProjectResponseDTO updated = projectService.updateProject(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{projectId}/tasks")
    public ResponseEntity<TaskResponseDTO> createTask(@PathVariable Long projectId, @RequestBody TaskRequestDTO dto) {
        TaskResponseDTO createdTask = taskService.createTask(projectId, dto);
        return ResponseEntity.status(201).body(createdTask);
    }
    @GetMapping("/{projectId}/tasks")
    public ResponseEntity<Page<TaskResponseDTO>> getTaskByProject(@PathVariable Long projectId, Pageable pageable) {
        Page<TaskResponseDTO> tasks = taskService.getTaskByProject(projectId, pageable);
        return ResponseEntity.ok(tasks);
    }
}
