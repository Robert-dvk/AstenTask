package avmb.desafio.AstenTask.model.task;

import java.time.LocalDateTime;

public record TaskRequestDTO(String title, String description, TaskStatus status, TaskPriority priority, Long assigneeId, Integer estimatedHours, Integer actualHours, LocalDateTime dueDate) {
}
