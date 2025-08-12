package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.comment.CommentRequestDTO;
import avmb.desafio.AstenTask.model.comment.CommentResponseDTO;
import avmb.desafio.AstenTask.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@Tag(name = "Comentários", description = "Operações para atualizar e deletar comentários")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "Atualizar comentário", description = "Atualiza um comentário existente pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long id, @RequestBody @Valid CommentRequestDTO dto) {
        CommentResponseDTO updated = commentService.updateComment(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Deletar comentário", description = "Remove um comentário pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.ok("Comment successfully deleted!");
    }
}
