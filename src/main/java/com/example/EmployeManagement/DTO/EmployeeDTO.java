package com.example.EmployeManagement.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
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

    public EmployeeDTO() {
    }

    public EmployeeDTO(Integer id, String name,
                    String department,
                    Integer age,
                    LocalDate dateOfBirth,
                    LocalDate dateOfJoining,
                    Integer experience,
                    Double salary   ) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.dateOfJoining = dateOfJoining;
        this.experience = experience;
        this.salary = salary;
    }
}
