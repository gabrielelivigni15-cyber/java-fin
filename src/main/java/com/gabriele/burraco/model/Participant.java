package com.gabriele.burraco.model;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class Participant {
    private UUID id;
    @NotBlank
    private String name;
    private String phone;
    private boolean isPresent = true;

    public Participant() {}

    public Participant(UUID id, String name, String phone, boolean isPresent) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.isPresent = isPresent;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public boolean isPresent() { return isPresent; }
    public void setPresent(boolean present) { isPresent = present; }
}
