package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.domain.models.Employee;
import org.springframework.context.annotation.Bean;

@Bean
public class EmployeeMapper {

    public Employee dtoToModel(EmployeeDto dto) {
        return new Employee(
                dto.id(),
                dto.name(),
                dto.
        );
    }
}
