package com.company.clothingerp.controller.web;

import com.company.clothingerp.model.*;
import com.company.clothingerp.repository.RoleRepository;
import com.company.clothingerp.repository.UserRepository;
import com.company.clothingerp.service.EmployeeService;
import com.company.clothingerp.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String showAdminPanel(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("employees", employeeService.getAllEmployees());
        model.addAttribute("logs", reportService.getAllSystemLogs());
        model.addAttribute("newEmployee", new Employee());
        return "admin";
    }

    @PostMapping("/employee")
    public String addEmployee(@ModelAttribute Employee employee,
                              @RequestParam String username,
                              @RequestParam String password,
                              @RequestParam RoleName roleName) {
        // Create User account first
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(employee.getEmail())
                .active(true)
                .build();

        Role role = roleRepository.findByName(roleName).orElse(null);
        if (role != null) {
            user.setRoles(new HashSet<>(Collections.singletonList(role)));
        }
        
        User savedUser = userRepository.save(user);

        // Save Employee and bind user
        employee.setUser(savedUser);
        employeeService.saveEmployee(employee);

        // Log the action
        reportService.logAction("EMPLOYEE_CREATED", "New employee " + employee.getFirstName() + " " + employee.getLastName() + " created with username: " + username, "ADMIN");

        return "redirect:/admin";
    }

    @PostMapping("/user/role")
    public String updateUserRole(@RequestParam Long userId, @RequestParam RoleName roleName) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            Role role = roleRepository.findByName(roleName).orElse(null);
            if (role != null) {
                user.setRoles(new HashSet<>(Collections.singletonList(role)));
                userRepository.save(user);
                reportService.logAction("USER_ROLE_UPDATED", "Role of user " + user.getUsername() + " changed to " + roleName, "ADMIN");
            }
        }
        return "redirect:/admin";
    }

    @PostMapping("/user/status")
    public String toggleUserStatus(@RequestParam Long userId) {
        Optional<User> optUser = userRepository.findById(userId);
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setActive(!user.isActive());
            userRepository.save(user);
            reportService.logAction("USER_STATUS_TOGGLED", "User " + user.getUsername() + " active status toggled to " + user.isActive(), "ADMIN");
        }
        return "redirect:/admin";
    }
}
