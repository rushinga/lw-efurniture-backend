//package com.webtech.efurniture.controller;
//
//
//
//import com.webtech.efurniture.model.Furniture;
//import com.webtech.efurniture.model.Role;
//import com.webtech.efurniture.model.User;
//import com.webtech.efurniture.service.FurnitureService;
//import com.webtech.efurniture.service.UserService;
//import org.springframework.core.io.ByteArrayResource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Controller
//
//public class AdminUserController {
//
//    private final UserService userService;
//    private final FurnitureService furnitureService;
//
//    public AdminUserController(UserService userService, FurnitureService furnitureService) {
//        this.userService = userService;
//        this.furnitureService = furnitureService;
//    }
////    @GetMapping("/admin")
////    public String showAdminDashboard(Model model) {
////        List<User> recentUsers = userService.getRecentUsers();
////        model.addAttribute("recentUsers", recentUsers);
////        return "admin-dashboard"; // The template where recent users are displayed
////    }
//
////    @GetMapping("/dashboard")
////    public String showAdminDashboard(Model model) {
////        // You can add any additional data you want to show on the dashboard here
////        model.addAttribute("totalUsers", userService.getAllUsers().size());
////        model.addAttribute("totalProducts", furnitureService.getAllFurniture().size());
////        // Add more data as needed
////        return "admin-dashboard"; // The template where the dashboard is displayed
////    }
//
//    @GetMapping("/dashboard")
//    public String showDashboard(Model model) {
//        return "admin-dashboard"; // Return the dashboard template
//    }
//
//    @GetMapping("/dashboard/data")
//    @ResponseBody
//    public Map<String, Integer> getDashboardData() {
//        Map<String, Integer> data = new HashMap<>();
//        int totalUsers = userService.getAllUsers().size();
//        int totalProducts = furnitureService.getAllFurniture().size();
//        int totalOrders = 0; // Replace with actual order count if you have an OrderService
//
//        data.put("totalUsers", totalUsers);
//        data.put("totalProducts", totalProducts);
//        data.put("totalOrders", totalOrders);
//        return data;
//    }
//
//    @GetMapping("/admin/add")
//    public String showAddUserForm(Model model) {
//        model.addAttribute("user", new User()); // Create a new User object
//        return "add-user"; // Return the add user template
//    }
//
//    @PostMapping("/admin/users")
//    public String addUser (@ModelAttribute User user) {
//        userService.registerUser (user); // Save the user using your service
//        return "redirect:/admin"; // Redirect to the admin dashboard after saving
//    }
//
//    @PostMapping("/admin/users/delete/{id}")
//    public String deleteUser (@PathVariable Long id) {
//        userService.deleteUser (id); // Call the service to delete the user
//        return "redirect:/admin"; // Redirect to the admin dashboard after deletion
//    }
//
//    @GetMapping("/admin/users/edit/{id}")
//    public String showEditUserForm(@PathVariable Long id, Model model) {
//        User user = userService.getUserById(id); // Fetch the user by ID
//        model.addAttribute("user", user); // Add the user to the model
//        return "edit-user"; // Return the edit user template
//    }
//
//    @PostMapping("/admin/users/update")
//    public String updateUser (@ModelAttribute User user) {
//        userService.updateUser (user); // Call the service to update the user
//        return "redirect:/admin"; // Redirect to the admin dashboard after updating
//    }
//
//    @GetMapping("/admin/search")
//    public String showSearchForm() {
//        return "search-user"; // Return the search user template
//    }
//
//    @GetMapping("/admin/search/results")
//    public String searchUsers(@RequestParam(required = false) String username,
//                              @RequestParam(required = false) String email,
//                              Model model) {
//        List<User> users = userService.searchUsers(username, email); // Call the service to search for users
//        model.addAttribute("users", users); // Add the list of users to the model
//        return "user-list"; // Return the template that displays the list of users
//    }
//
//
//    @GetMapping("/admin/download/users")
//    @ResponseBody
//    public ResponseEntity<ByteArrayResource> downloadUsers() throws IOException {
//        List<User> users = userService.getAllUsers(); // Fetch all users from the service
//
//        // Create CSV content
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        PrintWriter writer = new PrintWriter(outputStream);
//
//        // Write CSV header
//        writer.println("ID,Username,Email"); // Adjust according to your User fields
//
//        // Write user data
//        for (User  user : users) {
//            writer.printf("%d,%s,%s%n", user.getId(), user.getUsername(), user.getEmail()); // Adjust according to your User fields
//        }
//        writer.flush();
//        writer.close();
//
//        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
//
//        // Set the content type and attachment header
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.csv");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(resource.contentLength())
//                .contentType(MediaType.parseMediaType("text/csv"))
//                .body(resource);
//    }
//
//
//
//    @GetMapping("/admin/upload")
//    public String showUploadPage() {
//        return "upload"; // Return the combined upload page template
//    }
//
//    // Handle GET requests for the furniture upload endpoint
//    @GetMapping("/admin/upload/furniture")
//    public String showFurnitureUploadForm(Model model) {
//        model.addAttribute("furnitureMessage", "Please use the form to upload furniture data.");
//        return "upload"; // Display the upload page with a message
//    }
//
//    // Handle POST requests for furniture upload
//    @PostMapping("/admin/upload/furniture")
//    public String uploadFurniture(@RequestParam("file") MultipartFile file, Model model) {
//        if (file.isEmpty()) {
//            model.addAttribute("furnitureMessage", "Please select a file to upload.");
//            return "upload";
//        }
//
//        try {
//            List<Furniture> furnitureList = new ArrayList<>();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
//            String line;
//            reader.readLine(); // Skip header line
//
//            while ((line = reader.readLine()) != null) {
//                String[] data = line.split(",");
//                Furniture furniture = new Furniture();
//                furniture.setName(data[0]);
//                furniture.setType(data[1]);
//                furniture.setPrice(Double.parseDouble(data[2]));
//                furniture.setImageUrl(data[3]);
//                furnitureList.add(furniture);
//            }
//
//            furnitureService.saveAll(furnitureList);
//            model.addAttribute("furnitureMessage", "Furniture file uploaded successfully!");
//            return "redirect:/admin";
//        } catch (IOException | NumberFormatException e) {
//            model.addAttribute("furnitureMessage", "Failed to upload furniture file: " + e.getMessage());
//        }
//
//        return "upload";
//    }
//
//    // Handle GET requests for the user upload endpoint
//    @GetMapping("/admin/upload/users")
//    public String showUserUploadForm(Model model) {
//        model.addAttribute("userMessage", "Please use the form to upload user data.");
//        return "upload"; // Display the upload page with a message
//    }
//
//    // Handle POST requests for user upload
//    @PostMapping("/admin/upload/users")
//    public String uploadUsers(@RequestParam("file") MultipartFile file, Model model) {
//        if (file.isEmpty()) {
//            model.addAttribute("userMessage", "Please select a file to upload.");
//            return "upload";
//        }
//
//        try {
//            List<User> userList = new ArrayList<>();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
//            String line;
//            reader.readLine(); // Skip header line
//
//            while ((line = reader.readLine()) != null) {
//                String[] data = line.split(",");
//                User user = new User();
//                user.setUsername(data[0]);
//                user.setFirstName(data[1]);
//                user.setLastName(data[2]);
//                user.setEmail(data[3]);
//                user.setPhoneNumber(data[4]);
//                user.setProfilePicture(data[5]);
//                user.setRole(Role.valueOf(data[6]));
//                userList.add(user);
//            }
//
//            userService.saveAll(userList);
//            model.addAttribute("userMessage", "User file uploaded successfully!");
//            return "redirect:/admin";
//        } catch (IOException | IllegalArgumentException e) {
//            model.addAttribute("userMessage", "Failed to upload user file: " + e.getMessage());
//        }
//
//        return "upload";
//    }
//
//    @GetMapping("/admin/user-role-stats")
//    @ResponseBody
//    public Map<String, Integer> getUserRoleStatistics() {
//        List<User> users = userService.getAllUsers(); // Fetch all users from the service
//        Map<String, Integer> roleStats = new HashMap<>();
//
//        // Count users per role
//        for (User  user : users) {
//            String role = user.getRole().name(); // Assuming getRole() returns a Role enum
//            roleStats.put(role, roleStats.getOrDefault(role, 0) + 1);
//        }
//
//        return roleStats; // Return the statistics as a JSON response
//    }
//}
//
package com.webtech.efurniture.controller;

//import com.itextpdf.io.font.constants.StandardFonts;
import com.webtech.efurniture.model.ActivityLog;
import com.webtech.efurniture.model.Role;
import com.webtech.efurniture.model.User;
import com.webtech.efurniture.service.ActivityLogService;
import com.webtech.efurniture.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//
//import com.itextpdf.kernel.font.PdfFont;
//import com.itextpdf.kernel.font.PdfFontFactory;
//
//import com.itextpdf.kernel.pdf.PdfDocument;
//import com.itextpdf.kernel.pdf.PdfWriter;
//import com.itextpdf.layout.Document;
//import com.itextpdf.layout.element.Paragraph;
//import com.itextpdf.layout.element.Table;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminUserController {

    private final UserService userService;
    private final ActivityLogService activityLogService;

    public AdminUserController(UserService userService, ActivityLogService activityLogService) {
        this.userService = userService;
        this.activityLogService = activityLogService;
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getUsers(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {

        // Use the pageable method to get a page of users with sorting
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



//    @GetMapping("/users/add")
//    public String showAddUserForm(Model model) {
//        model.addAttribute("user", new User()); // Create a new User object
//        return "add-user"; // Return the add user template
//    }

    @PostMapping("/users")
    public String addUser (@ModelAttribute User user) {
        userService.registerUser (user); // Save the user using your service
        return "Successfully registered user"; // Redirect to the user management page after saving
    }



    @PostMapping("/users/delete/{id}")
    public String deleteUser (@PathVariable Long id) {
        userService.deleteUser (id); // Call the service to delete the user
        return "Successfully"; // Redirect to the user management page after deletion
    }



    @GetMapping("/search/results")
    public List<User> searchUsers(@RequestParam(required = false) String username,
                              @RequestParam(required = false) String email
                           ) {
        List<User> users = userService.searchUsers(username, email); // Call the service to search for users

        return users; // Return the template that displays the list of users
    }

    @GetMapping("/download/users")
    @ResponseBody
    public ResponseEntity<ByteArrayResource> downloadUsers() throws IOException {
        List<User> users = userService.getAllUsers(); // Fetch all users from the service

        // Create CSV content
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // Write CSV header
        writer.println("ID,Username,Email"); // Adjust according to your User fields

        // Write user data
        for (User  user : users) {
            writer.printf("%d,%s,%s%n", user.getId(), user.getUsername(), user.getEmail()); // Adjust according to your User fields
        }
        writer.flush();
        writer.close();

        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());

        // Set the content type and attachment header
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment ;filename=users.csv");

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(resource.contentLength())
                .headers(headers)
                .body(resource);
    }




    // Handle POST requests for user upload
    @PostMapping("/upload/users")
    public String uploadUsers(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Please select a file to upload.";
        }

        try {
            List<User> userList = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
            String line;
            reader.readLine(); // Skip header line

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                User user = new User();
                user.setUsername(data[0]);
                user.setFirstName(data[1]);
                user.setLastName(data[2]);
                user.setEmail(data[3]);
                user.setPhoneNumber(data[4]);
//                user.setProfilePicture(data[5]);
                user.setRole(Role.valueOf(data[5]));
                userList.add(user);
            }

            userService.saveAll(userList);

            return "User file uploaded successfully!";
        } catch (IOException | IllegalArgumentException e) {

            return e.getMessage();
        }


    }




    @GetMapping("/user-role-stats")
    @ResponseBody
    public Map<String, Integer> getUserRoleStatistics() {
        List<User> users = userService.getAllUsers(); // Fetch all users from the service
        Map<String, Integer> roleStats = new HashMap<>();

        // Count users per role
        for (User  user : users) {
            if (user.getRole() != null){
                String role = user.getRole().name();
                roleStats.put(role, roleStats.getOrDefault(role, 0) +1);
            }else {
                roleStats.put("no roles", roleStats.getOrDefault("no role",0) +1);
            }
        }

        return roleStats; // Return the statistics as a JSON response
    }


//    @GetMapping("/download/users/pdf")
//    @ResponseBody
//    public ResponseEntity<ByteArrayResource> downloadUsersPdf() throws IOException {
//        List<User> users = userService.getAllUsers(); // Fetch all users from the service
//
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        PdfWriter writer = new PdfWriter(outputStream);
//        PdfDocument pdfDocument = new PdfDocument(writer);
//        Document document = new Document(pdfDocument);
//
//        // Create a font instance
//        PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
//
//        // Add a title with font size and font
//        document.add(new Paragraph("User  Data").setFontSize(18).setFont(font));
//
//        // Create a table with 3 columns
//        Table table = new Table(3);
//        table.addHeaderCell("ID");
//        table.addHeaderCell("Username");
//        table.addHeaderCell("Email");
//
//
//        // Add user data to the table
//        for (User  user : users) {
//            table.addCell(String.valueOf(user.getId()));
//            table.addCell(user.getUsername());
//            table.addCell(user.getEmail());
//        }
//
//        document.add(table);
//        document.close();
//
//        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
//
//        // Set the content type and attachment header
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=users.pdf");
//
//        return ResponseEntity.ok()
//                .contentType(MediaType.APPLICATION_PDF)
//                .contentLength(resource.contentLength())
//                .headers(headers)
//                .body(resource);
//    }


    @PutMapping("/users/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            // Call the service method to update the user
            User updatedUser = userService.updateUser(id, user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/total-users")
    public ResponseEntity<Long> getTotalUsers() {
        Long totalUsers = userService.countTotalUsers();
        return ResponseEntity.ok(totalUsers);
    }

    @GetMapping("/recent-activity")
    public ResponseEntity<List<ActivityLog>> getRecentActivityLogs() {
        List<ActivityLog> recentLogs = activityLogService.getRecentActivities();
        return ResponseEntity.ok(recentLogs);
    }

}