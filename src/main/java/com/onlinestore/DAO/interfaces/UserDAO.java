package com.onlinestore.DAO.interfaces;

import java.util.List;
import java.util.Optional;

import com.onlinestore.api.entities.User;

public interface UserDAO {
    User save(User theUser);
    User findById(int id);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    List<User> findAll();
    void deleteById(int theId);
}
