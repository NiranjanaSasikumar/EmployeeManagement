package com.example.EmployeManagement.Department;

import com.example.EmployeManagement.DTO.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository repository;

    private static final Logger logger =
            LoggerFactory.getLogger(
                    DepartmentService.class);

    public ApiResponse<Department> createDepartment(
            Department department) {

        logger.info(
                "Received request to create department: {}",
                department.getName());

        if(repository.findByName(
                department.getName()).isPresent()) {

            logger.error(
                    "Department already exists: {}",
                    department.getName());

            throw new RuntimeException(
                    "Department already exists");
        }

        Department savedDepartment =
                repository.save(department);

        logger.info(
                "Department created successfully with ID {}",
                savedDepartment.getId());

        return new ApiResponse<>(
                "SUCCESS",
                "Department created successfully",
                savedDepartment,
                null
        );
    }

    public ApiResponse<List<Department>> getAllDepartments() {

        logger.info(
                "Fetching all departments");

        List<Department> departments =
                repository.findAll();

        return new ApiResponse<>(
                "SUCCESS",
                "Departments fetched successfully",
                departments,
                null
        );
    }

    public ApiResponse<Department> getDepartmentById(Integer id) {

        logger.info(
                "Fetching department with ID {}",
                id);

        Department department =
                repository.findById(id)
                        .orElseThrow(() -> {

                            logger.error(
                                    "Department not found with ID {}",
                                    id);

                            return new RuntimeException(
                                    "Department not found");
                        });

        return new ApiResponse<>(
                "SUCCESS",
                "Department fetched successfully",
                department,
                null
        );
    }

    public ApiResponse<Department> updateDepartment(
            Integer id,
            Department department) {

        logger.info(
                "Updating department with ID {}",
                id);

        Department existingDepartment =
                repository.findById(id)
                        .orElseThrow(() -> {

                            logger.error(
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

        logger.info(
                "Department updated successfully");

        return new ApiResponse<>(
                "SUCCESS",
                "Department updated successfully",
                updatedDepartment,
                null
        );
    }


    public ApiResponse<String> deleteDepartment(Integer id) {

        logger.info(
                "Deleting department with ID {}",
                id);

        Department department =
                repository.findById(id)
                        .orElseThrow(() -> {

                            logger.error(
                                    "Department not found with ID {}",
                                    id);

                            return new RuntimeException(
                                    "Department not found");
                        });

        repository.delete(department);

        logger.info(
                "Department deleted successfully");

        return new ApiResponse<>(
                "SUCCESS",
                "Department deleted successfully",
                "Department removed",
                null
        );
    }

}
