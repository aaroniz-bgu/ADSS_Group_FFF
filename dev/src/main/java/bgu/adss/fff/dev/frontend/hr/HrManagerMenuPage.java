package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.ListComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;

import java.io.PrintStream;
import java.util.List;

public class HrManagerMenuPage extends AbstractUserComponent {

    private final EmployeeDto employee;
    private final InputComponent choiceInput;

    protected HrManagerMenuPage(PrintStream out, EmployeeDto employee) {
        super(out);

        this.employee = employee;

        this.choiceInput = new InputComponent("Choose operation to perform:");

        page.add(new LogoComponent("Welcome " + employee.name() + " : HUMAN RESOURCES DEPARTMENT"));
        page.add(new ListComponent<String>(List.of(
                "Register new employee",
                "Edit existing employee details",
                "[#] Shift Assignment Interface", // Choose week->show shifts table??
                "Un/lock shifts to reports",
                "View shift history",
                "Require roles",
                "Exit"
        )));
        choiceInput.subscribe(this::onChoice);
        page.add(choiceInput);
    }

    private void onChoice(StateEvent event) {
        boolean rerender = true;
        try {
            int choice = Integer.parseInt(event.getData());
            switch (choice) {
                case 1: //TODO
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
                    break;
                default:
                    rerender = false;
                    break;
            }
            if(rerender) this.render();
        } catch(NumberFormatException e) {
            out.println("Please provide your choice as a number without any additional characters.");
            this.render();
        }
    }
}
