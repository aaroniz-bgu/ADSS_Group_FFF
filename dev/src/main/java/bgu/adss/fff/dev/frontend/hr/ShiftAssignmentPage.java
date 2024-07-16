package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.util.Arrays;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class ShiftAssignmentPage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/branch";

    public ShiftAssignmentPage(PrintStream out) {
        super(out);
        RestTemplate template = new RestTemplate();
        String branchesList;
        try {
            String[] branches = template.getForEntity(ROUTE, String[].class).getBody();
            branchesList = Arrays.toString(branches);
        } catch (Exception e) {
            // log...?
            branchesList = "[]";
        }
        InputComponent pickBranchInput = new InputComponent("Choose a branch from the list: \n"
                + branchesList);
        pickBranchInput.subscribe(this::onBranchPicked);
        page.add(new LogoComponent("Assign Employees To Shift"));
        page.add(pickBranchInput);
    }

    private void onBranchPicked(StateEvent event) {
        try {
            new ShiftAssignmentBranchPage(out, event.getData()).render();
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }

}
