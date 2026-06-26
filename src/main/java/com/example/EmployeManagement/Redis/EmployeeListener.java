package com.example.EmployeManagement.Redis;

import com.example.EmployeManagement.EmployeeHome.Employee;
import com.example.EmployeManagement.EmployeeHome.EmployeeRepository;


import com.example.EmployeManagement.PFDetails.PFDetails;
import com.example.EmployeManagement.PFDetails.PFDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeListener {

    private final EmployeeRepository employeeRepository;
    private final PFDetailsRepository pfDetailsRepository;

    public void receiveMessage(String employeeId) {

        Integer id = Integer.parseInt(employeeId);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        PFDetails pfDetails = new PFDetails();

        String[] names = employee.getName().split(" ", 2);

        pfDetails.setFirstName(names[0]);

        if (names.length > 1) {
            pfDetails.setLastName(names[1]);
        } else {
            pfDetails.setLastName("");
        }

        pfDetails.setDateOfBirth(employee.getDateOfBirth());
        pfDetails.setPanCardNo(employee.getPanCardNo());
        pfDetails.setPassword(employee.getPassword());

        pfDetailsRepository.save(pfDetails);

        System.out.println("PF Details created for Employee ID : " + id);
    }
}