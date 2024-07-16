package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;

import java.io.PrintStream;

public class InventoryMenuPage extends AbstractUserComponent {

    private final InputComponent chooseMenuOption;
    private final EmployeeDto employee;

    public InventoryMenuPage(PrintStream out, EmployeeDto employee) {
        super(out);

        this.employee = employee;

        page.add(new LogoComponent("Inventory Menu"));
        page.add(new LabelComponent("1. Product Menu"));
        page.add(new LabelComponent("2. Category Menu"));
        page.add(new LabelComponent("3. Report Menu"));
        page.add(new LabelComponent("4. Exit"));

        chooseMenuOption = new InputComponent("Choose an option: ");
        chooseMenuOption.subscribe(this::onChooseMenuOption);
        page.add(chooseMenuOption);
    }

    private void onChooseMenuOption(StateEvent event) {
        boolean rerender = true;
        try {
            int menuOption = Integer.parseInt(event.getData());
            switch (menuOption) {
                case 1:
                    new ProductMenuPage(out, employee).render();
                    break;
                case 2:
                    new CategoryMenuPage(out).render();
                    break;
                case 3:
                    new ReportMenuPage(out).render();
                    break;
                case 4:
                    rerender = false;
                    break;
                default:
                    throw new NumberFormatException("Invalid option");
            }
            if(rerender) this.render();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            chooseMenuOption.render(out);
        }
    }

}
