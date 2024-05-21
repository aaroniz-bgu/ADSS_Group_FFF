package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.util.EmployeeUtilHelper;

import java.util.Arrays;

import static bgu.adss.fff.dev.domain.models.Constants.*;

public class EmployeeMapper {
    public static Employee map(EmployeeDto dto) {
        int[] details = EmployeeUtilHelper.getBankDetailsHelper(dto.bankDetails());
        int bankId = details[BANK_ID_IND];
        int bankBranch = details[BANK_BRANCH_IND];
        int accountId= details[ACCOUNT_ID_IND];
        return new Employee(
                dto.id(),
                dto.name(),
                Arrays.stream(dto.roles())
                        .map(RoleMapper::map).toList(),
                null,
                bankId, bankBranch, accountId
        );
    }

    public static EmployeeDto map(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getRoles()
                        .stream()
                        .map(RoleMapper::map)
                        .toArray(RoleDto[]::new),
                employee.getBank()
        );
    }
}
