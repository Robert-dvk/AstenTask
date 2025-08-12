package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.dashboard.DashboardOverviewResponseDTO;
import avmb.desafio.AstenTask.model.task.TaskResponseDTO;
import avmb.desafio.AstenTask.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "Endpoints para informações gerais e tarefas do usuário")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "Visão geral do dashboard", description = "Retorna informações resumidas para o dashboard do usuário")
    @GetMapping("/overview")
    public ResponseEntity<DashboardOverviewResponseDTO> getOverview() {
        return ResponseEntity.ok(dashboardService.getOverview());
    }

    @Operation(summary = "Tarefas do usuário", description = "Retorna página das tarefas atribuídas ao usuário autenticado")
    @GetMapping("/my-tasks")
    public ResponseEntity<Page<TaskResponseDTO>> getMyTasks(Pageable pageable) {
        Page<TaskResponseDTO> page = dashboardService.getMyTasks(pageable);
        return ResponseEntity.ok(page);
    }
}
