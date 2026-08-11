package com.onlinestore.service.implementations;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.onlinestore.DAO.interfaces.UserDAO;
import com.onlinestore.api.entities.Role;
import com.onlinestore.api.entities.User;
import com.onlinestore.service.ErrorHandling.DuplicateResourceException;
import com.onlinestore.service.ErrorHandling.GlobalNotFoundException;
import com.onlinestore.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User register(String name, String email, String rawPassword, Role role) {
        if (userDAO.existsByEmail(email)) {
            throw new DuplicateResourceException("An account with this email already exists");
        }
        User theUser = new User(name, email, passwordEncoder.encode(rawPassword), role);
        return userDAO.save(theUser);
    }

    @Override
    public User findById(int id) {
        User theUser = userDAO.findById(id);
        if (theUser == null) {
            throw new GlobalNotFoundException("User id not found - " + id);
        }
        return theUser;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userDAO.findByEmail(email);
    }
}
