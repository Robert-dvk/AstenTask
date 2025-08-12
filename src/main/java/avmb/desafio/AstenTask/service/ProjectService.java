package avmb.desafio.AstenTask.service;

import avmb.desafio.AstenTask.exception.DeleteException;
import avmb.desafio.AstenTask.exception.InvalidInsertException;
import avmb.desafio.AstenTask.exception.ResourceNotFoundException;
import avmb.desafio.AstenTask.model.project.*;
import avmb.desafio.AstenTask.model.user.User;
import avmb.desafio.AstenTask.repository.ProjectRepository;
import avmb.desafio.AstenTask.repository.TaskRepository;
import avmb.desafio.AstenTask.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }
    @Transactional
    public ProjectResponseDTO createProject(ProjectRequestDTO dto) {
        validateProjectInput(dto.name(), dto.description());

        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User owner = (User) userRepository.findByEmail(userEmail);

        Project newProject = new Project(dto.name(), dto.description(), dto.status(), owner);

        Project savedProject = this.projectRepository.save(newProject);

        return getProjectResponseDTO(savedProject);
    }
    public Page<Project> listProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = findProjectByIdOrThrow(id);
        return getProjectResponseDTO(project);
    }

    private ProjectResponseDTO getProjectResponseDTO(Project project) {
        return new ProjectResponseDTO(
            project.getId(),
            project.getName(),
            project.getDescription(),
            project.getStatus(),
            project.getOwner().getName(),
            project.getCreatedAt(),
            project.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteProject(Long id) {
        findProjectByIdOrThrow(id);
        boolean hasTasks = taskRepository.existsByProjectId(id);
        if (hasTasks) {
            throw new DeleteException("Cannot delete the project because there are associated tasks.");
        }
        projectRepository.deleteById(id);
    }
    @Transactional
    public ProjectResponseDTO updateProject(Long id, ProjectRequestDTO dto) {
        Project project = findProjectByIdOrThrow(id);

        if (dto.name() != null && !dto.name().isBlank()) {
            project.setName(dto.name());
        }
        if (dto.description() != null) {
            project.setDescription(dto.description());
        }
        if (dto.status() != null) {
            project.setStatus(String.valueOf(dto.status()));
        }
        validateProjectInput(project.getName(), project.getDescription());

        project.setUpdatedAt(LocalDateTime.now());

        Project updatedProject = projectRepository.save(project);

        return getProjectResponseDTO(updatedProject);
    }
    public Project findProjectByIdOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }
    private void validateProjectInput(String name, String description) {
        if (name == null || name.isBlank()) {
            throw new InvalidInsertException("Project name cannot be null or empty");
        }
        if (description == null || description.isBlank()) {
            throw new InvalidInsertException("Project description cannot be null or empty");
        }
    }
}