package com.example.EmployeManagement.Department;

import com.example.EmployeManagement.DTO.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository repository;

    private DepartmentDTO convertToDTO(
            Department department) {

        DepartmentDTO dto =
                new DepartmentDTO();

        dto.setId(
                department.getId());

        dto.setName(
                department.getName());

        return dto;
    }

    public ApiResponse<DepartmentDTO> createDepartment(
            Department department) {

        log.info(
                "Received request to create department: {}",
                department.getName());

        if(repository.findByName(
                department.getName()).isPresent()) {

            log.error(
                    "Department already exists: {}",
                    department.getName());

            throw new RuntimeException(
                    "Department already exists");
        }

        Department savedDepartment =
                repository.save(department);

        log.info(
                "Department created successfully with ID {}",
                savedDepartment.getId());

        return new ApiResponse<>(
                "SUCCESS",
                "Department created successfully",
                convertToDTO(savedDepartment),
                null
        );
    }

    public ApiResponse<List<DepartmentDTO>> getAllDepartments() {

        log.info(
                "Fetching all departments");

        List<DepartmentDTO> departments =
                repository.findAll()
                        .stream()
                        .map(this::convertToDTO)
                        .toList();

        return new ApiResponse<>(
                "SUCCESS",
                "Departments fetched successfully",
                departments,
                null
        );
    }

    public ApiResponse<DepartmentDTO> getDepartmentById(Integer id) {

        log.info(
                "Fetching department with ID {}",
                id);

        Department department =
                repository.findById(id)
                        .orElseThrow(() -> {

                            log.error(
                                    "Department not found with ID {}",
                                    id);

                            return new RuntimeException(
                                    "Department not found");
                        });

        return new ApiResponse<>(
                "SUCCESS",
                "Department fetched successfully",
                convertToDTO(department),
                null
        );
    }

    public ApiResponse<DepartmentDTO> updateDepartment(
            Integer id,
            Department department) {

        log.info(
                "Updating department with ID {}",
                id);

        Department existingDepartment =
                repository.findById(id)
                        .orElseThrow(() -> {

                            log.error(
                                    "Department not found with ID {}",
                                    id);

                            return new RuntimeException(
                                    "Department not found");
                        });

        existingDepartment.setName(
                department.getName());

        Department updatedDepartment =
                repository.save(
                        existingDepartment);

        log.info(
                "Department updated successfully");

        return new ApiResponse<>(
                "SUCCESS",
                "Department updated successfully",
                convertToDTO(updatedDepartment),
                null
        );
    }


    public ApiResponse<String> deleteDepartment(Integer id) {

        log.info(
                "Deleting department with ID {}",
                id);

        Department department =
                repository.findById(id)
                        .orElseThrow(() -> {

                            log.error(
                                    "Department not found with ID {}",
                                    id);

                            return new RuntimeException(
                                    "Department not found");
                        });

        repository.delete(department);

        log.info(
                "Department deleted successfully");

        return new ApiResponse<>(
                "SUCCESS",
                "Department deleted successfully",
                "Department removed",
                null
        );
    }

}
