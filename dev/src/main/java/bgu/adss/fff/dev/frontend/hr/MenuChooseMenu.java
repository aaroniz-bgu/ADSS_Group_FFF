package bgu.adss.fff.dev.frontend.hr;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.components.ListComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;

import java.io.PrintStream;
import java.util.List;

public class MenuChooseMenu extends AbstractUserComponent {
    private final InputComponent input;
    EmployeeDto emp;
    public MenuChooseMenu(PrintStream out, EmployeeDto emp,
                            List<String> screensNames, List<AbstractUserComponent> screens) {
        super(out);
        this.emp = emp;
        input = new InputComponent("Please insert a number 1 to " + (1 + screens.size()) + ":");
        input.subscribe((s) -> {
            try {
                int choice = Integer.parseInt(s.getData());
                if(choice == 1) {
                    new EmployeeMenuPage(out, emp).render();
                } else if ((choice - 1) <= screensNames.size()) {
                    screens.get(choice - 2).render();
                } else {
                    out.println("Please insert only numbers between 1 and 2!");
                    input.render(out);
                }
            } catch (NumberFormatException e) {
                out.println("Please insert only numbers between 1 to " + (1 + screens.size()) + "!");
                input.render(out);
            }
        });
        page.add(new LogoComponent("Welcome "+emp.name()));
        page.add(new LabelComponent(
                "Due to multiple roles that are applied to your account you may choose what you'd like to do:"));
        List<String> toRenderList = new java.util.ArrayList<>(List.of("Regular Employee Menu"));
        toRenderList.addAll(screensNames);
        page.add(new ListComponent<>(toRenderList));
        page.add(input);
    }
}
