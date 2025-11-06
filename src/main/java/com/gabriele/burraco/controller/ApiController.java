package com.gabriele.burraco.controller;

import com.gabriele.burraco.model.Draw;
import com.gabriele.burraco.model.Participant;
import com.gabriele.burraco.service.BurracoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final BurracoService service;

    public ApiController(BurracoService service) { this.service = service; }

    // Health
    @GetMapping("/health")
    public Map<String,Object> health() {
        return Map.of("status","ok");
    }

    // Participants
    @GetMapping("/participants")
    public List<Participant> participants() { return service.getParticipants(); }

    @PostMapping("/participants")
    public Participant upsertParticipant(@Valid @RequestBody Participant p) { return service.saveParticipant(p); }

    @DeleteMapping("/participants/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable UUID id) {
        service.deleteParticipant(id);
        return ResponseEntity.noContent().build();
    }

    // Draws
    @GetMapping("/draws")
    public List<Draw> draws() { return service.getDraws(); }

    @PostMapping("/draws/next")
    public Draw drawNext(@RequestParam(defaultValue = "generic") String prizeType) {
        return service.drawNext(prizeType);
    }

    @DeleteMapping("/draws")
    public ResponseEntity<Void> resetDraws() {
        service.resetDraws();
        return ResponseEntity.noContent().build();
    }
}
