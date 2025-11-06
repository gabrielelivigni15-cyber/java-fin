package com.gabriele.burraco.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.UUID;

public class Draw {
    private UUID id;
    private Participant participant;
    private String prizeType;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private OffsetDateTime drawnAt;

    public Draw() {}

    public Draw(UUID id, Participant participant, String prizeType, OffsetDateTime drawnAt) {
        this.id = id;
        this.participant = participant;
        this.prizeType = prizeType;
        this.drawnAt = drawnAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Participant getParticipant() { return participant; }
    public void setParticipant(Participant participant) { this.participant = participant; }
    public String getPrizeType() { return prizeType; }
    public void setPrizeType(String prizeType) { this.prizeType = prizeType; }
    public OffsetDateTime getDrawnAt() { return drawnAt; }
    public void setDrawnAt(OffsetDateTime drawnAt) { this.drawnAt = drawnAt; }
}
