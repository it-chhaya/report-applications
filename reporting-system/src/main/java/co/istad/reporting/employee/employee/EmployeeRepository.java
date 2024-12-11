package co.istad.reporting.employee.employee;

import co.istad.reporting.domain.secondary.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
