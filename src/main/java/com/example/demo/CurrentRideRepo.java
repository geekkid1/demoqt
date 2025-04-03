package com.example.demo;

import org.springframework.data.repository.CrudRepository;

public interface CurrentRideRepo extends CrudRepository<CurrentRideData, Long> {
}
