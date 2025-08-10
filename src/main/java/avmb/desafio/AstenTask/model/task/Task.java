package avmb.desafio.AstenTask.model.task;

import avmb.desafio.AstenTask.model.user.User;
import avmb.desafio.AstenTask.model.project.Project;
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

    @Column(nullable=false)
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
    private LocalDateTime dueDate;

    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;

    public Task(String title, String description, TaskStatus status, TaskPriority priority, Project project, User assignee, User reporter, Integer estimatedHours, Integer actualHours, LocalDateTime dueDate, LocalDateTime createdAt) {
        this.title = title;
        this.description = description != null ? description : "";
        this.status = status != null ? status.toString() : TaskStatus.OPEN.toString();
        this.priority = priority != null ? priority.toString() : TaskPriority.MEDIUM.toString();
        this.project = project;
        this.assignee = assignee;
        this.reporter = reporter;
        this.estimatedHours = estimatedHours;
        this.actualHours = actualHours;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
