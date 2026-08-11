package com.onlinestore.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.onlinestore.api.entities.User;
import com.onlinestore.service.interfaces.UserService;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private UserService userService;

    public AppUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with email - " + email));
        return new UserPrincipal(user);
    }
}
