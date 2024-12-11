package co.istad.reporting.employee.employee;

import co.istad.reporting.domain.secondary.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    @GetMapping
    List<Employee> findEmployees() {
        return employeeRepository.findAll();
    }

}
