package com.example.demo;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import java.time.*;

@MappedSuperclass
public class RideData {
    @Id
    long id;
    String name;
    boolean is_open;
    int wait_time;
    Instant last_updated;
    @ManyToOne
    @JoinColumn(name="park_id", referencedColumnName = "id")
    ParkData park;

    public RideData(long id, String name, boolean is_open, int wait_time, Instant last_updated) {
        this.id = id;
        this.name = name;
        this.is_open = is_open;
        this.wait_time = wait_time;
        this.last_updated = last_updated;
    }
    public RideData() {}
}
