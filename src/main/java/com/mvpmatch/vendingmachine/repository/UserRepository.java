package com.mvpmatch.vendingmachine.repository;

import com.mvpmatch.vendingmachine.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

}
