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
    public String createEmployee(
            @Valid @RequestBody Employee employee) {

        return service.createEmployee(employee);
    }

    @PostMapping("/create/multiple")
    public String createMultipleEmployees(@Valid @RequestBody List<Employee> employees) {
        return service.createMultipleEmployees(employees);
    }

    @GetMapping("/all")
    public List<EmployeeDTO> getAllEmployees() {

        return service.getAllEmployees();
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

