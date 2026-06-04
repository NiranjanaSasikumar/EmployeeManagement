package com.example.EmployeManagement.EmployeeHome;

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
            @RequestBody Employee employee) {

        return service.createEmployee(employee);
    }

    @PostMapping("/create/multiple")
    public String createMultipleEmployees(@RequestBody List<Employee> employees) {
        return service.createMultipleEmployees(employees);
    }

    @GetMapping("/all")
    public List<Employee> getAllEmployees() {

        return service.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employee getEmployeeById(
            @PathVariable Integer id) {

        return service.getEmployeeById(id);
    }

    @PutMapping("/update")
    public String updateEmployee(
            @RequestBody Employee employee) {

        return service.updateEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public String deleteEmployee(
            @PathVariable Integer id) {

        return service.deleteEmployee(id);
    }
}

