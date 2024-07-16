package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class AddRolePage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/role";

    private final RestTemplate template;

    private final InputComponent nameInput;
    private final InputComponent isShiftManagerInput;
    private final InputComponent isHrManagerInput;

    private String name;
    private boolean isShiftManager;
    private boolean isHrManager;
    protected AddRolePage(PrintStream out) {
        super(out);

        this.template = new RestTemplate();

        this.nameInput = new InputComponent("Please insert the role's name without any spaces:");
        this.isShiftManagerInput = new InputComponent("Is this role privileged as shift manager? [Y/N]");
        this.isHrManagerInput = new InputComponent("Is this role privileged as HR manager? [Y/N]");

        nameInput.subscribe(this::onNameInput);
        isHrManagerInput.subscribe(this::onIsShiftManagerInput);
        isHrManagerInput.subscribe(this::onIsHrManagerInput);

        page.add(new LogoComponent("Add Role"));
        page.add(nameInput);
        page.add(isShiftManagerInput);
        page.add(isHrManagerInput);
    }


    private void onNameInput(StateEvent event) {
        if(event.getData().isBlank() || event.getData().contains(" ")) {
            out.println("Branch name must not be empty! & spaces are not allowed!");
            nameInput.render(out);
        } else {
            name = event.getData();
        }
    }

    private void onIsShiftManagerInput(StateEvent event) {
        String choice = event.getData().toLowerCase();
        if(choice.equals("y")) {
            isShiftManager = true;
        } else if (choice.equals("n")) {
            isShiftManager = false;
        } else {
            out.println("Please insert only values that you were instructed to!");
            isShiftManagerInput.render(out);
        }
    }

    private void onIsHrManagerInput(StateEvent event) {
        String choice = event.getData().toLowerCase();
        if(choice.equals("y")) {
            isHrManager = true;
            sendRequest();
        } else if (choice.equals("n")) {
            isHrManager = false;
            sendRequest();
        } else {
            out.println("Please insert only values that you were instructed to!");
            isHrManagerInput.render(out);
        }
    }

    private void sendRequest() {
        try {
            template.postForLocation(ROUTE, new RoleDto(name, isShiftManager, isHrManager));
            RoleDto role = template.getForEntity(ROUTE+"/"+name, RoleDto.class).getBody();
            out.println("Created role: "+role.name()+ " successfully!");
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }
}
