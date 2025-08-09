package avmb.desafio.AstenTask.service;

import avmb.desafio.AstenTask.model.project.CreateProjectDTO;
import avmb.desafio.AstenTask.model.project.Project;
import avmb.desafio.AstenTask.model.project.ProjectResponseDTO;
import avmb.desafio.AstenTask.model.project.UpdateProjectDTO;
import avmb.desafio.AstenTask.model.user.User;
import avmb.desafio.AstenTask.repository.ProjectRepository;
import avmb.desafio.AstenTask.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public ProjectResponseDTO createProject(CreateProjectDTO dto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails userDetails = userRepository.findByEmail(userEmail);

        User owner = (User) userDetails;

        Project newProject = new Project(dto.name(), dto.description(), dto.status(), owner);

        Project savedProject = this.projectRepository.save(newProject);

        return new ProjectResponseDTO(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getDescription(),
                savedProject.getStatus(),
                savedProject.getOwner().getName(),
                savedProject.getCreatedAt(),
                savedProject.getUpdatedAt()
        );
    }
    public Page<Project> listProjects(Pageable pageable) {
        return projectRepository.findAll(pageable);
    }
    public Optional<ProjectResponseDTO> getProjectById(Long id) {
        return projectRepository.findById(id)
                .map(project -> new ProjectResponseDTO(
                        project.getId(),
                        project.getName(),
                        project.getDescription(),
                        project.getStatus(),
                        project.getOwner().getName(),
                        project.getCreatedAt(),
                        project.getUpdatedAt()
                ));
    }
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }
    public Optional<ProjectResponseDTO> updateProject(Long id, UpdateProjectDTO dto) {
        return projectRepository.findById(id).map(project -> {
            if (dto.name() != null && !dto.name().isBlank()) {
                project.setName(dto.name());
            }
            if (dto.description() != null) {
                project.setDescription(dto.description());
            }
            if (dto.status() != null) {
                project.setStatus(String.valueOf(dto.status()));
            }
            Project updatedProject = projectRepository.save(project);
            return new ProjectResponseDTO( updatedProject.getId(),
                    updatedProject.getName(),
                    updatedProject.getDescription(),
                    updatedProject.getStatus(),
                    project.getOwner().getName(),
                    project.getCreatedAt(),
                    project.getUpdatedAt());
        });
    }
}