package com.be08.smart_notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.be08.smart_notes.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
