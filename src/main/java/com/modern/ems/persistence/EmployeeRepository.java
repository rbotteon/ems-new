package com.modern.ems.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.modern.ems.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findByDepartment(String department);
}
