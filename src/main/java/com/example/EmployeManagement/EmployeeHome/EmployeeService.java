package com.example.EmployeManagement.EmployeeHome;

import com.example.EmployeManagement.DTO.*;
import com.example.EmployeManagement.Department.Department;
import com.example.EmployeManagement.Department.DepartmentRepository;
import com.example.EmployeManagement.Redis.EmployeePublisher;
import com.example.EmployeManagement.Util.ExperienceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final EmployeePublisher employeePublisher;
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
        dto.setDepartment(
                employee.getDept().getName());
        dto.setEmail(
                encryptionUtil.decrypt(
                        employee.getEmail()));

        dto.setPhoneNo(
                encryptionUtil.decrypt(
                        employee.getPhoneNo()));
        dto.setPanCardNo(employee.getPanCardNo());
        return dto;
    }

    private AdminEmployeeDTO convertToAdminDTO(Employee employee) {

        AdminEmployeeDTO dto = new AdminEmployeeDTO();

        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setDepartment(employee.getDept().getName());
        dto.setAge(employee.getAge());
        dto.setDateOfBirth(employee.getDateOfBirth());
        dto.setDateOfJoining(employee.getDateOfJoining());
        dto.setExperience(employee.getExperience());
        dto.setSalary(employee.getSalary());
        dto.setEmail(employee.getEmail());
        dto.setPhoneNo(employee.getPhoneNo());
        dto.setPanCardNo(employee.getPanCardNo());

        return dto;
    }

    private String getCurrentUserRole() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication.getAuthorities()
                .stream()
                .findFirst()
                .get()
                .getAuthority();
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

    private void validateEmployeeId(Integer employeeId) {

        if (repository.existsById(employeeId)) {

            logger.error("Employee with ID {} already exists", employeeId);

            throw new RuntimeException(
                    "Employee with ID "
                            + employeeId
                            + " already exists");
        }
    }

    private void validatePanCardNo(String panCardNo) {

        if (repository.existsByPanCardNo(panCardNo)) {
            throw new RuntimeException(
                    "PAN Card Number " + panCardNo + " already exists");
        }
    }

    public ApiResponse<EmployeeDTO> createEmployee(Employee employee) {

        logger.info("Received request to create employee with ID {}",
                employee.getId());


        validateEmployeeId(employee.getId());

        validatePanCardNo(employee.getPanCardNo());

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

        employeePublisher.publishEmployeeCreated(savedEmployee.getId());

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

            validateEmployeeId(employee.getId());

            validatePanCardNo(employee.getPanCardNo());

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

        for (Employee employee : savedEmployees) {
            employeePublisher.publishEmployeeCreated(employee.getId());
        }

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


    public ApiResponse<Page<?>> getAllEmployees(int page,
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

        String role = getCurrentUserRole();

        Page<?> dtoPage;

        if(role.equals("ROLE_USER")) {

            dtoPage = employees.map(this::convertToDTO);

        } else {

            dtoPage = employees.map(this::convertToAdminDTO);

        }

        PaginationMetadata metadata =
                new PaginationMetadata(
                        dtoPage.getNumber() + 1,
                        dtoPage.getTotalPages(),
                        dtoPage.getTotalElements(),
                        dtoPage.getSize()
                );

        logger.info(
                "Successfully fetched {} employees from page {}",
                employees.getNumberOfElements(),
                page);

        return new ApiResponse<>(
                "SUCCESS",
                "Employees retrieved successfully",
                dtoPage,
                metadata
        );

    }

    public ApiResponse<Object> getEmployeeById(Integer id) {

        Employee employee =
                repository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found with id " + id
                                ));

        logger.info("Employee found with ID {}", id);

        String role = getCurrentUserRole();

        Object dto;

        if(role.equals("ROLE_USER")) {

            dto = convertToDTO(employee);

        } else {

            dto = convertToAdminDTO(employee);

        }

        return new ApiResponse<>(
                "SUCCESS",
                "Employee retrieved successfully",
                dto,
                null
        );

    }

    public ApiResponse<AdminEmployeeDTO> updateEmployee(
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

        Employee savedEmployee = repository.save(employee);

        return new ApiResponse<>(
                "SUCCESS",
                "Employee updated successfully",
                convertToAdminDTO(savedEmployee),
                null
        );
    }

    public ApiResponse<Object> deleteEmployee(Integer id) {

        repository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));

        repository.deleteById(id);

        logger.info("Employee deleted successfully with ID {}", id);

        return new ApiResponse<>(
                "SUCCESS",
                "Employee deleted successfully",
                null,
                null
        );
    }


    public ApiResponse<List<?>> searchEmployees(

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


        String role = getCurrentUserRole();

        List<?> dtoList;

        if(role.equals("ROLE_USER")) {

            dtoList = employees.stream()
                    .map(this::convertToDTO)
                    .toList();

        } else {

            dtoList = employees.stream()
                    .map(this::convertToAdminDTO)
                    .toList();
        }

        return new ApiResponse<>(
                "SUCCESS",
                employees.size() + " employee(s) found",
                dtoList,
                null
        );
    }

    public ApiResponse<SalaryIncrementResponseDTO> incrementSalary(
            Integer id,
            SalaryIncrementRequestDTO request) {

        logger.info(
                "Salary increment requested for employee id {}",
                id);

        Employee employee = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with id " + id));

        Double oldSalary = employee.getSalary();

        Double newSalary =
                oldSalary +
                        (oldSalary * request.getPercentage() / 100);

        employee.setSalary(newSalary);

        repository.save(employee);

        SalaryIncrementResponseDTO responseDTO =
                new SalaryIncrementResponseDTO(
                        employee.getId(),
                        employee.getName(),
                        oldSalary,
                        request.getPercentage(),
                        newSalary
                );

        logger.info(
                "Salary updated successfully for employee id {}. Old salary: {}, New salary: {}",
                id,
                oldSalary,
                newSalary);

        return new ApiResponse<>(
                "SUCCESS",
                "Salary increment applied successfully",
                responseDTO,
                null
        );
    }

}
