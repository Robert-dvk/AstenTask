package avmb.desafio.AstenTask.model;

import avmb.desafio.AstenTask.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="TEXT", nullable=false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="task_id", nullable=false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id", nullable=false)
    private User author;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}