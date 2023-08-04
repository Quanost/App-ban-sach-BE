package com.example.DemoSpringCRUD.controller;



import com.example.DemoSpringCRUD.model.User;
import com.example.DemoSpringCRUD.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {

        return userRepository.save(user);
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {

        return ResponseEntity.ok(userRepository.findAll());
    }
}
