package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.timeLog.TimeLogRequestDTO;
import avmb.desafio.AstenTask.model.timeLog.TimeLogResponseDTO;
import avmb.desafio.AstenTask.service.TimeLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/time-log")
@Tag(name = "Logs de Tempo", description = "Gerenciamento de registros de tempo em tarefas")
public class TimeLogController {
    private final TimeLogService timeLogService;

    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }

    @Operation(summary = "Atualizar log de tempo", description = "Atualiza um registro de tempo pelo ID")
    @PutMapping("/{id}")
    public ResponseEntity<TimeLogResponseDTO> updateTimeLog(@PathVariable Long id, @RequestBody TimeLogRequestDTO dto) {
        return ResponseEntity.ok(timeLogService.updateTimeLog(id, dto));
    }

    @Operation(summary = "Deletar log de tempo", description = "Remove um registro de tempo pelo ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTimeLog(@PathVariable Long id) {
        timeLogService.deleteTimeLog(id);
        return ResponseEntity.ok("Time log successfully deleted!");
    }
}
