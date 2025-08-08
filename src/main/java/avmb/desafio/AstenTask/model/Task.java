package avmb.desafio.AstenTask.model;

import avmb.desafio.AstenTask.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String title;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(nullable=false)
    private String status;

    private String priority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="project_id", nullable=false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="assignee_id")
    private User assignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="reporter_id")
    private User reporter;

    @Column(name="estimated_hours")
    private Integer estimatedHours;

    @Column(name="actual_hours")
    private Integer actualHours;

    @Column(name="due_date")
    private LocalDate dueDate;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
