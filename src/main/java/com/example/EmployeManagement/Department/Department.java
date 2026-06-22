package com.example.EmployeManagement.Department;

import com.example.EmployeManagement.EmployeeHome.Employee;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Department {
    @Id
    private Integer id;

    @NotBlank(message = "Department name cannot be empty")
    @Size(
            min = 2,
            max = 50,
            message = "Department name must be between 2 and 50 characters"
    )
    private String name;

    @OneToMany(mappedBy = "dept")
    @JsonIgnore
    private List<Employee> employees;
}
