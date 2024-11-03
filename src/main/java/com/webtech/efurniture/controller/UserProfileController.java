package com.webtech.efurniture.controller;

import com.webtech.efurniture.model.User;
import com.webtech.efurniture.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class UserProfileController {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private UserService userService;

    @GetMapping("/user/profile")
    public String viewProfile(HttpSession session, Model model) {
        // Log the current session state
        System.out.println("Current session user: " + session.getAttribute("loggedInUser "));

        // Retrieve the logged-in user's data from the session
        User user = (User) session.getAttribute("loggedInUser ");
        if (user == null) {
            return "redirect:/login"; // Redirect to login if user is not logged in
        }
        model.addAttribute("user", user);
        return "userprofile"; // Return the name of the profile HTML template
    }

    @PostMapping("/user/profile/update")
    public String updateProfile(HttpSession session,
                                @RequestParam String username,
                                @RequestParam String email,
                                @RequestParam(required = false) String password,
                                @RequestParam(required = false) String firstName,
                                @RequestParam(required = false) String lastName,
                                @RequestParam(required = false) String phoneNumber,
                                @RequestParam(required = false) MultipartFile profilePicture) {
        User user = (User ) session.getAttribute("loggedInUser  ");
        if (user == null) {
            return "redirect:/login"; // Redirect to login if user is not logged in
        }

        // Update user details
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);

        if (password != null && !password.isEmpty()) {
            user.setPassword(password); // Hash the password before saving
        }

        // Handle profile picture upload
        if (profilePicture != null && !profilePicture.isEmpty()) {
            // Ensure the upload directory exists
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it does not exist
            }

            // Define the file name and path
            String fileName = System.currentTimeMillis() + "_" + profilePicture.getOriginalFilename();
            File uploadFile = new File(directory, fileName);

            try {
                profilePicture.transferTo(uploadFile); // Save the file
                // Save the relative path in the user object
                user.setProfilePicture("/" + uploadDir + "/" + fileName); // Ensure this is a relative path
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the error (e.g., log it, show an error message, etc.)
            }
        }

        userService.updateUser (user); // Update the user in the database
        session.setAttribute("loggedInUser  ", user); // Update the session with the new user data

        // Redirect based on the user's role
        switch (user.getRole()) {
            case ROLE_ADMIN:
                return "redirect:/admin"; // Redirect to admin dashboard
            case ROLE_SELLER:
                return "redirect:/seller"; // Redirect to seller dashboard
            case ROLE_USER:
                return "redirect:/user"; // Redirect to user dashboard
            default:
                return "redirect:/login"; // Default redirect if no role matches
        }

    }
}