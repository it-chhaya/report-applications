package co.istad.reporting.domain.secondary;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Employee {

    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private String gender;
    private LocalDate birthDate;
    private LocalDate hireDate;

}
