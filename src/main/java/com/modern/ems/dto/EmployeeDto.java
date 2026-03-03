package com.modern.ems.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.PastOrPresent;

public record EmployeeDto(Long id, String name, String email, String department, double salary, LocalDate hireDate) {}
