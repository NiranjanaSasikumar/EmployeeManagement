package com.example.EmployeManagement.DTO;

import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
public class EmployeeDTO {

    @Id
    private Integer id;
    private String name;
    private String department;
    private Integer age;
    private LocalDate dateOfBirth;
    private Integer experience;
    private Double salary;

    public EmployeeDTO() {
    }

    public EmployeeDTO(Integer id, String name,
                    String department,
                    Integer age,
                    LocalDate dateOfBirth,
                    Integer experience,
                    Double salary   ) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.age = age;
        this.dateOfBirth= dateOfBirth;
        this.experience = experience;
        this.salary = salary;
    }
}
