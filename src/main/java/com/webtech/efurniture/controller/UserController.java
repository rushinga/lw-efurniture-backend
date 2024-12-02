package com.webtech.efurniture.controller;

import com.webtech.efurniture.model.Role;
import com.webtech.efurniture.model.User;
import com.webtech.efurniture.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@RestController

public class UserController {

    @Autowired
    private UserService userService;



    @Autowired
    private MessageSource messageSource; // Inject MessageSource for localization



    @PostMapping("/register")
    public String showRegisterForm(@RequestBody User userObj) {
        User user = userService.registerUser(userObj);
        if (Objects.isNull(user))
            return "Failed to register";
        return "Registration Successfull";
    }

    @GetMapping("/user")
    public User getUser(@RequestParam Long id) {
        return userService.getUserById(id);
    }



    @CrossOrigin("*")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User userobj) {
        Map<String, Object> response = new HashMap<>();

        User user = userService.loginUser(userobj.getUsername());

        // Check if user exists and the password is correct
        if (user == null || !user.getPassword().equals(userobj.getPassword())) {
            response.put("message", "Login failed");
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        // Set user role in the response
        response.put("status", "success");
        response.put("message", "Login successful");
        response.put("role", user.getRole().name());



        return ResponseEntity.ok(response);
    }



    @GetMapping("/admin")
    public ResponseEntity<Map<String,Object>> showAdminDashboard(
                                     @RequestParam(defaultValue = "0") int pageNo,
                                     @RequestParam(defaultValue = "5") int pageSize,
                                     @RequestParam(defaultValue = "id") String sortBy) {



        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<User> userPage = userService.getAllUsers(pageable);

        // Build the response
        Map<String, Object> response = new HashMap<>();
        response.put("users", userPage.getContent());
        response.put("currentPage", pageNo);
        response.put("totalPages", userPage.getTotalPages());
        response.put("totalUsers", userPage.getTotalElements());
        response.put("sortBy", sortBy);

        return ResponseEntity.ok(response);
    }


}