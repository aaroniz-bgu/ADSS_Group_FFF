package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.contracts.RequireRoleRequest;
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

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class RequireRolesPage extends AbstractUserComponent {

    private static final String ROLES_ROUTE = URI_PATH + "/role";
    private static final String BRANCH_ROUTE = URI_PATH + "/branch";
    private static final String SHIFT_ROUTE = URI_PATH + "/shift/";

    private final RestTemplate template;

    private final InputComponent requireRemoveInput;
    private final InputComponent reoccurringInput;
    private final InputComponent dateInput;
    private final InputComponent partInput;
    private final InputComponent roleInput;

    private String rolesList;
    private String branchList;

    private boolean require;
    private LocalDate date;
    private int part;
    private String branch;
    private String role;
    private boolean reoccuring;

    protected RequireRolesPage(PrintStream out) {
        super(out);

        this.template = new RestTemplate();

        try {
            RoleDto[] roles = template.getForEntity(ROLES_ROUTE, RoleDto[].class).getBody();
            rolesList = Arrays.toString(roles);
            String[] branches = template.getForEntity(BRANCH_ROUTE, String[].class).getBody();
            branchList = Arrays.toString(branches);
        } catch (Exception e) {
            //log?
        }

        this.requireRemoveInput = new InputComponent("You're requiring[Y] a role or removing[N] a requirement?: [Y/N]");
        this.reoccurringInput = new InputComponent("Is this a reoccurring constraint[Y]? or specific to this shift only[N]? [Y/N]?");
        InputComponent branchInput = new InputComponent("Which branch this requirement is needed for?\n"
                + branchList);
        this.dateInput = new InputComponent("Please insert the date of the requested shift (or just a date of the day if it's an reoccurring role requirement).");
        this.partInput = new InputComponent("Which shift should it be? 0 - morning or 1 evening?");
        this.roleInput = new InputComponent("Please pick one role which you want to require:\n"
                + rolesList);


        requireRemoveInput.subscribe(this::onRequireInput);
        reoccurringInput.subscribe(this::onReoccurringInput);
        branchInput.subscribe((e) -> branch = e.getData());
        dateInput.subscribe(this::onDateInput);
        partInput.subscribe(this::onPartInput);
        roleInput.subscribe((e) -> {
            role = e.getData();
            sendRequest();
        });

        page.add(new LogoComponent("Require Roles"));
        page.add(requireRemoveInput);
        page.add(reoccurringInput);
        page.add(branchInput);
        page.add(dateInput);
        page.add(partInput);
        page.add(roleInput);
    }

    private void onDateInput(StateEvent event) {
        String input = event.getData();
        String regex = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
        if(!input.matches(regex)) {
            out.println("Please insert date in the provided format!");
            dateInput.render(out);
        } else {
            date = LocalDate.parse(input, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }
    }

    private void onPartInput(StateEvent event) {
        try {
            part = Integer.parseInt(event.getData());
            if(part != 0 && part != 1) throw new RuntimeException();
        } catch (RuntimeException e) {
            out.println("Please insert only values that you're instructed to!");
            partInput.render(out);
        }
    }

    private void onRequireInput(StateEvent event) {
        String choice = event.getData().toLowerCase();
        if(choice.equals("y")) {
            require = true;
        } else if (choice.equals("n")) {
            require = false;
        } else {
            out.println("Please insert only values that you were instructed to!");
            requireRemoveInput.render(out);
        }
    }
    private void onReoccurringInput(StateEvent event) {
        String choice = event.getData().toLowerCase();
        if(choice.equals("y")) {
            reoccuring = true;
        } else if (choice.equals("n")) {
            reoccuring = false;
        } else {
            out.println("Please insert only values that you were instructed to!");
            reoccurringInput.render(out);
        }
    }

    private void sendRequest() {
        try {
            template.put(SHIFT_ROUTE + "role=" + require,
                    new RequireRoleRequest(date, part, branch, role, reoccuring));
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }
}
