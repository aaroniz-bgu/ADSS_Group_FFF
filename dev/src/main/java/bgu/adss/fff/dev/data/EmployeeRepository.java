package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
