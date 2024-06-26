package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class CustomRoleFieldUpdatePage extends AbstractUserComponent {

    private final RestTemplate template;

    private final InputComponent idInput;

    private long id;
    private String role;
    private String field;
    private String value;

    protected CustomRoleFieldUpdatePage(PrintStream out) {
        super(out);

        template = new RestTemplate();

        idInput = new InputComponent("Insert the id of the employee, must be a number:");

        InputComponent roleInput = new InputComponent("Insert the role which is associated with this:");
        InputComponent fieldInput = new InputComponent("Insert the name of the field you'd like to save:");
        InputComponent valueInput = new InputComponent("Insert the value of the field you'd like to save:");

        idInput.subscribe(this::onIdInput);
        roleInput.subscribe(e -> role = e.getData());
        fieldInput.subscribe(e -> field = e.getData());
        valueInput.subscribe(e -> {
            value = e.getData();
            sendRequest();
        });

        page.add(new LogoComponent("Update Role Field"));
        page.add(idInput);
        page.add(roleInput);
        page.add(fieldInput);
        page.add(valueInput);
    }

    private void onIdInput(StateEvent e) {
        try {
            id = Long.parseUnsignedLong(e.getData());
        } catch(NumberFormatException ex) {
            idInput.render(out);
        }
    }

    private void sendRequest() {
        try {
            template.put(constructFinalPath(), null);
            out.println("Success!");
        } catch (RestClientResponseException e) {
            out.print("Failure!");
        } catch (Exception e) {
            out.println("Fatal failure, contact IT team.");
        }
    }

    private String constructFinalPath() {
        return URI_PATH + "employee/" + id + "/role/" + role + "customField?field=" + field + ",val=" + value;
    }
}
