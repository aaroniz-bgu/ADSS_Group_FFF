package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.LabelComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;

import java.io.PrintStream;

public class ProductMenuPage extends AbstractUserComponent {

    private final InputComponent chooseMenuOption;

    public ProductMenuPage(PrintStream out) {
        super(out);

        page.add(new LogoComponent("Product Menu"));

        page.add(new LabelComponent("1. Add Product"));
        page.add(new LabelComponent("2. Get Product"));

        page.add(new LabelComponent("3. Add Item Batch"));
        page.add(new LabelComponent("4. Move Items to Shelves"));
        page.add(new LabelComponent("5. Set Item as Defective"));

        page.add(new LabelComponent("6. Apply Discount"));
        page.add(new LabelComponent("7. Update Price"));

        page.add(new LabelComponent("8. Back"));

        chooseMenuOption = new InputComponent("Choose an option: ");
        chooseMenuOption.subscribe(this::onChooseMenuOption);
        page.add(chooseMenuOption);
    }

    private void onChooseMenuOption(StateEvent event) {
        try {
            int menuOption = Integer.parseInt(event.getData());

            switch (menuOption) {
                case 1:
                    new AddProductPage(out).render();
                    break;
                case 2:
                    System.out.println("Rendering GetProductPage");
                    break;
                case 3:
                    System.out.println("Rendering AddItemBatchPage");
                    break;
                case 4:
                    System.out.println("Rendering MoveItemsToShelvesPage");
                    break;
                case 5:
                    System.out.println("Rendering SetItemAsDefectivePage");
                    break;
                case 6:
                    System.out.println("Rendering ApplyDiscountPage");
                    break;
                case 7:
                    System.out.println("Rendering UpdatePricePage");
                    break;
                case 8:
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
