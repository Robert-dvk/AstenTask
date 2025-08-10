package avmb.desafio.AstenTask.model;

import avmb.desafio.AstenTask.model.task.Task;
import avmb.desafio.AstenTask.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "time_logs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class TimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="task_id", nullable=false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(name="hours_worked", nullable=false)
    private Integer hoursWorked;

    @Column(columnDefinition="TEXT")
    private String description;

    @Column(name="log_date", nullable=false)
    private LocalDate logDate;
}