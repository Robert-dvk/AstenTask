package avmb.desafio.AstenTask.model.comment;

import java.time.LocalDateTime;

public record CommentResponseDTO(Long id, String content, String authorName, String taskTitle, String taskDescription, LocalDateTime createdAt) {
}
