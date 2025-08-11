package avmb.desafio.AstenTask.model.timeLog;

import java.time.LocalDateTime;

public record TimeLogRequestDTO(Integer hoursWorked, String description, LocalDateTime logDate) {
}
