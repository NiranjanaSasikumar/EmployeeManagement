package com.example.EmployeManagement.EmployeeHome;

import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.DTO.EmployeeDTO;
import com.example.EmployeManagement.DTO.PaginationMetadata;
import com.example.EmployeManagement.Util.ExperienceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EncryptionUtil encryptionUtil;

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeService.class);


    private EmployeeDTO convertToDTO(Employee employee) {

        EmployeeDTO dto = new EmployeeDTO();

        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setDepartment(employee.getDepartment());
        dto.setAge(employee.getAge());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setDateOfJoining(employee.getDateOfJoining());
        dto.setExperience(employee.getExperience());
        dto.setSalary(employee.getSalary());
        dto.setEmail(
                encryptionUtil.decrypt(
                        employee.getEmail()));

        dto.setPhoneNo(
                encryptionUtil.decrypt(
                        employee.getPhoneNo()));
        return dto;
    }

    public Double calculateSalary(
            String department,
            Integer experience) {

        double baseSalary;

        switch (department.toUpperCase()) {

            case "IT":
                baseSalary = 50000;
                break;

            case "HR":
                baseSalary = 40000;
                break;

            case "FINANCE":
                baseSalary = 55000;
                break;

            case "MARKETING":
                baseSalary = 45000;
                break;

            case "COMPUTER SCIENCE":
                baseSalary = 60000;
                break;

            default:
                throw new RuntimeException(
                        "Invalid department");
        }

        return baseSalary + (experience * 5000);
    }

    public ApiResponse<EmployeeDTO> createEmployee(Employee employee) {

        logger.info("Received request to create employee with ID {}",
                employee.getId());


        if(repository.existsById(employee.getId())) {

            logger.error("Employee with ID {} already exists",
                    employee.getId());

            throw new RuntimeException(
                    "Employee with ID "
                            + employee.getId()
                            + " already exists");
        }

        Integer experience =
                ExperienceUtil.calculateExperience(
                        employee.getDateOfJoining());

        employee.setExperience(experience);


        employee.setSalary(
                calculateSalary(employee.getDepartment(),employee.getExperience())
        );

        employee.setEmail(
                encryptionUtil.encrypt(
                        employee.getEmail()));

        employee.setPhoneNo(
                encryptionUtil.encrypt(
                        employee.getPhoneNo()));

        Employee savedEmployee =
                repository.save(employee);

        logger.info("Employee created successfully with ID {}",
                savedEmployee.getId());

        return new ApiResponse<>(
                "SUCCESS",
                "Employee created successfully",
                convertToDTO(savedEmployee),
                null
        );

    }

    public ApiResponse<List<EmployeeDTO>> createMultipleEmployees(
            List<Employee> employees) {

        logger.info("Received request to create {} employees",
                employees.size());

        for(Employee employee : employees) {

            if(repository.existsById(employee.getId())) {

                logger.error("Employee with id {} already exists",
                        employee.getId());


                throw new RuntimeException(
                        "Employee with ID "
                                + employee.getId()
                                + " already exists");
            }

            Integer experience =
                    ExperienceUtil.calculateExperience(
                            employee.getDateOfJoining());

            employee.setExperience(experience);

            employee.setSalary(

                    calculateSalary(employee.getDepartment(),employee.getExperience())
            );

            employee.setEmail(
                    encryptionUtil.encrypt(
                            employee.getEmail()));

            employee.setPhoneNo(
                    encryptionUtil.encrypt(
                            employee.getPhoneNo()));
        }

        List<Employee> savedEmployees =
                repository.saveAll(employees);

        logger.info("{} employees created successfully",
                savedEmployees.size());

        List<EmployeeDTO> employeeDTOs =
                savedEmployees.stream()
                        .map(this::convertToDTO)
                        .toList();

        return new ApiResponse<>(
                "SUCCESS",
                "Employees created successfully",
                employeeDTOs,
                null
        );
    }


    public ApiResponse<Page<EmployeeDTO>> getAllEmployees(int page,
                                             int size,
                                             String sortBy,
                                             String direction) {

        logger.info(
                "Fetching employees. Page: {}, Size: {}, Sort By: {}, Direction: {}",
                page,
                size,
                sortBy,
                direction);

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Page<Employee> employees =
                repository.findAll(
                        PageRequest.of(page, size, sort));

        if(employees.isEmpty()) {

            logger.error(
                    "No employees found. Page: {}, Size: {}",
                    page,
                    size);

            throw new RuntimeException("No employees found");
        }

        Page<EmployeeDTO> employeeDTOPage =
                employees.map(this::convertToDTO);

        PaginationMetadata metadata =
                new PaginationMetadata(
                        employeeDTOPage.getNumber() + 1,
                        employeeDTOPage.getTotalPages(),
                        employeeDTOPage.getTotalElements(),
                        employeeDTOPage.getSize()
                );

        logger.info(
                "Successfully fetched {} employees from page {}",
                employees.getNumberOfElements(),
                page);


        return new ApiResponse<>(
                "SUCCESS",
                "Employees retrieved successfully",
                employeeDTOPage,
                metadata
        );

    }

    public ApiResponse<EmployeeDTO> getEmployeeById(Integer id) {

        Employee employee = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        logger.info("Employee found with ID {}", id);

        return new ApiResponse<>(
                "SUCCESS",
                "Employee retrieved successfully",
                convertToDTO(employee),
                null
        );

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

        if(updatedEmployee.getEmail() != null) {
            employee.setEmail(
                    encryptionUtil.encrypt(
                            updatedEmployee.getEmail()));
        }

        if(updatedEmployee.getPhoneNo() != null) {
            employee.setPhoneNo(
                    encryptionUtil.encrypt(
                            updatedEmployee.getPhoneNo()));
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
