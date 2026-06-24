package com.example.EmployeManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminEmployeeDTO {

    private Integer id;
    private String name;
    private String department;
    private Integer age;
    private LocalDate dateOfBirth;
    private LocalDate dateOfJoining;
    private Integer experience;
    private Double salary;
    private String email;
    private String phoneNo;

}
