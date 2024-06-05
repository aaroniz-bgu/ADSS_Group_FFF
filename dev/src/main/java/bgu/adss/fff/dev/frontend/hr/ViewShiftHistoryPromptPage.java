package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.contracts.ShiftDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.ListComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.util.Arrays;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class ViewShiftHistoryPromptPage extends AbstractUserComponent {

    private static final String BRANCHES_ROUTE = URI_PATH + "/branch";
    private static final String SHIFT_ROUTE = URI_PATH + "/shift/from=";

    private final RestTemplate template;

    private final InputComponent branchInput;
    private final InputComponent fromInput;
    private final InputComponent toInput;

    private String branch;
    private String from;
    private String to;

    public ViewShiftHistoryPromptPage(PrintStream out) throws RestClientException {
        super(out);

        this.template = new RestTemplate();

        String[] branches = template.getForEntity(BRANCHES_ROUTE, String[].class).getBody();

        this.branchInput = new InputComponent("Which branch you'd like to see the history of?");
        this.fromInput = new InputComponent(
                "Insert the earliest date you'd like to see using the following pattern: [dd-MM-yyyy]");
        this.toInput = new InputComponent(
                "Insert the latest date you'd like to see using the following pattern [dd-MM-yyyy]");

        branchInput.subscribe(this::onBranch);
        fromInput.subscribe(this::onFrom);
        toInput.subscribe(this::onTo);

        page.add(new LogoComponent("Shift History"));
        page.add(new ListComponent<>(Arrays.asList(branches)));
        page.add(branchInput);
        page.add(fromInput);
        page.add(toInput);
    }

    private void onBranch(StateEvent event) {
        branch = event.getData();
    }

    private void onFrom(StateEvent event) {
        if(checkDateFormat(event.getData())) {
            from = event.getData();
        } else {
            out.println("Please use the format given!");
            fromInput.render(out);
        }
    }

    private void onTo(StateEvent event) {
        if(checkDateFormat(event.getData())) {
            to = event.getData();
            sendRequest();
        } else {
            out.println("Please use the format given!");
            toInput.render(out);
        }
    }

    private boolean checkDateFormat(String s) {
        String regex = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
        return s.matches(regex);
    }

    private void sendRequest() {
        try {
            ShiftDto[] shifts = template.getForEntity(SHIFT_ROUTE+from+"&to="+to+"&"+branch,ShiftDto[].class)
                    .getBody();
            new ViewShiftHistoryPage(out, shifts).render();
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }
}
