package com.example.EmployeManagement.Controller;


import com.example.EmployeManagement.Auth.AuthenticationService;
import com.example.EmployeManagement.Auth.JwtFilter;
import com.example.EmployeManagement.DTO.AdminEmployeeDTO;
import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.DTO.EmployeeDTO;
import com.example.EmployeManagement.EmployeeHome.Employee;
import com.example.EmployeManagement.EmployeeHome.EmployeeController;
import com.example.EmployeManagement.EmployeeHome.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Arrays;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EmployeeService service;

    @MockitoBean
    private JwtFilter jwtFilter;

    @MockitoBean
    private AuthenticationService authenticationService;

    @Test
    void createEmployee_Success() throws Exception {

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(1);
        dto.setName("Arjun");

        ApiResponse<EmployeeDTO> response =
                new ApiResponse<>("SUCCESS",
                        "Employee created successfully",
                        dto,
                        null);

        when(service.createEmployee(any()))
                .thenReturn(response);


        String employeeJson = """
                {
                  "id": 1,
                  "name": "Arjun",
                  "department": "IT",
                  "age": 25,
                  "dateOfBirth": "2000-07-18",
                  "dateOfJoining": "2020-08-04",
                  "password": "arjun123"
                }
                """;

        mockMvc.perform(post("/employee/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(employeeJson))
                .andExpect(status().isOk());
    }

    @Test
    void createMultipleEmployees_Success() throws Exception {

        EmployeeDTO dto1 = new EmployeeDTO();
        dto1.setId(1);
        dto1.setName("Arjun");

        EmployeeDTO dto2 = new EmployeeDTO();
        dto2.setId(2);
        dto2.setName("Rahul");

        List<EmployeeDTO> employeeList =
                Arrays.asList(dto1, dto2);

        ApiResponse<List<EmployeeDTO>> response =
                new ApiResponse<>(
                        "SUCCESS",
                        "Employees created successfully",
                        employeeList,
                        null
                );

        when(service.createMultipleEmployees(anyList()))
                .thenReturn(response);

        String employeesJson = """
            [
              {
                "id":1,
                "name":"Arjun",
                "department":"IT",
                "age":25,
                "dateOfBirth":"2000-07-18",
                "dateOfJoining":"2020-08-04",
                "password":"arjun123"
              },
              {
                "id":2,
                "name":"Rahul",
                "department":"HR",
                "age":28,
                "dateOfBirth":"1997-05-10",
                "dateOfJoining":"2019-03-15",
                "password":"rahul123"
              }
            ]
            """;

        mockMvc.perform(
                        post("/employee/create/multiple")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(employeesJson))
                .andExpect(status().isOk());
    }

    @Test
    void getEmployeeById_Success() throws Exception {

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(1);
        dto.setName("Arjun");

        ApiResponse<Object> response =
                new ApiResponse<>(
                        "SUCCESS",
                        "Employee retrieved successfully",
                        dto,
                        null
                );

        when(service.getEmployeeById(1))
                .thenReturn(response);

        mockMvc.perform(get("/employee/1"))
                .andExpect(status().isOk());

    }

    @Test
    void getAllEmployees_Success() throws Exception {

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(1);
        dto.setName("Arjun");

        Page<EmployeeDTO> page =
                new PageImpl<>(List.of(dto));

        ApiResponse<Page<?>> response =
                new ApiResponse<>(
                        "SUCCESS",
                        "Employees retrieved successfully",
                        page,
                        null
                );

        when(service.getAllEmployees(
                anyInt(),
                anyInt(),
                anyString(),
                anyString()))
                .thenReturn(response);

        mockMvc.perform(
                        get("/employee/all")
                                .param("page", "1")
                                .param("size", "10")
                                .param("sortBy", "id")
                                .param("direction", "asc"))
                .andExpect(status().isOk());
    }

    @Test
    void updateEmployee_Success() throws Exception {

        AdminEmployeeDTO employeeDTO = new AdminEmployeeDTO();
        employeeDTO.setId(1);
        employeeDTO.setName("Updated Arjun");

        ApiResponse<AdminEmployeeDTO> response =
                new ApiResponse<>(
                        "SUCCESS",
                        "Employee updated successfully",
                        employeeDTO,
                        null
                );

        when(service.updateEmployee(
                eq(1),
                any(Employee.class)))
                .thenReturn(response);

        String employeeJson = """
            {
              "name":"Updated Arjun"
            }
            """;

        mockMvc.perform(
                        put("/employee/update/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(employeeJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteEmployee_Success() throws Exception {

        ApiResponse<Object> response =
                new ApiResponse<>(
                        "SUCCESS",
                        "Employee deleted successfully",
                        null,
                        null
                );

        when(service.deleteEmployee(1))
                .thenReturn(response);

        mockMvc.perform(delete("/employee/1"))
                .andExpect(status().isOk());
    }


}
