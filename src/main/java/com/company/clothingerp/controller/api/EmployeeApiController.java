package com.company.clothingerp.controller.api;

import com.company.clothingerp.model.Employee;
import com.company.clothingerp.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/employees")
@Tag(name = "Employee Directory (Admin/ERP)", description = "Endpoints for managing corporate human resources")
public class EmployeeApiController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    @Operation(summary = "Get list of all employees")
    public List<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @PostMapping
    @Operation(summary = "Create or update an employee directory entry")
    public Employee saveEmployee(@RequestBody Employee employee) {
        return employeeService.saveEmployee(employee);
    }
}
