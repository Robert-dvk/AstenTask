package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.project.CreateProjectDTO;
import avmb.desafio.AstenTask.model.project.Project;
import avmb.desafio.AstenTask.model.project.ProjectResponseDTO;
import avmb.desafio.AstenTask.model.project.UpdateProjectDTO;
import avmb.desafio.AstenTask.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<?> createProject(@RequestBody CreateProjectDTO dto) {
        return ResponseEntity.ok(projectService.createProject(dto));
    }
    @GetMapping
    public ResponseEntity<Page<Project>> listProjects() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createdAt").descending());
        Page<Project> projects = projectService.listProjects(pageable);
        return ResponseEntity.ok(projects);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getProjectById(@PathVariable Long id) {
        Optional<ProjectResponseDTO> project = projectService.getProjectById(id);
        if (project.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Project with id " + id + " not found");
        }
        return ResponseEntity.ok(project.get());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProjectById(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok("Project successfully deleted!");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProject(@PathVariable Long id, @RequestBody UpdateProjectDTO dto) {
        Optional<ProjectResponseDTO> updated = projectService.updateProject(id, dto);
        if (updated.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Project with id " + id + " not found");
        }
        return ResponseEntity.ok(updated.get());
    }
}
