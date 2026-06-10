package com.example.EmployeManagement.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationMetadata {

    private int currentPage;

    private int totalPages;

    private long totalElements;

    private int pageSize;
}
