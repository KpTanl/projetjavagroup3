package com.group3.carrental.entity;

import java.util.UUID;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class Note {

    @Id
    protected String id;

    @PrePersist
    protected void generateIdIfNull() {
        if (this.id == null || this.id.isBlank()) {
            this.id = UUID.randomUUID().toString();
        }
    }

    public abstract double calculerNoteMoyenne();
}
