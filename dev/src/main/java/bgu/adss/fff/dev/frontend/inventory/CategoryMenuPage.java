package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;

import java.io.PrintStream;

public class CategoryMenuPage extends AbstractUserComponent {

    private final InputComponent chooseMenuOption;

    public CategoryMenuPage(PrintStream out) {
        super(out);

        page.add(new LogoComponent("Category Menu"));

        page.add(new LabelComponent("1. Add Category"));
        page.add(new LabelComponent("2. Get Category"));

        page.add(new LabelComponent("3. Assign Category to Product"));
        page.add(new LabelComponent("4. See Category Products"));

        page.add(new LabelComponent("5. Apply Discount to Category"));

        page.add(new LabelComponent("6. Back"));

        chooseMenuOption = new InputComponent("Choose an option: ");
        chooseMenuOption.subscribe(this::onChooseMenuOption);
        page.add(chooseMenuOption);
    }

    private void onChooseMenuOption(StateEvent event) {
        try {
            int menuOption = Integer.parseInt(event.getData());

            switch (menuOption) {
                case 1:
                    new AddCategoryPage(out).render();
                    break;
                case 2:
                    new GetCategoryPage(out).render();
                    break;
                case 3:
                    new AssignProductCategoryPage(out).render();
                    break;
                case 4:
                    new SeeCategoryProductsPage(out).render();
                    break;
                case 5:
                    new AddCategoryDiscountPage(out).render();
                    break;
                case 6:
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
