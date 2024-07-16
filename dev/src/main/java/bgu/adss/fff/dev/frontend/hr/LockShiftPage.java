package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.contracts.ShiftDto;
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

public class LockShiftPage extends AbstractUserComponent {

    private static final String BROUTE = URI_PATH + "/branch";
    private static final String ROUTE = URI_PATH + "/shift/";

    private final RestTemplate template;

    private final InputComponent lockInput;
    private final InputComponent dateInput;
    private final InputComponent partInput;

    private String branch;
    private boolean lock;
    private LocalDate date;
    private int part;
    protected LockShiftPage(PrintStream out) {
        super(out);
        template = new RestTemplate();
        String branchesList;
        try {
            String[] branches = template.getForEntity(BROUTE, String[].class).getBody();
            branchesList = Arrays.toString(branches);
        } catch (Exception e) {
            // log...?
            branchesList = "[]";
        }
        InputComponent branchInput = new InputComponent("Insert the branch of the shift you want to lock/unlock:" +
                "\n"+branchesList);
        lockInput = new InputComponent("[Y/N] Do you want to lock for reports? (n === unlock)");
        dateInput = new InputComponent("Insert the date [dd-MM-yyyy] which you want to update:");
        partInput = new InputComponent("Insert 0 for morning shift and 1 for evening shift:");

        branchInput.subscribe(this::onBranchInput);
        lockInput.subscribe(this::onLockInput);
        dateInput.subscribe(this::onDateInput);
        partInput.subscribe(this::onPartInput);

        page.add(new LogoComponent("Lock Shifts"));
        page.add(branchInput);
        page.add(lockInput);
        page.add(dateInput);
        page.add(partInput);
    }

    private void onBranchInput(StateEvent event) {
        branch = event.getData();
    }

    private void onLockInput(StateEvent event) {
        String choice = event.getData().toLowerCase();
        if(choice.equals("y")) {
            lock = true;
        } else if (choice.equals("n")) {
            lock = false;
        } else {
            out.println("Please insert only values that you were instructed to!");
            lockInput.render(out);
        }
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
            sendRequest();
        } catch (RuntimeException e) {
            out.println("Please insert only values that you're instructed to!");
            partInput.render(out);
        }
    }

    private void sendRequest() {
        try {
            template.put(ROUTE + lock, new ShiftDto(
                    date, part, branch, false, null, null, null
            ));
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }
}
