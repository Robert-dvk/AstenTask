package avmb.desafio.AstenTask.service;

import avmb.desafio.AstenTask.exception.InvalidInsertException;
import avmb.desafio.AstenTask.exception.ResourceNotFoundException;
import avmb.desafio.AstenTask.model.project.Project;
import avmb.desafio.AstenTask.model.task.*;
import avmb.desafio.AstenTask.model.user.User;
import avmb.desafio.AstenTask.repository.ProjectRepository;
import avmb.desafio.AstenTask.repository.TaskRepository;
import avmb.desafio.AstenTask.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }
    @Transactional
    public TaskResponseDTO createTask(Long projectId, TaskRequestDTO dto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails userDetails = userRepository.findByEmail(userEmail);
        User reporter = (User) userDetails;

        validateTaskInput(dto);

        Optional<Project> project = projectRepository.findById(projectId);

        if (project.isEmpty()) {
            throw new ResourceNotFoundException("Project with id " + projectId + " not found");
        }

        User assignee = dto.assigneeId() == null ? null : userRepository.findById(dto.assigneeId())
                .orElseThrow(() -> new ResourceNotFoundException("Assignee not found with id: " + dto.assigneeId()));
        
        Task newTask = new Task(
            dto.title(), 
            dto.description() != null ? dto.description() : "",
            dto.status(),
            dto.priority(),
            project.get(), 
            assignee, 
            reporter, 
            dto.estimatedHours(), 
            dto.actualHours(), 
            dto.dueDate(), 
            LocalDateTime.now()
        );

        Task savedTask = this.taskRepository.save(newTask);

        return new TaskResponseDTO(
                savedTask.getId(),
                savedTask.getTitle(),
                savedTask.getDescription(),
                savedTask.getPriority(),
                savedTask.getStatus(),
                project.get().getId(),
                project.get().getName(),
                savedTask.getAssignee() != null ? savedTask.getAssignee().getName() : null,
                savedTask.getReporter().getName(),
                savedTask.getEstimatedHours(),
                savedTask.getActualHours(),
                savedTask.getDueDate(),
                savedTask.getCreatedAt(),
                savedTask.getUpdatedAt()
        );
    }
    public Optional<TaskResponseDTO> getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(task -> new TaskResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getPriority(),
                        task.getStatus(),
                        task.getProject().getId(),
                        task.getProject().getName(),
                        task.getAssignee() != null ? task.getAssignee().getName() : null,
                        task.getReporter().getName(),
                        task.getEstimatedHours(),
                        task.getActualHours(),
                        task.getDueDate(),
                        task.getCreatedAt(),
                        task.getUpdatedAt()
                ));
    }
    public Page<TaskResponseDTO> getTaskByProject(Long projectId, Pageable pageable) {
        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found with id: " + projectId);
        }

        Page<Task> tasksPage = taskRepository.findByProjectId(projectId, pageable);

        return tasksPage.map(task -> new TaskResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getProject().getId(),
                task.getProject().getName(),
                task.getAssignee() != null ? task.getAssignee().getName() : null,
                task.getReporter().getName(),
                task.getEstimatedHours(),
                task.getActualHours(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        ));
    }

    @Transactional
    public TaskResponseDTO updateTask(Long taskId, TaskRequestDTO dto) {
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        validateTaskInput(dto);
        if (dto.title() != null) {
            existingTask.setTitle(dto.title());
        }

        if (dto.description() != null) {
            existingTask.setDescription(dto.description());
        }

        if (dto.estimatedHours() != null) {
            existingTask.setEstimatedHours(dto.estimatedHours());
        }

        if (dto.actualHours() != null) {
            existingTask.setActualHours(dto.actualHours());
        }

        if (dto.dueDate() != null) {
            existingTask.setDueDate(dto.dueDate());
        }
        if (dto.assigneeId() != null) {
            User assignee = userRepository.findById(dto.assigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assignee not found with id: " + dto.assigneeId()));
            existingTask.setAssignee(assignee);
        }
        existingTask.setUpdatedAt(LocalDateTime.now());
        Task updatedTask = taskRepository.save(existingTask);
        return new TaskResponseDTO(
                updatedTask.getId(),
                updatedTask.getTitle(),
                updatedTask.getDescription(),
                updatedTask.getPriority(),
                updatedTask.getStatus(),
                updatedTask.getProject().getId(),
                updatedTask.getProject().getName(),
                updatedTask.getAssignee() != null ? updatedTask.getAssignee().getName() : null,
                updatedTask.getReporter().getName(),
                updatedTask.getEstimatedHours(),
                updatedTask.getActualHours(),
                updatedTask.getDueDate(),
                updatedTask.getCreatedAt(),
                updatedTask.getUpdatedAt()
        );
    }

    private void validateTaskInput(TaskRequestDTO dto) {
        if (dto.title() == null || dto.title().isBlank()) {
            throw new InvalidInsertException("Task title cannot be null or empty");
        }

        if (dto.description() == null || dto.description().isBlank()) {
            throw new InvalidInsertException("Task description cannot be null or empty");
        }

        if (dto.status() == null) {
            throw new InvalidInsertException("Task status cannot be null");
        }

        if (dto.priority() == null) {
            throw new InvalidInsertException("Task priority cannot be null");
        }

        if (dto.estimatedHours() != null && dto.estimatedHours() < 0) {
            throw new InvalidInsertException("Estimated hours cannot be negative");
        }

        if (dto.actualHours() != null && dto.actualHours() < 0) {
            throw new InvalidInsertException("Actual hours cannot be negative");
        }
    }

}
