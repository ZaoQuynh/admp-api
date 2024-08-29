package com.example.admpapi.service;


import com.example.admpapi.api.model.User;
import com.example.admpapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.*;

@Service
public class UserService {

    private List<User> users;
    @Autowired
    private UserRepository userRepository;

    public UserService (){
    }

    public Optional<User> getUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        return Optional.of(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
