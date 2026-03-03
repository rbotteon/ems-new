package com.modern.ems.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.modern.ems.dto.EmployeeDto;
import com.modern.ems.entity.Employee;
import com.modern.ems.persistence.EmployeeRepository;

@Component
public class EmployeeService {
  @Autowired
  EmployeeRepository employeeRepository;

  public List<EmployeeDto> getEmployees() {
    List<Employee> employees = employeeRepository.findAll();
    List<EmployeeDto> employeeDto = reduceToDto(employees);
    return null;
  }

  private List<EmployeeDto> reduceToDto(List<Employee> employees) {
    List<EmployeeDto> dtoList = new ArrayList<>();
    employees.forEach(employee -> {
      EmployeeDto dto = mapToDto(employee);
      dtoList.add(dto);
    });
    return dtoList;
  }

  private EmployeeDto mapToDto(Employee employee) {
    EmployeeDto dto = new EmployeeDto(employee.getId(), employee.getName(), employee.getEmail());
    return dto;
  }
}
