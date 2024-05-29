package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findEmployeesByBranch(Branch branch);
}