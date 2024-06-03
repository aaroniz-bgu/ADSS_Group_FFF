package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.contracts.RoleDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class LoginPage extends AbstractUserComponent {

    private static final String ROUTE = "/employee";

    private final RestTemplate restTemplate;

    public LoginPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        out.println();
        page.add(new LogoComponent("LOGIN"));
        InputComponent input = new InputComponent("Insert your ID: ");
        input.subscribe(this::onLogin);
        page.add(input);
    }

    private void onLogin(StateEvent event) {
        String ans = event.getData();
        String uri =  URI_PATH + ROUTE + "/" + ans;
        try {
            EmployeeDto response = restTemplate.getForEntity(uri, EmployeeDto.class).getBody();
            AbstractUserComponent nextPage = isHr(response) ? null : new EmployeeMenuPage(out, response);
            nextPage.render();
            out.print("Horray! "+response.name());
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
            this.render();
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }

    private boolean isHr(EmployeeDto emp) {
        for(RoleDto r : emp.roles()) {
            if(r.isShiftManager()) return true;
        }
        return false;
    }
}
