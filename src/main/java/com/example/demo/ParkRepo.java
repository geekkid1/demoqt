package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface ParkRepo extends CrudRepository<ParkData, Long> {
}
