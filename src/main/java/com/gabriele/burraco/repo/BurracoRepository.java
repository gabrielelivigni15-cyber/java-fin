package com.gabriele.burraco.repo;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabriele.burraco.model.Draw;
import com.gabriele.burraco.model.Participant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BurracoRepository {

    private final JdbcTemplate jdbc;
    private final ObjectMapper om;

    public BurracoRepository(JdbcTemplate jdbc, ObjectMapper om) {
        this.jdbc = jdbc;
        this.om = om;
    }

    // Participants
    public List<Participant> listParticipants() {
        return jdbc.query("select id, name, phone, isPresent from participants order by created_at asc", participantRow());
    }

    public Participant upsertParticipant(Participant p) {
        if (p.getId() == null) p.setId(UUID.randomUUID());
        jdbc.update("insert into participants (id, name, phone, isPresent) values (?,?,?,?) on conflict (id) do update set name=excluded.name, phone=excluded.phone, isPresent=excluded.isPresent",
                p.getId(), p.getName(), p.getPhone(), p.isPresent());
        return p;
    }

    public void deleteParticipant(UUID id) {
        jdbc.update("delete from participants where id=?", id);
    }

    private RowMapper<Participant> participantRow() {
        return (rs, i) -> new Participant(
                UUID.fromString(rs.getString("id")),
                rs.getString("name"),
                rs.getString("phone"),
                rs.getBoolean("isPresent")
        );
    }

    // Draws
    public List<Draw> listDraws() {
        return jdbc.query("select id, participant, prizeType, drawnAt from draws order by drawnAt asc", drawRow());
    }

    public Draw upsertDraw(Draw d) {
        if (d.getId() == null) d.setId(UUID.randomUUID());
        try {
            String json = om.writeValueAsString(d.getParticipant());
            jdbc.update("insert into draws (id, participant, prizeType, drawnAt) values (?,?,?,?) on conflict (id) do update set participant=excluded.participant, prizeType=excluded.prizeType, drawnAt=excluded.drawnAt",
                    d.getId(), json, d.getPrizeType(), d.getDrawnAt());
            return d;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearDraws() {
        jdbc.update("delete from draws");
    }

    private RowMapper<Draw> drawRow() {
        return new RowMapper<Draw>() {
            @Override
            public Draw mapRow(ResultSet rs, int rowNum) throws SQLException {
                UUID id = UUID.fromString(rs.getString("id"));
                String pjson = rs.getString("participant");
                Participant p;
                try {
                    p = om.readValue(pjson, Participant.class);
                } catch (Exception ex) {
                    throw new SQLException("Invalid participant json", ex);
                }
                String prize = rs.getString("prizeType");
                OffsetDateTime at = rs.getObject("drawnAt", OffsetDateTime.class);
                return new Draw(id, p, prize, at);
            }
        };
    }
}
