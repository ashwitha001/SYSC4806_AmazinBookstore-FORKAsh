package com.bookstore.repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.bookstore.model.Cart;

@Repository
public interface CartRepository extends MongoRepository<Cart, Long>{}
