package com.example.admpapi.api.control;

import com.example.admpapi.api.model.User;
import com.example.admpapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/get-user")
    public ResponseEntity<User> getUser(@RequestParam String username, @RequestParam String password) {
        Optional<User> user = userService.getUser(username, password);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }


    @PostMapping("/add-user")
    public User create(@RequestBody Map<String, String> body){
        String fullName = body.get("fullName");
        String email = body.get("email");
        String phoneNumber = body.get("phoneNumber");
        String username = body.get("username");
        String password = body.get("password");

        User user = new User(fullName, email, phoneNumber, username, password);

        return userService.save(user);
    }
}
