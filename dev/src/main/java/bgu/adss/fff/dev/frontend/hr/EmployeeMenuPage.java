package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.ListComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;

import java.io.PrintStream;
import java.util.List;

public class EmployeeMenuPage extends AbstractUserComponent {

    private final EmployeeDto employee;

    public EmployeeMenuPage(PrintStream out, EmployeeDto employee) {
        super(out);

        this.employee = employee;

        page.add(new LogoComponent("Welcome "+employee.name()));
        page.add(new ListComponent<String>(
                List.of("Update availability", "Exit")
        ));
        InputComponent input = new InputComponent("Your choice:");
        input.subscribe(this::onChoice);
        page.add(input);
    }

    private void onChoice(StateEvent event) {
        String input = event.getData();
        try {
            int choice = Integer.parseInt(input);
            boolean rerender = true;

            switch(choice) {
                case 1:
                    new ReportAvailabilityPage(out, employee).render();
                    break;
                case 2:
                    rerender = false;
                    break;
                default:
                    break;
            }

            if(rerender) this.render();
            // If the user is retarded:
        } catch (NumberFormatException e) {
            out.println("Please choose a number from the list without any additional characters.");
        }
    }
}
