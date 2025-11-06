package com.gabriele.burraco.service;

import com.gabriele.burraco.model.Draw;
import com.gabriele.burraco.model.Participant;
import com.gabriele.burraco.repo.BurracoRepository;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BurracoService {

    private final BurracoRepository repo;
    private final Random rnd = new Random();

    public BurracoService(BurracoRepository repo) {
        this.repo = repo;
    }

    public List<Participant> getParticipants() { return repo.listParticipants(); }

    public Participant saveParticipant(Participant p) { return repo.upsertParticipant(p); }

    public void deleteParticipant(UUID id) { repo.deleteParticipant(id); }

    public List<Draw> getDraws() { return repo.listDraws(); }

    public Draw drawNext(String prizeType) {
        List<Participant> participants = repo.listParticipants().stream()
                .filter(Participant::isPresent).collect(Collectors.toList());
        List<Draw> existing = repo.listDraws();

        Set<UUID> alreadyDrawn = existing.stream().map(d -> d.getParticipant().getId()).collect(Collectors.toSet());
        List<Participant> remaining = participants.stream()
                .filter(p -> !alreadyDrawn.contains(p.getId()))
                .collect(Collectors.toList());

        if (remaining.isEmpty()) throw new IllegalStateException("Nessun partecipante disponibile per l'estrazione");

        Participant selected = remaining.get(rnd.nextInt(remaining.size()));
        Draw d = new Draw(UUID.randomUUID(), selected, prizeType, OffsetDateTime.now());
        return repo.upsertDraw(d);
    }

    public void resetDraws() { repo.clearDraws(); }
}
