package avmb.desafio.AstenTask.repository;

import avmb.desafio.AstenTask.model.timeLog.TimeLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeLogRepository extends JpaRepository<TimeLog, Long> {
    Page<TimeLog> findByTaskId(Long taskId, Pageable pageable);
}
