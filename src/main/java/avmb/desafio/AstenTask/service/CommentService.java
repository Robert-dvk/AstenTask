package avmb.desafio.AstenTask.service;

import avmb.desafio.AstenTask.exception.ResourceNotFoundException;
import avmb.desafio.AstenTask.model.comment.Comment;
import avmb.desafio.AstenTask.model.comment.CommentRequestDTO;
import avmb.desafio.AstenTask.model.comment.CommentResponseDTO;
import avmb.desafio.AstenTask.model.task.Task;
import avmb.desafio.AstenTask.model.user.User;
import avmb.desafio.AstenTask.repository.CommentRepository;
import avmb.desafio.AstenTask.repository.TaskRepository;
import avmb.desafio.AstenTask.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    public CommentService(CommentRepository commentRepository, TaskRepository taskRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    @Transactional
    public CommentResponseDTO createComment(Long taskId, CommentRequestDTO dto) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User author = (User) userRepository.findByEmail(userEmail);
        Task task = findTaskByIdOrThrow(taskId);
        Comment comment = new Comment(author, task, dto.content());
        return getCommentResponse(commentRepository.save(comment));
    }
    public Page<CommentResponseDTO> getCommentByTask(Long id, Pageable pageable) {
        findTaskByIdOrThrow(id);
        Page<Comment> commentPage = commentRepository.findByTaskId(id, pageable);
        return commentPage.map(this::getCommentResponse);
    }
    @Transactional
    public CommentResponseDTO updateComment(Long id, CommentRequestDTO dto) {
        Comment comment = findCommentByIdOrThrow(id);
        comment.setContent(dto.content());
        Comment updated = commentRepository.save(comment);
        return getCommentResponse(updated);
    }
    @Transactional
    public void deleteComment(Long id) {
        Comment comment = findCommentByIdOrThrow(id);
        commentRepository.delete(comment);
    }
    private CommentResponseDTO getCommentResponse(Comment savedComment) {
        return new CommentResponseDTO(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getAuthor().getName(),
                savedComment.getTask().getTitle(),
                savedComment.getTask().getDescription(),
                savedComment.getCreatedAt()
        );
    }
    public Task findTaskByIdOrThrow(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
    }
    public Comment findCommentByIdOrThrow(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + id));
    }
}
