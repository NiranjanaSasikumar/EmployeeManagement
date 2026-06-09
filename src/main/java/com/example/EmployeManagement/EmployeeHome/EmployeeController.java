package com.example.EmployeManagement.EmployeeHome;

import com.example.EmployeManagement.DTO.EmployeeDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @PostMapping("/create")
    public EmployeeDTO createEmployee(
            @Valid @RequestBody Employee employee) {

        return service.createEmployee(employee);
    }

    @PostMapping("/create/multiple")
    public List<EmployeeDTO> createMultipleEmployees(
            @Valid @RequestBody List<Employee> employees) {

        return service.createMultipleEmployees(employees);
    }

    @GetMapping("/all")
    public List<EmployeeDTO> getAllEmployees(@RequestParam(required = false , defaultValue = "1") int page,
                                             @RequestParam(required = false , defaultValue = "10") int size,
                                             @RequestParam(required = false , defaultValue = "id") String sortBy,
                                             @RequestParam(required = false , defaultValue = "asc") String direction) {

        return service.getAllEmployees(page-1, size, sortBy, direction);

    }

    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable Integer id) {
        return
                service.getEmployeeById(id);
    }


    @PutMapping("/update/{id}")
    public Employee updateEmployee(
            @PathVariable Integer id,
            @RequestBody Employee employee) {

        return service.updateEmployee(id, employee);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(
            @PathVariable Integer id) {

        return service.deleteEmployee(id);
    }
}

