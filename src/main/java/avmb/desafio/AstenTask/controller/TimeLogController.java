package avmb.desafio.AstenTask.controller;

import avmb.desafio.AstenTask.model.timeLog.TimeLogRequestDTO;
import avmb.desafio.AstenTask.model.timeLog.TimeLogResponseDTO;
import avmb.desafio.AstenTask.service.TimeLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/time-log")
public class TimeLogController {
    private final TimeLogService timeLogService;
    public TimeLogController(TimeLogService timeLogService) {
        this.timeLogService = timeLogService;
    }
    @PutMapping("/{id}")
    public ResponseEntity<TimeLogResponseDTO> updateTimeLog(@PathVariable Long id, @RequestBody TimeLogRequestDTO dto) {
        return ResponseEntity.ok(timeLogService.updateTimeLog(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTimeLog(@PathVariable Long id) {
        timeLogService.deleteTimeLog(id);
        return ResponseEntity.ok("Time log successfully deleted!");
    }
}
