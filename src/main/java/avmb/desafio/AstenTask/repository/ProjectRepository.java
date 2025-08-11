package avmb.desafio.AstenTask.repository;

import avmb.desafio.AstenTask.model.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    long countByOwnerId(Long ownerId);
}
