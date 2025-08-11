package avmb.desafio.AstenTask.repository;

import avmb.desafio.AstenTask.model.comment.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findByTaskId(Long taskId, Pageable pageable);
    boolean existsByTaskId(Long taskId);
}
