package com.example.EmployeManagement.Service;

import com.example.EmployeManagement.DTO.AdminEmployeeDTO;
import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.DTO.EmployeeDTO;
import com.example.EmployeManagement.Department.Department;
import com.example.EmployeManagement.Department.DepartmentRepository;
import com.example.EmployeManagement.EmployeeHome.Employee;
import com.example.EmployeManagement.EmployeeHome.EmployeeRepository;
import com.example.EmployeManagement.EmployeeHome.EmployeeService;
import com.example.EmployeManagement.EmployeeHome.EncryptionUtil;
import com.example.EmployeManagement.Redis.EmployeePublisher;
import com.example.EmployeManagement.Util.ExperienceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @Mock
    private DepartmentRepository departmentRepository;

    @Mock
    private EncryptionUtil encryptionUtil;

    @Mock
    private EmployeePublisher employeePublisher;

    @InjectMocks
    private EmployeeService service;

    private Employee createTestEmployee() {

        Employee employee = new Employee();

        employee.setId(1);
        employee.setName("Arjun");
        Department department = new Department();
        department.setId(1);
        department.setName("IT");
        employee.setEmail("arjun@test.com");
        employee.setPhoneNo("9876543210");
        employee.setPanCardNo("ABCDE1234F");
        employee.setDept(department);
        employee.setAge(25);
        employee.setDateOfBirth(
                LocalDate.of(2000,7,18)
        );
        employee.setDateOfJoining(
                LocalDate.of(2020,8,4)
        );
        employee.setPassword("arjun123");

        return employee;
    }

    @Test
    void createEmployee_ShouldThrowException_WhenEmployeeAlreadyExists() {

        Employee employee = createTestEmployee();

        when(repository.existsById(1))
                .thenReturn(true);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> service.createEmployee(employee)
                );

        assertEquals(
                "Employee with ID 1 already exists",
                exception.getMessage()
        );
    }

    @Test
    void createEmployee_ShouldThrowException_ForInvalidDepartment() {

        Employee employee = createTestEmployee();

        Department department = new Department();
        department.setId(100);

        employee.setDept(department);

        when(repository.existsById(1))
                .thenReturn(false);

        when(departmentRepository.findById(100))
                .thenReturn(Optional.empty());

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> service.createEmployee(employee)
                );

        assertEquals(
                "Department not found",
                exception.getMessage()
        );
    }

    @Test
    void createEmployee_StaticMockingTest() {

        Employee employee = createTestEmployee();

        when(repository.existsById(1))
                .thenReturn(false);

        when(departmentRepository.findById(1))
                .thenReturn(Optional.of(employee.getDept()));

        when(encryptionUtil.encrypt(anyString()))
                .thenReturn("encryptedValue");

        when(repository.save(any(Employee.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        try (MockedStatic<ExperienceUtil> mocked =
                     Mockito.mockStatic(ExperienceUtil.class)) {

            mocked.when(() ->
                            ExperienceUtil.calculateExperience(
                                    employee.getDateOfJoining()))
                    .thenReturn(10);

            doNothing().when(employeePublisher)
                    .publishEmployeeCreated(anyInt());

            ApiResponse<EmployeeDTO> response =
                    service.createEmployee(employee);

            assertEquals(
                    10,
                    response.getData().getExperience());
        }
    }
}
