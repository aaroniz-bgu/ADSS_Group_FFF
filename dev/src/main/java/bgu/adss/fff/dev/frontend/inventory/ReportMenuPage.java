package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;

import java.io.PrintStream;

public class ReportMenuPage extends AbstractUserComponent {

    private final InputComponent chooseMenuOption;

    public ReportMenuPage(PrintStream out) {
        super(out);

        page.add(new LogoComponent("Report Menu"));

        page.add(new LabelComponent("1. Create Inventory Report"));
        page.add(new LabelComponent("2. Get Previous Inventory Report"));

        page.add(new LabelComponent("3. Create Out Of Stock Report"));
        page.add(new LabelComponent("4. Get Previous Out Of Stock Report"));

        page.add(new LabelComponent("5. Create Defective Items Report"));
        page.add(new LabelComponent("6. Get Previous Defective Items Report"));

        page.add(new LabelComponent("7. Back"));

        chooseMenuOption = new InputComponent("Choose an option: ");
        chooseMenuOption.subscribe(this::onChooseMenuOption);
        page.add(chooseMenuOption);
    }

    private void onChooseMenuOption(StateEvent event) {
        try {
            int menuOption = Integer.parseInt(event.getData());

            switch (menuOption) {
                case 1:
                    System.out.println("Rendering CreateInventoryReportPage");
                    break;
                case 2:
                    System.out.println("Rendering GetPreviousInventoryReportPage");
                    break;
                case 3:
                    System.out.println("Rendering CreateOutOfStockReportPage");
                    break;
                case 4:
                    System.out.println("Rendering GetPreviousOutOfStockReportPage");
                    break;
                case 5:
                    System.out.println("Rendering CreateDefectiveItemsReportPage");
                    break;
                case 6:
                    System.out.println("Rendering GetPreviousDefectiveItemsReportPage");
                    break;
                case 7:
                    // By not rendering anything, we effectively go back to the InventoryMenuPage
                    break;
                default:
                    throw new NumberFormatException("Invalid option");
            }

        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            chooseMenuOption.render(out);
        }
    }

}
