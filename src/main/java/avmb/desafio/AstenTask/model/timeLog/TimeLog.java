package avmb.desafio.AstenTask.model.timeLog;

import avmb.desafio.AstenTask.model.task.Task;
import avmb.desafio.AstenTask.model.user.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    public TimeLog(User author, Task task, Integer hoursWorked, String description) {
        this.task = task;
        this.user = author;
        this.hoursWorked = hoursWorked;
        this.description = description;
    }
}