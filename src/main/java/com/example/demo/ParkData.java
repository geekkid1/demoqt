package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ParkData {
    @Id
    long id;
    String name;
    String timezone;

    public ParkData() {}
    public ParkData(long id, String name, String timezone) {
        this.id = id;
        this.name = name;
        this.timezone = timezone;
    }
}
