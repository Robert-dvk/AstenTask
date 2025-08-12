package avmb.desafio.AstenTask.service;

import avmb.desafio.AstenTask.exception.DeleteException;
import avmb.desafio.AstenTask.exception.InvalidInsertException;
import avmb.desafio.AstenTask.exception.ResourceNotFoundException;
import avmb.desafio.AstenTask.model.project.Project;
import avmb.desafio.AstenTask.model.task.*;
import avmb.desafio.AstenTask.model.user.User;
import avmb.desafio.AstenTask.repository.CommentRepository;
import avmb.desafio.AstenTask.repository.ProjectRepository;
import avmb.desafio.AstenTask.repository.TaskRepository;
import avmb.desafio.AstenTask.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final CommentRepository commentRepository;
    private final BrazilApiService brazilService;
    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository, UserRepository userRepository, CommentRepository commentRepository, BrazilApiService brazilService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
        this.commentRepository = commentRepository;
        this.brazilService = brazilService;
    }
    @Transactional
    public TaskResponseDTO createTask(Long projectId, TaskRequestDTO dto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User reporter = (User) userRepository.findByEmail(userEmail);

        validateTaskInput(dto);

        Project project = findProjectByIdOrThrow(projectId);

        Task savedTask = this.taskRepository.save(new Task(
                dto.title(),
                dto.description() != null ? dto.description() : "",
                dto.status(),
                dto.priority(),
                project,
                null,
                reporter,
                dto.estimatedHours(),
                dto.actualHours(),
                dto.dueDate(),
                LocalDateTime.now()
        ));

        return getTaskResponseDTO(savedTask);
    }
    public TaskResponseDTO getTaskById(Long id) {
        Task task = findTaskByIdOrThrow(id);
        return getTaskResponseDTO(task);
    }

    private TaskResponseDTO getTaskResponseDTO(Task task) {
        return new TaskResponseDTO (
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
        );
    }

    public Page<TaskResponseDTO> getTaskByProject(Long projectId, Pageable pageable) {
        findProjectByIdOrThrow(projectId);
        Page<Task> tasksPage = taskRepository.findByProjectId(projectId, pageable);
        return tasksPage.map(this::getTaskResponseDTO);
    }

    @Transactional
    public TaskResponseDTO updateTask(Long id, TaskRequestDTO dto) {
        Task task = findTaskByIdOrThrow(id);
        validateTaskInput(dto);
        if (dto.title() != null) {
            task.setTitle(dto.title());
        }

        if (dto.description() != null) {
            task.setDescription(dto.description());
        }

        if (dto.estimatedHours() != null) {
            task.setEstimatedHours(dto.estimatedHours());
        }

        if (dto.actualHours() != null) {
            task.setActualHours(dto.actualHours());
        }

        if (dto.dueDate() != null) {
            task.setDueDate(dto.dueDate());
        }
        if(dto.priority() != null) {
            task.setPriority(String.valueOf(dto.priority()));
        }
        if(dto.status() != null) {
            task.setStatus(String.valueOf(dto.status()));
        }
        if (dto.assigneeId() != null) {
            User assignee = findUserByIdOrThrow(dto.assigneeId());
            task.setAssignee(assignee);
        }
        task.setUpdatedAt(LocalDateTime.now());
        Task updatedTask = taskRepository.save(task);
        return getTaskResponseDTO(updatedTask);
    }

    @Transactional
    public void deleteTask(Long id) {
        Task task = findTaskByIdOrThrow(id);
        boolean hasComment = commentRepository.existsByTaskId(id);
        if (hasComment) {
            throw new DeleteException("Cannot delete the task because there are associated comments.");
        }
        taskRepository.delete(task);
    }

    @Transactional
    public TaskResponseDTO updateTaskStatus(Long id, TaskStatusUpdateDTO dto) {
        Task task = findTaskByIdOrThrow(id);
        task.setStatus(String.valueOf(dto.status()));
        Task updated = taskRepository.save(task);
        return getTaskResponseDTO(updated);
    }

    @Transactional
    public TaskResponseDTO assignTask(Long id, TaskAssigneeUpdateDTO dto) {
        Task task = findTaskByIdOrThrow(id);
        System.out.println(dto.assignee());
        User assignee = findUserByIdOrThrow(dto.assignee());
        task.setAssignee(assignee);
        Task updated = taskRepository.save(task);
        return getTaskResponseDTO(updated);
    }

    public Task findTaskByIdOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }

    public Project findProjectByIdOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    public User findUserByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    private void validateTaskInput(TaskRequestDTO dto) {
        if (brazilService.isHoliday(LocalDate.from(dto.dueDate()))) {
            throw new InvalidInsertException("The due date cannot be a holiday.");
        }
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
