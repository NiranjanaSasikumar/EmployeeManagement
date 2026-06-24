package com.example.EmployeManagement.DTO;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

@Data
public class SalaryIncrementRequestDTO {

    @DecimalMin(value = "0.1",
            message = "Increment percentage must be greater than 0")
    @DecimalMax(value = "50.0",
            message = "Increment percentage cannot exceed 50")
    private Double percentage;

}
