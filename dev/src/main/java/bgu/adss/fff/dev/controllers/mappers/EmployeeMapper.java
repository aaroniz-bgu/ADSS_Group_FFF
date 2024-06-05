package bgu.adss.fff.dev.controllers.mappers;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.EmployeeTermsDto;
import bgu.adss.fff.dev.contracts.FullEmployeeDto;
import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.domain.models.Branch;
import bgu.adss.fff.dev.domain.models.Employee;
import bgu.adss.fff.dev.domain.models.EmploymentTerms;
import bgu.adss.fff.dev.domain.models.JobType;
import bgu.adss.fff.dev.util.EmployeeUtilHelper;

import java.util.Arrays;

import static bgu.adss.fff.dev.domain.models.Constants.*;

/**
 * Maps employees to DTOs (Data Transfer Objects) and vice versa.
 */
public class EmployeeMapper {
    /**
     * Converts an EmployeeDto object to an Employee entity.
     * The branch name is used to create a new Branch entity,
     * cannot assume that the branch already exists in the database.
     *
     * @param dto the EmployeeDto object to be converted
     * @return the converted Employee entity
     */
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
                bankId, bankBranch, accountId, new Branch(dto.branchName())
        );
    }

    /**
     * Converts an Employee entity to an EmployeeDto object.
     *
     * @param employee the Employee entity to be converted
     * @return the converted EmployeeDto object
     */
    public static EmployeeDto map(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getRoles()
                        .stream()
                        .map(RoleMapper::map)
                        .toArray(RoleDto[]::new),
                employee.getBank(),
                employee.getBranch().getName()
        );
    }

    /**
     * Converts a FullEmployeeDto object to an Employee entity with full employment terms.
     *
     * @param dto the FullEmployeeDto object to be converted
     * @return the converted Employee entity
     */
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
                bankId, bankBranch, accountId, new Branch(dto.branchName())
        );
    }

    /**
     * Converts an Employee entity with full employment terms to a FullEmployeeDto object.
     *
     * @param emp the Employee entity to be converted
     * @return the converted FullEmployeeDto object
     */
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
                emp.getBranch().getName(),
                trm.getStartDate(),
                trm.getJobType().ordinal(),
                trm.getMonthlySalary(),
                trm.getHourlyRate(),
                trm.getDaysOff(),
                map(trm.getManager()),
                trm.getEndDate()
        );
    }

    /**
     * Converts an EmployeeTermsDto object to an EmploymentTerms entity.
     *
     * @param dto the EmployeeTermsDto object to be converted
     * @return the converted EmploymentTerms entity
     */
    public static EmploymentTerms map(EmployeeTermsDto dto) {
        return new EmploymentTerms(
                dto.startDate(),
                JobType.values()[dto.jobType()],
                dto.directManager() != null ? mapMangerHelper(dto.directManager()) : null,
                dto.monthlySalary(),
                dto.hourlyRate(),
                dto.daysOff()
        );
    }

    private static Employee mapMangerHelper(EmployeeDto dto) {
        return new Employee(dto.id(), null, null, null, 0, 0, 0, null);
    }

    /**
     * Converts an EmploymentTerms entity to an EmployeeTermsDto object.
     *
     * @param id the ID of the Employee
     * @param trm the EmploymentTerms entity to be converted
     * @return the converted EmployeeTermsDto object
     */
    public static EmployeeTermsDto map(long id, EmploymentTerms trm) {
        return new EmployeeTermsDto(
                id,
                trm.getStartDate(),
                trm.getJobType().ordinal(),
                trm.getMonthlySalary(),
                trm.getHourlyRate(),
                trm.getDaysOff(),
                trm.getManager() != null ? map(trm.getManager()) : null,
                trm.getEndDate()
        );
    }
}
