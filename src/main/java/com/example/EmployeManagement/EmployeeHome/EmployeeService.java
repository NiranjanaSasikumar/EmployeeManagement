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

    public String createMultipleEmployees(List<Employee> employees) {

        for(Employee employee : employees) {

            if(repository.existsById(employee.getId())) {

                return "Employee with ID "
                        + employee.getId()
                        + " already exists";
            }
        }
        repository.saveAll(employees);
        return "Employees created successfully";
    }


    public List<Employee> getAllEmployees() {

        return repository.findAll();
    }

    public Employee getEmployeeById(Integer id) {

        Optional<Employee> employee =
                repository.findById(id);

        return employee.orElse(null);
    }

    public String updateEmployee(
            Integer id,
            String field,
            String value) {

        Employee employee =
                repository.findById(id).orElse(null);

        if(employee == null) {
            return "Employee not found";
        }

        switch(field.toLowerCase()) {

            case "name":
                employee.setName(value);
                break;

            case "department":
                employee.setDepartment(value);
                break;

            case "age":
                employee.setAge(Integer.parseInt(value));
                break;

            case "salary":
                employee.setSalary(Double.parseDouble(value));
                break;

            default:
                return "Invalid field";
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
