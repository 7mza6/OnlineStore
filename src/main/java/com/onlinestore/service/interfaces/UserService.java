package com.onlinestore.service.interfaces;

import java.util.Optional;

import com.onlinestore.api.entities.Role;
import com.onlinestore.api.entities.User;

public interface UserService {
    User register(String name, String email, String rawPassword, Role role);
    User findById(int id);
    Optional<User> findByEmail(String email);
}
