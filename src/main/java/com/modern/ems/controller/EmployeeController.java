package com.modern.ems.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.modern.ems.dto.EmployeeDto;
import com.modern.ems.entity.Employee;
import com.modern.ems.service.EmployeeService;

@RestController
public class EmployeeController {
  @Autowired
  private EmployeeService employeeService;

  @RequestMapping(method = POST, value = "/api/employees/create")
  public @ResponseBody
  ResponseEntity<?> create(@RequestBody EmployeeDto request) {
    Employee employee = employeeService.saveEmployee(request);
    return new ResponseEntity<>(employee, HttpStatus.CREATED);
  }

  @RequestMapping(method = GET, value = "/api/employees")
  public @ResponseBody ResponseEntity<?> list() {
    List<EmployeeDto> employees = employeeService.getEmployees();
    return new ResponseEntity<>(employees, HttpStatus.OK);
  }

  @RequestMapping(method = GET, value = "/api/employees/department")
  public @ResponseBody ResponseEntity<?> findByDepartment(@RequestParam(required = true) String department) {
    List<EmployeeDto> employees = employeeService.findByDepartment(department);
    return new ResponseEntity<>(employees, HttpStatus.OK);
  }

  }
