package com.example.EmployeManagement.EmployeeHome;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employee {

    @Id
    @NotNull(message = "Id cannot be null")
    private Integer id;

    @NotBlank(message = "Name cannot be empty")
    @Pattern(
            regexp = "^[a-zA-Z ]+$",
            message = "Name must contain only letters"
    )
    private String name;

    @NotBlank(message = "Department cannot be empty")
    private String department;

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 60, message = "Age cannot exceed 60")
    private Integer age;

    private LocalDate dateOfBirth;

    @NotBlank(message = "Password is required")
    private String password;

    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experience;


    private Double salary;

    public Employee() {
    }

    public Employee(Integer id, String name,
                    String department,
                    Integer age,
                    LocalDate dateOfBirth,
                    String password,
                    Integer experience,
                    Double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.age = age;
        this.dateOfBirth = dateOfBirth;
        this.password = password;
        this.experience = experience;
        this.salary = salary;

    }
}
