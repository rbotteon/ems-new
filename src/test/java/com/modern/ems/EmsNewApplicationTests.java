package com.modern.ems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.RestTestClient;

import com.modern.ems.controller.EmployeeController;
import com.modern.ems.dto.EmployeeDto;
import com.modern.ems.entity.Employee;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EmsNewApplicationTests {
  @Autowired
  private EmployeeController employeeController;
  private RestTestClient restTestClient;

  @BeforeEach
  void beforeEach() {
    restTestClient = RestTestClient.bindToController(employeeController).build();
  }

  @Test
  void saveAndRead() {
    EmployeeDto employeeDto = new EmployeeDto(null, "testName", "test@email.com");
    EmployeeDto employeeDto2 = new EmployeeDto(null, "testName2", "test2@email.com");

    restTestClient.post().uri("/api/employees/create")
        .body(employeeDto)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(Employee.class);

    restTestClient.post().uri("/api/employees/create")
        .body(employeeDto2)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(Employee.class);

    List<Employee> employeeList = restTestClient.get()
        .uri("/api/employees")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(new ParameterizedTypeReference<List<Employee>>() {})
        .returnResult()
        .getResponseBody();

    assertEquals(2, employeeList.size());
    assertEquals(employeeDto.name(), employeeList.get(0).getName());
    assertEquals(employeeDto.email(), employeeList.get(0).getEmail());
    assertEquals(employeeDto2.name(), employeeList.get(1).getName());
    assertEquals(employeeDto2.email(), employeeList.get(1).getEmail());
  }

  @Test
  void saveInvalid() {
    EmployeeDto employeeDto = new EmployeeDto(null, "testName", "invalid");
    restTestClient.post().uri("/api/employees/create")
        .body(employeeDto)
        .exchange()
        .expectStatus().is5xxServerError();
  }

}
