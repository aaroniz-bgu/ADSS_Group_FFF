package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.contracts.FullEmployeeDto;
import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private EmployeeDto directManager;

    public RegisterEmployeePage(PrintStream out) {
        super(out);

        this.template = new RestTemplate();
        initLists();

        this.idInput = new InputComponent("Insert the ID of the employee");
        this.nameInput = new InputComponent("Insert the name of the employee:");
        this.rolesInput = new InputComponent("Insert the roles this employee has:\n" +
                "Pick from the list (or leave empty), to separate use comma (,): "+toStringRoles());
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

        idInput.subscribe(this::onIdInput);
        nameInput.subscribe(this::onNameInput);
        rolesInput.subscribe(this::onRolesInput);
        bankInput.subscribe(this::onBankInput);
        branchInput.subscribe(this::onBranchInput);
        startInput.subscribe(this::onStartInput);
        jobTypeInput.subscribe(this::onJobTypeInput);
        monthlySalaryInput.subscribe(this::onMonthlySalInput);
        hourlyRateInput.subscribe(this::onHourlyRateInput);
        daysOffInput.subscribe(this::onDaysOffInput);
        managerInput.subscribe(this::onMangerInput);

        page.add(new LogoComponent("Register New Employee"));
        page.add(idInput);
        page.add(nameInput);
        page.add(rolesInput);
        page.add(bankInput);
        page.add(branchInput);
        page.add(startInput);
        page.add(jobTypeInput);
        page.add(monthlySalaryInput);
        page.add(hourlyRateInput);
        page.add(daysOffInput);
        page.add(managerInput);
    }

    private void onIdInput(StateEvent event) {
        try {
            this.id = Long.parseUnsignedLong(event.getData());
        } catch (NumberFormatException e) {
            out.println("Please type a proper id which is supposed to be a number.");
            idInput.render(out);
        }
    }

    private void onNameInput(StateEvent event) {
        if(event.getData() == null || event.getData().isBlank()) {
            out.println("Name cannot be empty/blank.");
            nameInput.render(out);
        } else {
            this.name = event.getData();
        }
    }

    private void onRolesInput(StateEvent event) {
        if(!event.getData().isBlank()) {
            String[] roleTitles = event.getData().replace(" ","").split(",");
            roles = new RoleDto[roleTitles.length];
            for(int i = 0 ; i< roles.length; i++) {
                roles[i] = new RoleDto(roleTitles[i], false, false);
            }
        } else this.roles = new RoleDto[0];
    }

    private void onBankInput(StateEvent event) {
        char[] input = event.getData().toCharArray();
        int count = 0;
        for(char c : input) {
            if(c == ':') count++;
        }
        if(count != 2) {
            out.println("please supply bank details as formatted!");
            bankInput.render(out);
            return;
        }
        bankDetails = event.getData();
    }

    private void onBranchInput(StateEvent event) {
        this.branch = event.getData();
    }

    private void onStartInput(StateEvent event) {
        String input = event.getData();
        if(input.isBlank()) {
            startDate = LocalDate.now();
            return;
        }
        String regex = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
        if(!input.matches(regex)) {
            out.println("Please make sure your date is formatted properly [dd-MM-yyyy]");
            startInput.render(out);
        } else {
            startDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
    }

    private void onJobTypeInput(StateEvent event) {
        try {
            jobType = Integer.parseInt(event.getData()) - 1;
        } catch(NumberFormatException e) {
            out.println("Only insert numbers as requested!");
            jobTypeInput.render(out);
        }
    }

    private void onMonthlySalInput(StateEvent event) {
        try {
            monthlySalary = event.getData().isBlank() ? 0f : Float.parseFloat(event.getData());
        } catch(NumberFormatException e) {
            out.println("Only insert numbers as requested!");
            monthlySalaryInput.render(out);
        }
    }

    private void onHourlyRateInput(StateEvent event) {
        try {
            hourlyRate = event.getData().isBlank() ? 0f : Float.parseFloat(event.getData());
        } catch(NumberFormatException e) {
            out.println("Only insert numbers as requested!");
            hourlyRateInput.render(out);
        }
    }

    private void onDaysOffInput(StateEvent event) {
        try {
            daysOff = Integer.parseInt(event.getData());
        } catch (NumberFormatException e) {
            out.println("Only insert numbers as requested!");
            daysOffInput.render(out);
        }
    }

    private void onMangerInput(StateEvent event) {
        try {
            directManager = event.getData().isBlank() ? null :
                    new EmployeeDto(Long.parseUnsignedLong(event.getData()), "", null, "" , "");
            sendRequest();
        } catch(NumberFormatException e) {
            out.println("Only insert numbers as requested!");
            managerInput.render(out);
        }
    }

    private void sendRequest() {
        try {
            template.postForLocation(EMPLOYEE_ROUTE, new FullEmployeeDto(
                    id, name, roles, bankDetails, branch, startDate, jobType,
                    monthlySalary, hourlyRate, daysOff, directManager, null
            ));
            EmployeeDto created = template.getForEntity(EMPLOYEE_ROUTE+"/"+id, EmployeeDto.class).getBody();
            out.println("Created new employee: "+created.id()+":"+created.name());
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }

    private void initLists() {
        try {
            RoleDto[] roles = template.getForEntity(ROLES_ROUTE, RoleDto[].class).getBody();
            rolesList = Arrays.asList(roles);
            String[] branches = template.getForEntity(BRANCH_ROUTE, String[].class).getBody();
            branchList = Arrays.asList(branches);
        } catch (Exception e) {
            //log?
        }
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
