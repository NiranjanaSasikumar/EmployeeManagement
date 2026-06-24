package com.example.EmployeManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalaryIncrementResponseDTO {

    private Integer employeeId;
    private String employeeName;
    private Double oldSalary;
    private Double incrementPercentage;
    private Double newSalary;

}
