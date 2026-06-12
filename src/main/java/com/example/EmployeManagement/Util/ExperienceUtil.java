package com.example.EmployeManagement.Util;

import java.time.LocalDate;
import java.time.Period;

public class ExperienceUtil {

    public static Integer calculateExperience(LocalDate dateOfJoining) {
        return Period.between(
                        dateOfJoining,
                        LocalDate.now())
                .getYears();
    }
}
