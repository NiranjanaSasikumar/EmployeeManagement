package com.example.EmployeManagement.Department;

import com.example.EmployeManagement.DTO.ApiResponse;
import com.example.EmployeManagement.Util.ApiRoutes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoutes.DEPARTMENT)
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping("/create")
    public ApiResponse<DepartmentDTO> createDepartment(
            @Valid @RequestBody Department department) {

        return service.createDepartment(department);
    }

    @GetMapping("/all")
    public ApiResponse<?> getAllDepartments() {

        return service.getAllDepartments();
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getDepartmentById(
            @PathVariable Integer id) {

        return service.getDepartmentById(id);
    }

    @PutMapping("/update/{id}")
    public ApiResponse<?> updateDepartment(
            @PathVariable Integer id,
            @Valid @RequestBody Department department) {

        return service.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteDepartment(
            @PathVariable Integer id) {

        return service.deleteDepartment(id);
    }

}
