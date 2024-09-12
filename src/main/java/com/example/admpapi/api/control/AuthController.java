package com.example.admpapi.api.control;

import com.example.admpapi.api.model.User;
import com.example.admpapi.service.JwtService;
import com.example.admpapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<Object> register (@RequestBody Map<String, String> body){
        System.out.println("Registering...");

        String fullName = body.get("fullName");
        String email = body.get("email");
        String phoneNumber = body.get("phoneNumber");
        String username = body.get("username");
        String password = body.get("password");

        User user = new User(fullName, email, phoneNumber, username, password);

        var bCryptEncoder = new BCryptPasswordEncoder();
        user.setPassword(bCryptEncoder.encode(password));

        try {
            var otherUser = userService.findByEmail(email);
            if (otherUser.isPresent()) {
                return ResponseEntity.badRequest().body("Email address already used");
            }

            userService.save(user);
            String jwtToken = jwtService.createJwtToken(user);

            var response = new HashMap<String, Object>();
            response.put("token", jwtToken);
            response.put("user", user);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody Map<String, String> body) {
        System.out.println("Logging...");
        String username = body.get("username");
        String password = body.get("password");

        try {

            Optional<User> optionalUser = userService.findByUsername(username);
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                if (!passwordEncoder.matches(password, user.getPassword())) {
                    // Trả về lỗi nếu mật khẩu không khớp
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid password");
                }
                String jwtToken = jwtService.createJwtToken(user);

                var response = new HashMap<String, Object>();
                response.put("user", user);
                response.put("token", jwtToken);

                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body("Bad username and password");
    }
}
