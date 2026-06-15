package com.example.EmployeManagement.EmployeeHome;

import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.DTO.EmployeeDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


@RestController
@RequestMapping("/employee")
@Tag(name = "Employee Management", description = "APIs for managing employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @PostMapping("/create")
    @Operation(
            summary = "Create Employee",
            description = "Creates a new employee and calculates salary based on experience"
    )
    public ApiResponse<EmployeeDTO> createEmployee(
            @Valid @RequestBody Employee employee) {

        return service.createEmployee(employee);
    }


    @PostMapping("/create/multiple")
    @Operation(
            summary = "Create Multiple Employee",
            description = "Creates multiple employee records and calculates salary based on experience"
    )
    public ApiResponse<List<EmployeeDTO>> createMultipleEmployees(
            @Valid @RequestBody List<Employee> employees) {

        return service.createMultipleEmployees(employees);
    }


    @GetMapping("/all")
    @Operation(
            summary = "Get All Employee",
            description = "Fetches details of all employees"
    )
    public ApiResponse<Page<EmployeeDTO>> getAllEmployees(@RequestParam(required = false , defaultValue = "1") int page,
                                                          @RequestParam(required = false , defaultValue = "10") int size,
                                                          @RequestParam(required = false , defaultValue = "id") String sortBy,
                                                          @RequestParam(required = false , defaultValue = "asc") String direction) {

        return service.getAllEmployees(page-1, size, sortBy, direction);

    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Get Employee By ID",
            description = "Fetch an employee using employee ID"
    )
    public ApiResponse<EmployeeDTO> getEmployeeById(@PathVariable Integer id) {
        return
                service.getEmployeeById(id);
    }


    @PutMapping("/update/{id}")
    @Operation(
            summary = "Update Employee",
            description = "Updates employee details using employee ID"
    )

    public Employee updateEmployee(
            @PathVariable Integer id,
            @RequestBody Employee employee) {

        return service.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete Employee",
            description = "Deletes employee using employee ID"
    )
    public String deleteEmployee(
            @PathVariable Integer id) {

        return service.deleteEmployee(id);
    }
}