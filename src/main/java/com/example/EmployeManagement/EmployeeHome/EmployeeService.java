package com.example.EmployeManagement.EmployeeHome;

import com.example.EmployeManagement.DTO.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository repository;

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeService.class);

    public EmployeeDTO getEmployeeById(Integer id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        logger.info("Employee found with ID {}", id);
        return convertToDTO(employee);

    }

    private EmployeeDTO convertToDTO(Employee employee) {

        EmployeeDTO dto = new EmployeeDTO();

        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setDepartment(employee.getDepartment());
        dto.setAge(employee.getAge());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setExperience(employee.getExperience());
        dto.setSalary(employee.getSalary());

        return dto;
    }

    private Double calculateSalary(Integer experience) {

        if(experience <= 2) {
            return 25000.0;
        }
        else if(experience <= 5) {
            return 50000.0;
        }
        else {
            return 80000.0;
        }
    }


    public String createEmployee(Employee employee) {
        logger.info("Received request to create employee with ID {}", employee.getId());

        if(repository.existsById(employee.getId())) {

            logger.error("Employee with ID {} already exists",
                    employee.getId());

            throw new RuntimeException(
                    "Employee with ID "
                            + employee.getId()
                            + " already exists");
        }

        employee.setSalary(
                calculateSalary(employee.getExperience())
        );
        repository.save(employee);
        logger.info("Employee created successfully");
        return "Employee created successfully.";
    }

    public String createMultipleEmployees(List<Employee> employees) {

        logger.info("Received request to create {} employees", employees.size());

        for(Employee employee : employees) {

            if(repository.existsById(employee.getId())) {

                logger.error("Employee with ID {} already exists", employee.getId());

                throw new RuntimeException(
                        "Employee with ID "
                                + employee.getId()
                                + " already exists");
            }

            employee.setSalary(
                    calculateSalary(employee.getExperience())
            );
        }

        repository.saveAll(employees);
        logger.info("{} employees created successfully", employees.size());
        return "Employees created successfully";
    }


    public List<EmployeeDTO> getAllEmployees() {

        List<Employee> employees = repository.findAll();

        if(employees.isEmpty()) {

            logger.error("No employees found in database");

            throw new RuntimeException("No employees found");
        }

        logger.info("Employees retrieved successfully");

        return employees.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Employee updateEmployee(
            Integer id,
            Employee updatedEmployee) {

        Employee employee =
                repository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

        if(employee == null) {
            return null;
        }

        if(updatedEmployee.getName() != null) {
            employee.setName(updatedEmployee.getName());
        }

        if(updatedEmployee.getDepartment() != null) {
            employee.setDepartment(
                    updatedEmployee.getDepartment());
        }

        if(updatedEmployee.getAge() != null) {
            employee.setAge(updatedEmployee.getAge());
        }

        if(updatedEmployee.getDateOfBirth() != null) {
            employee.setDateOfBirth(updatedEmployee.getDateOfBirth());
        }

        if(updatedEmployee.getPassword() != null) {
            employee.setPassword(updatedEmployee.getPassword());
        }

        if(updatedEmployee.getExperience() != null) {
            employee.setExperience(
                    updatedEmployee.getExperience());
        }

        logger.info("Employee updated successfully with ID {}", id);

        return repository.save(employee);
    }

    public String deleteEmployee(Integer id) {

        repository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

        repository.deleteById(id);

        logger.info("Employee deleted successfully with ID {}", id);

        return "Employee deleted successfully";
    }
}
