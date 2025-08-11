package avmb.desafio.AstenTask.service;

import avmb.desafio.AstenTask.exception.ResourceNotFoundException;
import avmb.desafio.AstenTask.model.dashboard.DashboardOverviewResponseDTO;
import avmb.desafio.AstenTask.model.task.Task;
import avmb.desafio.AstenTask.model.task.TaskResponseDTO;
import avmb.desafio.AstenTask.model.user.User;
import avmb.desafio.AstenTask.repository.ProjectRepository;
import avmb.desafio.AstenTask.repository.TaskRepository;
import avmb.desafio.AstenTask.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public DashboardService(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    public DashboardOverviewResponseDTO getOverview() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) userRepository.findByEmail(userEmail);

        long totalProjects = projectRepository.countByOwnerId(user.getId());
        long totalTasksAssigned = taskRepository.countByAssigneeId(user.getId());
        long tasksCompleted = taskRepository.countByAssigneeIdAndStatus(user.getId(), "FINISHED");
        long tasksPending = totalTasksAssigned - tasksCompleted;

        return new DashboardOverviewResponseDTO(totalProjects, totalTasksAssigned, tasksCompleted, tasksPending);
    }

    public Page<TaskResponseDTO> getMyTasks(Pageable pageable) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = (User) userRepository.findByEmail(userEmail);
        Page<Task> tasksPage = taskRepository.findByAssigneeId(user.getId(), pageable);
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
}
