package avmb.desafio.AstenTask.model.project;

import java.time.LocalDateTime;

public record ProjectResponseDTO(Long id, String name, String description, String status, String ownerName, LocalDateTime createdAt, LocalDateTime updatedAt) {
}

