package avmb.desafio.AstenTask.repository;

import avmb.desafio.AstenTask.model.task.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByProjectId(Long projectId, Pageable pageable);
    long countByAssigneeId(Long assigneeId);
    long countByAssigneeIdAndStatus(Long assigneeId, String status);
    boolean existsByProjectId(Long projectId);
    Page<Task> findByAssigneeId(Long assigneeId, Pageable pageable);
}
