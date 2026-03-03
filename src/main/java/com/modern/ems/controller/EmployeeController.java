package com.modern.ems.controller;

import static org.springframework.web.bind.annotation.RequestMethod.DELETE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.modern.ems.dto.EmployeeDto;
import com.modern.ems.entity.Employee;
import com.modern.ems.service.EmployeeService;

import jakarta.validation.Valid;

@RestController
public class EmployeeController {
  @Autowired
  private EmployeeService employeeService;

  @RequestMapping(method = POST, value = "/api/employees/create")
  public @ResponseBody
  ResponseEntity<?> create(@Valid @RequestBody EmployeeDto request) {
    Employee employee = employeeService.saveEmployee(request);
    return new ResponseEntity<>(employee, HttpStatus.CREATED);
  }

  @RequestMapping(method = GET, value = "/api/employees")
  public @ResponseBody ResponseEntity<?> list() {
    List<EmployeeDto> employees = employeeService.getEmployees();
    return new ResponseEntity<>(employees, HttpStatus.OK);
  }

  @RequestMapping(method = GET, value = "/api/employees/{employeeId}")
  public @ResponseBody
  ResponseEntity<?> read(final @PathVariable("employeeId") Long employeeId) {
    EmployeeDto employeeDto = employeeService.getById(employeeId);
    return new ResponseEntity<>(employeeDto, HttpStatus.OK);
  }

  @RequestMapping(method = PUT, value = "/api/employees/{employeeId}")
  public @ResponseBody
  ResponseEntity<?> update(@Valid @RequestBody EmployeeDto employeeDto) {
    Employee employee = employeeService.updateEmployee(employeeDto);
    return new ResponseEntity<>(employee, HttpStatus.CREATED);
  }

  @RequestMapping(method = DELETE, value = "/api/employees/{employeeId}")
  public @ResponseBody
  ResponseEntity<?> delete(final @PathVariable("employeeId") Long employeeId) {
    employeeService.deleteEmployee(employeeId);
    return new ResponseEntity<>(employeeId, HttpStatus.OK);
  }


  @RequestMapping(method = GET, value = "/api/employees/department")
  public @ResponseBody ResponseEntity<?> findByDepartment(@RequestParam(required = true) String department) {
    List<EmployeeDto> employees = employeeService.findByDepartment(department);
    return new ResponseEntity<>(employees, HttpStatus.OK);
  }

  }
