package com.example.EmployeManagement.EmployeeHome;

import com.example.EmployeManagement.Department.Department;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 60, message = "Age cannot exceed 60")
    private Integer age;

    private LocalDate dateOfBirth;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Date of Joining cannot be null")
    @PastOrPresent(message = "Date of joining cannot be in the future")
    private LocalDate dateOfJoining;

    private Integer experience;

    private Double salary;

    @ManyToOne
    @JoinColumn(name = "dept_id")
    private Department dept;

}
