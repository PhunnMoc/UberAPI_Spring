package com.example.demo.repositories;

import com.example.demo.models.User;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;
@EnableMongoRepositories
public interface UserRepository extends MongoRepository <User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);

}
