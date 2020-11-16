package com.assignment.assignment.service;


import com.assignment.assignment.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User save(User user);
}
