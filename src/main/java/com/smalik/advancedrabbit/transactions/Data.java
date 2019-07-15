package com.smalik.advancedrabbit.transactions;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
public class Data {

    @Id
    private String id;

    @NotNull
    private String notes;

    public Data() { }

    public Data(String notes) {
        this.id = UUID.randomUUID().toString();
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
