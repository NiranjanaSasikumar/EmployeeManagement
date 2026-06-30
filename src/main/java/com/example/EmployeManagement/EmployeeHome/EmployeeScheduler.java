package com.example.EmployeManagement.EmployeeHome;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmployeeScheduler {

    private final EmployeeRepository repository;

    @Value("${employee.salary.increment.percentage}")
    private double annualIncrementPercentage;

    @Scheduled(cron = "${employee.scheduler.cron}")
    public void updateExperienceAndSalary() {

        log.info(
                "Running yearly employee update scheduler");

        List<Employee> employees =
                repository.findAll();

        LocalDate today =
                LocalDate.now();

        for(Employee employee : employees) {

            LocalDate joiningDate =
                    employee.getDateOfJoining();

            if(joiningDate == null) {
                continue;
            }

            if(today.getMonth() ==
                    joiningDate.getMonth()

                    &&

                    today.getDayOfMonth() ==
                            joiningDate.getDayOfMonth()) {

                updateEmployee(employee);
            }
        }

        log.info(
                "Employee update scheduler completed");
    }

    private void updateEmployee(
            Employee employee) {

        Integer currentExperience =
                employee.getExperience();

        Double currentSalary =
                employee.getSalary();

        employee.setExperience(
                currentExperience + 1);

        Double incrementAmount =
                currentSalary *
                        (annualIncrementPercentage / 100);

        employee.setSalary(
                currentSalary + incrementAmount);

        repository.save(employee);

        log.info(
                "Updated employee {}. Experience: {}, Salary: {}",
                employee.getId(),
                employee.getExperience(),
                employee.getSalary());
    }

}
