package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.EmployeeTermsDto;
import bgu.adss.fff.dev.contracts.FullEmployeeDto;
import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.EmploymentTerms;
import bgu.adss.fff.dev.domain.models.JobType;
import bgu.adss.fff.dev.util.EmployeeUtilHelper;

import java.util.Arrays;

import static bgu.adss.fff.dev.domain.models.Constants.*;

/**
 * Maps employees to dtos, and the inverse.
 */
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

    public static Employee fullMap(FullEmployeeDto dto) {
        int[] details = EmployeeUtilHelper.getBankDetailsHelper(dto.bankDetails());
        int bankId = details[BANK_ID_IND];
        int bankBranch = details[BANK_BRANCH_IND];
        int accountId= details[ACCOUNT_ID_IND];
        EmploymentTerms trm = new EmploymentTerms(
                dto.startDate(),
                JobType.values()[dto.jobType()],
                dto.directManager() != null ? map(dto.directManager()) : null,
                dto.monthlySalary(),
                dto.hourlyRate(),
                dto.daysOff()
        );
        return new Employee(
                dto.id(),
                dto.name(),
                Arrays.stream(dto.roles())
                        .map(RoleMapper::map).toList(),
                trm,
                bankId, bankBranch, accountId
        );
    }

    public static FullEmployeeDto fullMap(Employee emp) {
        EmploymentTerms trm = emp.getTerms();
        return new FullEmployeeDto(
                emp.getId(),
                emp.getName(),
                emp.getRoles()
                        .stream()
                        .map(RoleMapper::map)
                        .toArray(RoleDto[]::new),
                emp.getBank(),
                trm.getStartDate(),
                trm.getJobType().ordinal(),
                trm.getMonthlySalary(),
                trm.getHourlyRate(),
                trm.getDaysOff(),
                map(trm.getManager()),
                trm.getEndDate()
        );
    }

    public static EmploymentTerms map(EmployeeTermsDto dto) {
        return new EmploymentTerms(
                dto.startDate(),
                JobType.values()[dto.jobType()],
                dto.directManager() != null ? map(dto.directManager()) : null,
                dto.monthlySalary(),
                dto.hourlyRate(),
                dto.daysOff()
        );
    }

    public static EmployeeTermsDto map(long id, EmploymentTerms trm) {
        return new EmployeeTermsDto(
                id,
                trm.getStartDate(),
                trm.getJobType().ordinal(),
                trm.getMonthlySalary(),
                trm.getHourlyRate(),
                trm.getDaysOff(),
                map(trm.getManager()),
                trm.getEndDate()
        );
    }
}
