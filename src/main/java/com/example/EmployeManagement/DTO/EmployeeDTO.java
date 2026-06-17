package com.example.EmployeManagement.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Employee Response DTO")
public class EmployeeDTO {

    @Id
    private Integer id;
    private String name;
    private String department;
    private Integer age;
    private LocalDate dateOfBirth;
    private LocalDate dateOfJoining;
    private Integer experience;
    private Double salary;

}
