package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class RegisterEmployeePage extends AbstractUserComponent {

    private static final String ROLES_ROUTE = URI_PATH + "/role";
    private static final String BRANCH_ROUTE = URI_PATH + "/branch";
    private static final String EMPLOYEE_ROUTE = URI_PATH + "/employee";

    private final RestTemplate template;

    private List<RoleDto> rolesList;
    private List<String> branchList;

    private final InputComponent idInput;
    private final InputComponent nameInput;
    private final InputComponent rolesInput;
    private final InputComponent bankInput;
    private final InputComponent branchInput;
    private final InputComponent startInput;
    private final InputComponent jobTypeInput;
    private final InputComponent monthlySalaryInput;
    private final InputComponent hourlyRateInput;
    private final InputComponent daysOffInput;
    private final InputComponent managerInput;

    private long id;
    private String name;
    private RoleDto[] roles;
    private String bankDetails;
    private String branch;
    private LocalDate startDate;
    private int jobType;
    private float monthlySalary;
    private float hourlyRate;
    private int daysOff;
    private long directManager;

    protected RegisterEmployeePage(PrintStream out) {
        super(out);

        this.template = new RestTemplate();

        boolean listInitiated = initLists();

        this.idInput = new InputComponent("Insert the ID of the employee");
        this.nameInput = new InputComponent("Insert the name of the employee:");
        this.rolesInput = new InputComponent("Insert the roles this employee has:\n" +
                "Pick from the list, to separate use comma (,): "+toStringRoles());
        this.bankInput = new InputComponent("Insert the bank details of the employee"+
                "\n Using the following format: [BANK ID]:[BANK BRANCH]:[ACCOUNT ID] !");
        this.branchInput = new InputComponent("Insert the branch this employee is working at: \n" +
                "Available branches: "+toStringBranches());
        this.startInput = new InputComponent("Insert start date or leave empty for today:");
        this.jobTypeInput = new InputComponent("Insert job type:\n1. Full time\n2. Part time\n3. Contract\n" +
                "Do not insert any additional characters, only numbers from 1 to 3!");
        this.monthlySalaryInput = new InputComponent("Insert the monthly salary, you can leave this empty:");
        this.hourlyRateInput = new InputComponent("Insert the hourly rate of the employee\n" +
                "This field cannot be left empty if monthly salary was left empty");
        this.daysOffInput = new InputComponent("Insert the number of yearly days off for the employee " +
                "(must be greater than 0!)");
        this.managerInput = new InputComponent("Insert the id of the manager or leave blank.");

        //TODO LISTENERS

        page.add(new LogoComponent("REGISTER NEW EMPLOYEE"));
        //TODO
    }

    private boolean initLists() {
        try {
            RoleDto[] roles = template.getForEntity(ROLES_ROUTE, RoleDto[].class).getBody();
            rolesList = Arrays.asList(roles);
            String[] branches = template.getForEntity(BRANCH_ROUTE, String[].class).getBody();
            branchList = Arrays.asList(branches);
            return true;
        } catch (Exception e) {
            //log?
        }
        return false;
    }

    private String toStringRoles() {
        if(rolesList == null || rolesList.isEmpty()) return "[]";
        StringBuilder builder = new StringBuilder("[");
        boolean first = true;
        for(RoleDto role : rolesList) {
            if(!first) builder.append(", ");
            first = false;
            builder.append(role.name());
        }
        return builder.toString() + "]";
    }

    private String toStringBranches() {
        if(branchList == null || branchList.isEmpty()) return "[]";
        StringBuilder builder = new StringBuilder("[");
        boolean first = true;
        for(String br : branchList) {
            if(!first) builder.append(", ");
            first = false;
            builder.append(br);
        }
        return builder.toString() + "]";
    }

}
