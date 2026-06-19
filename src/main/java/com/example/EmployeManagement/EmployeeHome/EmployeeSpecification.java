package com.example.EmployeManagement.EmployeeHome;

import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee>
    hasName(String name) {

        return (root, query, criteriaBuilder) ->

                criteriaBuilder.like(
                        criteriaBuilder.lower(
                                root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    public static Specification<Employee> hasAge(
            Integer age) {

        return (root, query, cb) ->
                cb.equal(
                        root.get("age"),
                        age);
    }

    public static Specification<Employee>
    hasDepartment(String department) {

        return (root, query, criteriaBuilder) ->

                criteriaBuilder.equal(
                        criteriaBuilder.lower(
                                root.join("dept")
                                        .get("name")),
                        department.toLowerCase()
                );
    }
}
