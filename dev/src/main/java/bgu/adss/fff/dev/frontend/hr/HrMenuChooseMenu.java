package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.components.ListComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;

import java.io.PrintStream;
import java.util.List;

public class HrMenuChooseMenu extends AbstractUserComponent {
    private final InputComponent input;
    EmployeeDto emp;
    public HrMenuChooseMenu(PrintStream out, EmployeeDto emp) {
        super(out);
        this.emp = emp;
        input = new InputComponent("Please insert 1 or 2:");
        input.subscribe((s) -> {
            try {
                int choice = Integer.parseInt(s.getData());
                switch(choice) {
                    case 1:
                        new EmployeeMenuPage(out, emp).render();
                        break;
                    case 2:
                        new HrManagerMenuPage(out, emp).render();
                        break;
                    default:
                        out.println("Please insert only numbers between 1 and 2!");
                        input.render(out);
                }
            } catch (NumberFormatException e) {
                out.println("Please insert only numbers between 1 and 2!");
                input.render(out);
            }
        });
        page.add(new LogoComponent("Welcome "+emp.name()));
        page.add(new LabelComponent("Since you're part of the HR department you may choose which menu you need:"));
        page.add(new ListComponent<>(List.of("Regular Employee Menu", "HR Menu")));
        page.add(input);
    }
}
