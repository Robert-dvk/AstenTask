package avmb.desafio.AstenTask.service;

import avmb.desafio.AstenTask.exception.ResourceNotFoundException;
import avmb.desafio.AstenTask.model.task.Task;
import avmb.desafio.AstenTask.model.timeLog.TimeLog;
import avmb.desafio.AstenTask.model.timeLog.TimeLogRequestDTO;
import avmb.desafio.AstenTask.model.timeLog.TimeLogResponseDTO;
import avmb.desafio.AstenTask.model.user.User;
import avmb.desafio.AstenTask.repository.TaskRepository;
import avmb.desafio.AstenTask.repository.TimeLogRepository;
import avmb.desafio.AstenTask.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class TimeLogService {
    private final TimeLogRepository timeLogRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public TimeLogService(TimeLogRepository timeLogRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.timeLogRepository = timeLogRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public TimeLogResponseDTO createTimeLog(Long taskId, TimeLogRequestDTO dto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = (User) userRepository.findByEmail(userEmail);
        Task task = findTaskByIdOrThrow(taskId);
        TimeLog timeLog = new TimeLog(author, task, dto.hoursWorked(), dto.description());
        timeLog.setLogDate(LocalDate.from(LocalDateTime.now()));
        return getTimeLogResponseDTO(timeLogRepository.save(timeLog));
    }
    public Page<TimeLogResponseDTO> getTimeLogByTask(Long taskId, Pageable pageable) {
        Task task = findTaskByIdOrThrow(taskId);

        return timeLogRepository.findByTaskId(task.getId(), pageable)
                .map(this::getTimeLogResponseDTO);
    }
    @Transactional
    public TimeLogResponseDTO updateTimeLog(Long id, TimeLogRequestDTO dto) {
        TimeLog timeLog = findTimeLogByIdOrThrow(id);

        if(dto.hoursWorked() != null) {
            timeLog.setHoursWorked(dto.hoursWorked());
        }
        if(dto.description() != null) {
            timeLog.setDescription(dto.description());
        }

        TimeLog updated = timeLogRepository.save(timeLog);

        return getTimeLogResponseDTO(updated);
    }
    @Transactional
    public void deleteTimeLog(Long id) {
        TimeLog timeLog = findTimeLogByIdOrThrow(id);
        timeLogRepository.delete(timeLog);
    }
    private TimeLogResponseDTO getTimeLogResponseDTO(TimeLog timeLog) {
        return new TimeLogResponseDTO(
                timeLog.getId(),
                timeLog.getUser().getName(),
                timeLog.getTask().getTitle(),
                timeLog.getDescription(),
                timeLog.getHoursWorked(),
                timeLog.getLogDate()
        );
    }
    public Task findTaskByIdOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }
    public TimeLog findTimeLogByIdOrThrow(Long id) {
        return timeLogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Time log not found with id: " + id));
    }
}
