package com.example.demo;

import jakarta.persistence.Entity;

import java.time.Instant;

@Entity
public class CurrentRideData extends RideData {
    public CurrentRideData(long id, String name, boolean is_open, int wait_time, Instant last_updated) {
        super(id,name,is_open,wait_time,last_updated);
    }
    public CurrentRideData() {}
}
