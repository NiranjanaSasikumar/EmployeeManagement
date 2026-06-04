package com.example.EmployeManagement.EmployeeHome;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    public String createEmployee(Employee employee) {

        if(repository.existsById(employee.getId())) {
            return "Employee already exists";
        }

        repository.save(employee);
        return "Employee created successfully.";
    }

    public List<Employee> getAllEmployees() {

        return repository.findAll();
    }

    public Employee getEmployeeById(Integer id) {

        Optional<Employee> employee =
                repository.findById(id);

        return employee.orElse(null);
    }

    public String updateEmployee(Employee employee) {

        if(!repository.existsById(employee.getId())) {
            return "Employee not found";
        }

        repository.save(employee);
        return "Employee updated successfully";
    }

    public String deleteEmployee(Integer id) {

        if(!repository.existsById(id)) {
            return "Employee not found";
        }

        repository.deleteById(id);
        return "Employee deleted successfully";
    }
}
