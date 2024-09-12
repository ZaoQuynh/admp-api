package com.example.admpapi.service;


import com.example.admpapi.api.model.User;
import com.example.admpapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

@Service
public class UserService {

    private List<User> users;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService (){
    }

    public Optional<User> findById(Long uid) {
        return userRepository.findById(uid);
    }

    public Optional<User> getUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        return Optional.ofNullable(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void updatePassword(Long uid, String password) {
        User user = userRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + uid));

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return Optional.ofNullable(user);
    }
}
