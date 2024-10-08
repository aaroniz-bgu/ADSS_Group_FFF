package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.ErrorDetails;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.io.PrintStream;

public class EditEmployeeDetailsPage extends AbstractUserComponent {

    private final InputComponent idInput;
    private final InputComponent termsOrEmployeeInput;

    private long id;
    public EditEmployeeDetailsPage(PrintStream out) {
        super(out);

        idInput = new InputComponent("Insert employee ID:");
        termsOrEmployeeInput = new InputComponent("Would you like to edit employee details[E] or the employment terms[T]?");

        idInput.subscribe(this::onIdInput);
        termsOrEmployeeInput.subscribe(this::onTermsOrEmployeeInput);

        page.add(new LogoComponent("Edit Employee Details"));
        page.add(idInput);
        page.add(termsOrEmployeeInput);
    }

    private void onIdInput(StateEvent event) {
        try {
            this.id = Long.parseUnsignedLong(event.getData());
        } catch (NumberFormatException e) {
            out.println("Please type a proper id which is supposed to be a number.");
            idInput.render(out);
        }
    }

    private void onTermsOrEmployeeInput(StateEvent event) {
        String choice = event.getData().toLowerCase();
        try {
            if (choice.equals("t")) {
                new EditEmployeeTermsPage(out, id).render();
            } else if (choice.equals("e")) {
                new EditEmployeeDetailsOnlyPage(out, id).render();
            } else {
                out.println("Please insert only values that you were instructed to!");
                termsOrEmployeeInput.render(out);
            }
        } catch (RestClientResponseException e) {
            ErrorDetails err = e.getResponseBodyAs(ErrorDetails.class);
            out.println(err.message());
        }
    }
}
