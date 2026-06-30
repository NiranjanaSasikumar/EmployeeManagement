package com.example.EmployeManagement.PFDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PFDetailsRepository
        extends JpaRepository<PFDetails, Integer> {

}
