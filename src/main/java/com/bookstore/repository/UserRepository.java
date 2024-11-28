package com.bookstore.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.model.User;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, Long>{
    Optional<User> findByUsername(String username);
}
