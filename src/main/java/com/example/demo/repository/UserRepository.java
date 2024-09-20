package com.example.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.UserModel;

@Repository
public interface UserRepository extends CrudRepository<UserModel, Long> {
    UserModel findByEmail(String email);
}
