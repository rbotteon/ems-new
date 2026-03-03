package com.modern.ems.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.modern.ems.dto.EmployeeDto;
import com.modern.ems.entity.Employee;
import com.modern.ems.persistence.EmployeeRepository;

@Component
public class EmployeeService {
  @Autowired
  EmployeeRepository employeeRepository;

  @Transactional
  public Employee saveEmployee(EmployeeDto employeeDto) {

    Employee employee = buildEmployee(employeeDto);
    employee = employeeRepository.save(employee);
    return employee;
  }

  public List<EmployeeDto> getEmployees() {
    List<Employee> employees = employeeRepository.findAll();
    List<EmployeeDto> employeeDto = reduceToDto(employees);
    return employeeDto;
  }

  public List<EmployeeDto> findByDepartment(String department) {
    List<Employee> employees = employeeRepository.findByDepartment(department);
    return reduceToDto(employees);
  }

  private Employee buildEmployee(EmployeeDto employeeDto) {
    Employee employee = new Employee();
    employee.setName(employeeDto.name());
    employee.setEmail(employeeDto.email());
    employee.setDepartment(employeeDto.department());
    employee.setSalary(employeeDto.salary());
    employee.setHireDate(employeeDto.hireDate());
    return employee;
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
    EmployeeDto dto = new EmployeeDto(employee.getId(), employee.getName(), employee.getEmail(),  employee.getDepartment(), employee.getSalary(), employee.getHireDate());
    return dto;
  }

}
