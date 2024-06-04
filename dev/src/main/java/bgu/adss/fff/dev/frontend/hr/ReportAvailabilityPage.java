package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.contracts.ReportShiftRequest;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class ReportAvailabilityPage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH +  "/shift";
    private static final String ASS_ROUTE = ROUTE + "/available/";

    InputComponent dateInput;
    InputComponent shiftInput;

    private final EmployeeDto employee;
    private String date;
    private int shift;

    protected ReportAvailabilityPage(PrintStream out, EmployeeDto employee) {
        super(out);

        dateInput = new InputComponent("Insert the date of shift [dd-MM-yyyy] without the brackets:");
        shiftInput = new InputComponent("Insert the shift MORNING/EVENING using [0/1]:");

        this.employee = employee;

        dateInput.subscribe(this::onDateInserted);
        shiftInput.subscribe(this::onShiftInserted);

        page.add(new LogoComponent("Availability"));
        page.add(dateInput);
        page.add(shiftInput);
    }

    private void onDateInserted(StateEvent event) {
        // Check that the date is in form of `dd-MM-yyyy`:
        date = event.getData();
        String regex = "^(3[01]|[12][0-9]|0[1-9])-(1[0-2]|0[1-9])-[0-9]{4}$";
        if(!date.matches(regex)) {
            out.println("Please make sure your date is formatted properly [dd-MM-yyyy]");
            dateInput.render(out);
        }
    }

    private void onShiftInserted(StateEvent event) {
        try {
            shift = Integer.parseInt(event.getData());
            if(shift != 0 && shift != 1) {
                wrongShiftInput();
                return;
            }
            result();
        } catch (NumberFormatException e) {
            wrongShiftInput();
        }
    }

    private void wrongShiftInput() {
        out.println("Please insert 0 or 1.");
        shiftInput.render(out);
    }

    private void result() {
        ReportShiftRequest request = new ReportShiftRequest(
                LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy")), shift, employee.id());

        RestTemplate template = new RestTemplate();
        try {
            template.postForLocation(ROUTE, request);

            // Retrieve available emps:
            String route = ASS_ROUTE + date + "&" + shift + "&" + employee.branchName();
            EmployeeDto[] response = template.getForEntity(route, EmployeeDto[].class).getBody();

            boolean isIn = false;
            for(EmployeeDto emp : response) {
                if(emp.id() == employee.id()) {
                    isIn = true;
                }
            }

            if(isIn) {
                out.println("Reported : AVAILABLE");
            } else {
                out.println("Reported: NOT-AVAILABLE");
            }
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
        } catch (Exception e) {
            out.println("an unexpected error occurred please try again later.");
        }
    }
}
