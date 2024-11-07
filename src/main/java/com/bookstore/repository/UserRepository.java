package com.bookstore.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
}
