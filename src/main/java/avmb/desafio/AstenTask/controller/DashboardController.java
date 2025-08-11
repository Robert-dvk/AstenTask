package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.dashboard.DashboardOverviewResponseDTO;
import avmb.desafio.AstenTask.model.task.TaskResponseDTO;
import avmb.desafio.AstenTask.service.DashboardService;
import avmb.desafio.AstenTask.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;
    public DashboardController(DashboardService dashboardService,  TaskService taskService) {
        this.dashboardService = dashboardService;
    }
    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewResponseDTO> getOverview() {
        return ResponseEntity.ok(dashboardService.getOverview());
    }
    @GetMapping("/my-tasks")
    public ResponseEntity<Page<TaskResponseDTO>> getMyTasks(Pageable pageable) {
        Page<TaskResponseDTO> page = dashboardService.getMyTasks(pageable);
        return ResponseEntity.ok(page);
    }
}
