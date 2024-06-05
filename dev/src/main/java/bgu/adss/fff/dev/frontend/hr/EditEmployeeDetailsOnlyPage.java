package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.util.Arrays;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class EditEmployeeDetailsOnlyPage extends AbstractUserComponent {

    private static final String ROLES_ROUTE = URI_PATH + "/role";
    private static final String BRANCH_ROUTE = URI_PATH + "/branch";
    private static final String EMPLOYEE_ROUTE = URI_PATH + "/employee/";

    private final RestTemplate template;

    private final InputComponent nameInput;
    private final InputComponent rolesInput;
    private final InputComponent bankInput;
    private final InputComponent branchInput;

    private String rolesList, branchList;

    private long id;
    private String name;
    private RoleDto[] roles;
    private String bankDetails;
    private String branch;
    public EditEmployeeDetailsOnlyPage(PrintStream out, long id) throws RestClientResponseException {
        super(out);

        this.template = new RestTemplate();

        RoleDto[] roles = template.getForEntity(ROLES_ROUTE, RoleDto[].class).getBody();
        rolesList = Arrays.toString(Arrays.stream(roles).map((r) -> r.name()).toArray());
        String[] branches = template.getForEntity(BRANCH_ROUTE, String[].class).getBody();
        branchList = Arrays.toString(branches);

        EmployeeDto emp = template.getForEntity(EMPLOYEE_ROUTE+id, EmployeeDto.class).getBody();
        name = emp.name();
        this.roles = emp.roles();
        bankDetails = emp.bankDetails();
        branch = emp.branchName();

        this.id = id;

        this.nameInput = new InputComponent("Insert the name of the employee:");
        this.rolesInput = new InputComponent("Insert the roles this employee has:\n" +
                "Pick from the list (or leave empty), to separate use comma (,): "+rolesList);
        this.bankInput = new InputComponent("Insert the bank details of the employee"+
                "\n Using the following format: [BANK ID]:[BANK BRANCH]:[ACCOUNT ID] !");
        this.branchInput = new InputComponent("Insert the branch this employee is working at: \n" +
                "Available branches: "+branchList);

        nameInput.subscribe(this::onNameInput);
        rolesInput.subscribe(this::onRolesInput);
        bankInput.subscribe(this::onBankInput);
        branchInput.subscribe(this::onBranchInput);

        page.add(new LogoComponent("Edit Existing Employee"));
        page.add(new LabelComponent("Leave empty [ENTER] if you don't want to change the field!"));
        page.add(nameInput);
        page.add(rolesInput);
        page.add(bankInput);
        page.add(branchInput);
    }

    private void onNameInput(StateEvent event) {
        if(event.getData() == null) {
            out.println("Input error occurred.");
            nameInput.render(out);
        } else if(event.getData().isBlank()) {
            // do not change name.
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
        } // else we're not updating the roles.
    }

    private void onBankInput(StateEvent event) {
        if(event.getData().isBlank()) return; //no need to worry...

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
        if(!event.getData().isBlank()) this.branch = event.getData();
        sendRequest();
    }

    private  void sendRequest() {
        try {
            template.put(EMPLOYEE_ROUTE + id, new EmployeeDto(
                    id, name, roles, bankDetails, branch
            ));
            EmployeeDto emp = template.getForEntity(EMPLOYEE_ROUTE+id, EmployeeDto.class).getBody();
            out.println("Updated: "+emp.name()+" : "+emp.id()+" successfully!");
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }
}
