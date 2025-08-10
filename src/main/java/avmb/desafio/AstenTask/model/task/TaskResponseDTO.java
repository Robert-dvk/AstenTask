package avmb.desafio.AstenTask.model.task;

import java.time.LocalDateTime;

public record TaskResponseDTO(Long id, String title, String description, String priority, String status, Long projectId, String projectName, String assigneeName, String reporterName, Integer estimatedHours, Integer actualHours, LocalDateTime dueDate, LocalDateTime createdAt, LocalDateTime updatedAt) {
}
