package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class AddBranchPage extends AbstractUserComponent {
    private static final String ROUTE = URI_PATH + "/branch/";
    protected AddBranchPage(PrintStream out) {
        super(out);
        RestTemplate template = new RestTemplate();
        InputComponent input = new InputComponent("Insert your new branch name (spaces are not allowed):");
        input.subscribe((e) -> {
            if(e.getData().isBlank() || e.getData().contains(" ")) {
                out.println("Branch name must not be empty! & spaces are not allowed!");
                input.render(out);
            } else {
                try {
                    template.postForLocation(ROUTE+e.getData(), null);
                } catch (RestClientResponseException ex) {
                    ErrorDetails err = ex.getResponseBodyAs(ErrorDetails.class);
                    out.println(err.message());
                } catch (Exception ex) {
                    out.println("an unexpected error occurred please try again later.");
                }
            }
        });
        page.add(new LogoComponent("Create New Branch"));
        page.add(input);
    }
}
