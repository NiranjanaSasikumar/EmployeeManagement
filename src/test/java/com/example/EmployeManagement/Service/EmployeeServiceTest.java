package com.example.EmployeManagement.Service;

import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.DTO.EmployeeDTO;
import com.example.EmployeManagement.EmployeeHome.Employee;
import com.example.EmployeManagement.EmployeeHome.EmployeeRepository;
import com.example.EmployeManagement.EmployeeHome.EmployeeService;
import com.example.EmployeManagement.Util.ExperienceUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository repository;

    @InjectMocks
    private EmployeeService service;

    private Employee createTestEmployee() {

        Employee employee = new Employee();

        employee.setId(1);
        employee.setName("Arjun");
        employee.setDepartment("IT");
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
    void createEmployee_ShouldCalculateSalaryCorrectly() {

        Employee employee = createTestEmployee();

        when(repository.existsById(1))
                .thenReturn(false);

        when(repository.save(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ApiResponse<EmployeeDTO> response =
                service.createEmployee(employee);

        assertNotNull(response);

        EmployeeDTO dto = response.getData();

        assertEquals(75000.0, dto.getSalary());
    }

    @Test
    void createEmployee_ShouldThrowException_ForInvalidDepartment() {

        Employee employee = createTestEmployee();

        employee.setDepartment("XYZ");

        when(repository.existsById(1))
                .thenReturn(false);

        RuntimeException exception =
                assertThrows(
                        RuntimeException.class,
                        () -> service.createEmployee(employee)
                );

        assertEquals(
                "Invalid department",
                exception.getMessage()
        );
    }

    @Test
    void createEmployee_StaticMockingTest() {

        Employee employee = createTestEmployee();

        when(repository.existsById(1))
                .thenReturn(false);

        when(repository.save(any(Employee.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0));

        try (MockedStatic<ExperienceUtil> mocked =
                     Mockito.mockStatic(ExperienceUtil.class)) {

            mocked.when(() ->
                            ExperienceUtil.calculateExperience(
                                    employee.getDateOfJoining()))
                    .thenReturn(10);

            ApiResponse<EmployeeDTO> response =
                    service.createEmployee(employee);

            assertEquals(
                    10,
                    response.getData().getExperience());
        }
    }
}
