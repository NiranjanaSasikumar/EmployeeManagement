package com.example.EmployeManagement.EmployeeHome;

import com.example.EmployeManagement.Department.Department;
import com.example.EmployeManagement.Department.DepartmentRepository;
import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.DTO.EmployeeDTO;
import com.example.EmployeManagement.DTO.PaginationMetadata;
import com.example.EmployeManagement.Util.ExperienceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final DepartmentRepository departmentRepository;
    private final EncryptionUtil encryptionUtil;

    private static final Logger logger =
            LoggerFactory.getLogger(EmployeeService.class);


    private EmployeeDTO convertToDTO(Employee employee) {

        EmployeeDTO dto = new EmployeeDTO();

        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setAge(employee.getAge());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setDateOfJoining(employee.getDateOfJoining());
        dto.setExperience(employee.getExperience());
        dto.setSalary(employee.getSalary());
        dto.setDepartment(
                employee.getDept().getName());
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

    private void validateEmail(String email) {

        logger.info("Validating employee email");

        if (email == null ||
                !email.matches(
                        "^[A-Za-z0-9+_.-]+@(.+)$")) {

            logger.error(
                    "Email validation failed for value: {}",
                    email);

            throw new RuntimeException(
                    "Invalid email format");
        }
        logger.info("Email validation successful");
    }

    private void validatePhoneNo(String phoneNo) {

        logger.info("Validating employee phone number");

        if (phoneNo == null ||
                !phoneNo.matches(
                        "^[6-9][0-9]{9}$")) {

            logger.error(
                    "Phone number validation failed for value: {}",
                    phoneNo);

            throw new RuntimeException(
                    "Invalid phone number");
        }
        logger.info("Phone number validation successful");
    }

    public ApiResponse<EmployeeDTO> createEmployee(Employee employee) {

        logger.info("Received request to create employee with ID {}",
                employee.getId());


        if (repository.existsById(employee.getId())) {

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

        Department dept =
                departmentRepository.findById(
                                employee.getDept().getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Department not found"));

        employee.setDept(dept);

        employee.setSalary(
                calculateSalary(
                        dept.getName(),
                        employee.getExperience()));

        validateEmail(employee.getEmail());
        validatePhoneNo(employee.getPhoneNo());

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

            Department dept =
                    departmentRepository.findById(
                                    employee.getDept().getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Department not found"));

            employee.setDept(dept);

            employee.setSalary(
                    calculateSalary(
                            dept.getName(),
                            employee.getExperience()));

            validateEmail(employee.getEmail());
            validatePhoneNo(employee.getPhoneNo());

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

        if(updatedEmployee.getDept() != null) {

            Department dept =
                    departmentRepository.findById(
                                    updatedEmployee.getDept().getId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Department not found"));

            employee.setDept(dept);
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

            validateEmail(
                    updatedEmployee.getEmail());

            employee.setEmail(
                    encryptionUtil.encrypt(
                            updatedEmployee.getEmail()));
        }

        if(updatedEmployee.getPhoneNo() != null) {

            validatePhoneNo(
                    updatedEmployee.getPhoneNo());

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


    public ApiResponse<List<EmployeeDTO>> searchEmployees(

            String name,
            String department,
            Integer age,
            List<String> sortBy) {

        logger.info(
                "Search request received. Name: {}, Department: {}, Age: {}",
                name,
                department,
                age);


        Specification<Employee> spec =
                Specification.allOf();

        if(name != null) {

            spec = spec.and(
                    EmployeeSpecification.hasName(
                            name));

            logger.info(
                    "Searching employees by name: {}",
                    name);

        }

        if(department != null) {

            spec = spec.and(
                    EmployeeSpecification.hasDepartment(
                            department));

            logger.info(
                    "Searching employees by department: {}",
                    department);

        }

        if(age != null) {

            spec = spec.and(
                    EmployeeSpecification.hasAge(
                            age));


            logger.info(
                    "Searching employees by age: {}",
                    age);


        }

         if(name == null
                 && department == null
                 && age == null){

            logger.error(
                    "Search failed. No search parameter provided");

            throw new RuntimeException(
                    "Please provide at least one search parameter");
        }

        List<Employee> employees =
                repository.findAll(spec);

        if(sortBy != null && !sortBy.isEmpty()) {

            Comparator<Employee> comparator = null;

            for(String sortField : sortBy) {

                String[] parts = sortField.split(",");

                if(parts.length != 2) {

                    throw new RuntimeException(
                            "Invalid sort format. Use field,direction");
                }

                String field =
                        parts[0].trim().toLowerCase();

                String direction =
                        parts[1].trim().toLowerCase();

                Comparator<Employee> currentComparator;

                switch(field) {

                    case "name":

                        currentComparator =
                                Comparator.comparing(
                                        Employee::getName,
                                        String.CASE_INSENSITIVE_ORDER);
                        break;

                    case "age":

                        currentComparator =
                                Comparator.comparing(
                                        Employee::getAge);
                        break;

                    case "department":

                        currentComparator =
                                Comparator.comparing(
                                        employee ->
                                                employee.getDept()
                                                        .getName(),
                                        String.CASE_INSENSITIVE_ORDER);
                        break;

                    default:

                        throw new RuntimeException(
                                "Invalid sort field: " + field);
                }

                if(direction.equals("desc")) {

                    currentComparator =
                            currentComparator.reversed();

                } else if(!direction.equals("asc")) {

                    throw new RuntimeException(
                            "Invalid sort direction: "
                                    + direction);
                }

                comparator = comparator == null
                        ? currentComparator
                        : comparator.thenComparing(
                        currentComparator);
            }

            employees = employees.stream()
                    .sorted(comparator)
                    .toList();
        }

        if(employees.isEmpty()) {

            logger.error(
                    "No employees found for the given search criteria");

            throw new RuntimeException(
                    "No employees found for the given search criteria");
        }

        logger.info(
                "{} employees found",
                employees.size());


        List<EmployeeDTO> employeeDTOs =
                employees.stream()
                        .map(this::convertToDTO)
                        .toList();

        return new ApiResponse<>(
                "SUCCESS",
                employees.size() + " employee(s) found",
                employeeDTOs,
                null
        );
    }

}
