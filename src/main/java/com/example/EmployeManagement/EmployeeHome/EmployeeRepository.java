package com.example.EmployeManagement.EmployeeHome;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByNameContainingIgnoreCase(
            String name);

    List<Employee> findByDept_NameIgnoreCase(
            String department);

    List<Employee> findByAge(
            Integer age);

}