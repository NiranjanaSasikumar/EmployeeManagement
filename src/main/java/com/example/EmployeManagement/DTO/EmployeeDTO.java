package com.example.EmployeManagement.DTO;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Employee Response DTO")
public class EmployeeDTO {

    private Integer id;
    private String name;
    private String department;
    private Integer age;
    private LocalDate dateOfBirth;
    private LocalDate dateOfJoining;
    private Integer experience;
    private String email;
    private String phoneNo;
    private String panCardNo;

}
