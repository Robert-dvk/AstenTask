package avmb.desafio.AstenTask.model.timeLog;

public record TimeLogResponseDTO(Long id, String authorName, String taskTitle, String description, Integer hoursWorked, java.time.LocalDate logDate) {
}
