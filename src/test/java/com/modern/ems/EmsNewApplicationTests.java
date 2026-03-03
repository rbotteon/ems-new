package com.modern.ems;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
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
    EmployeeDto employeeDto = new EmployeeDto(null, "testName", "test@email.com", "dep1", 12.34, LocalDate.now());
    EmployeeDto employeeDto2 = new EmployeeDto(null, "testName2","test2@email.com", "dep2",56.78,  LocalDate.now());

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

    List<EmployeeDto> employeeList = restTestClient.get()
        .uri("/api/employees")
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(new ParameterizedTypeReference<List<EmployeeDto>>() {})
        .returnResult()
        .getResponseBody();

    assertEquals(2, employeeList.size());
    assertEmployee(employeeDto, employeeList.get(0));
    assertEmployee(employeeDto2, employeeList.get(1));



  }

  @Test
  void saveInvalid() {
    EmployeeDto employeeDto = new EmployeeDto(null, "testName", "invalid", "dep1", 12.34, LocalDate.now());
    restTestClient.post().uri("/api/employees/create")
        .body(employeeDto)
        .exchange()
        .expectStatus().is5xxServerError();
  }

  @Test
  void shouldFindByDepartment() {
    // given
    EmployeeDto expected = new EmployeeDto(null, "testName", "test@email.com", "dep11", 12.34, LocalDate.now());

    restTestClient.post().uri("/api/employees/create")
        .body(expected)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(Employee.class);

    // when
    List<EmployeeDto> employeeList = restTestClient.get()
        .uri(uriBuilder -> uriBuilder.path("/api/employees/department")
            .queryParam("department", expected.department())
            .build())
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(new ParameterizedTypeReference<List<EmployeeDto>>() {})
        .returnResult()
        .getResponseBody();

    // then
    assertEquals(1, employeeList.size());
    assertEmployee(expected, employeeList.get(0));
  }

  @Test
  void shouldCRUD() {
    // create
    EmployeeDto expected = new EmployeeDto(null, "crudName", "crud@email.com", "depCrud", 999999.99, LocalDate.now());
    EmployeeDto created = restTestClient.post().uri("/api/employees/create")
        .body(expected)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(EmployeeDto.class)
        .returnResult()
        .getResponseBody();

    assertEmployee(expected, created);

    // read
    EmployeeDto read = restTestClient.get()
        .uri("/api/employees/" + created.id())
        .exchange()
        .expectStatus().isOk()
        .expectBody(EmployeeDto.class)
        .returnResult()
        .getResponseBody();

    assertEmployee(created, read, true);

    // update
    String newDepartment = "newDepartment";
    EmployeeDto toUpdate = new EmployeeDto(read.id(), read.name(), read.email(), newDepartment, read.salary(), read.hireDate());
    EmployeeDto updated = restTestClient.put()
        .uri("/api/employees/{employeeId}", 1)
        .body(toUpdate)
        .exchange()
        .expectStatus().isCreated()
        .expectBody(EmployeeDto.class)
        .returnResult()
        .getResponseBody();

    assertEmployee(toUpdate, updated, true);

    // delete
    restTestClient.delete()
        .uri("/api/employees/" + updated.id())
        .exchange()
        .expectStatus().isOk();

    restTestClient.get()
        .uri("/api/employees/" + updated.id())
        .exchange()
        .expectStatus().is5xxServerError();

  }

  private void assertEmployee(EmployeeDto expected, EmployeeDto actual, Boolean checkId) {

    if (checkId) {
      assertEquals(expected.id(), actual.id());
    }
    assertEmployee(expected, actual);
  }

  private void assertEmployee(EmployeeDto expected, EmployeeDto actual) {
    System.out.println("Expected: " + expected);
    System.out.println("Actual:   " + actual);
    assertEquals(expected.name(), actual.name());
    assertEquals(expected.email(), actual.email());
    assertEquals(expected.department(), actual.department());
    assertEquals(expected.salary(), actual.salary());
    assertEquals(expected.hireDate(), actual.hireDate());
  }

}
