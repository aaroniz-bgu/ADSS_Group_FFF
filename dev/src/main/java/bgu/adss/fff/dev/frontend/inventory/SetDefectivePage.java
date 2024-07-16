package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.EmployeeDto;
import bgu.adss.fff.dev.contracts.ItemDto;
import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class SetDefectivePage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/product";
    private final RestTemplate restTemplate;
    private final EmployeeDto employee;

    private final InputComponent productIDInput;
    private final InputComponent itemIDInput;

    private long productID;
    private long itemID;

    public SetDefectivePage(PrintStream out, EmployeeDto employee) {
        super(out);

        this.employee = employee;

        restTemplate = new RestTemplate();

        productIDInput = new InputComponent("Enter product ID (מק''ט): ");
        itemIDInput = new InputComponent("Enter item ID (מספר מוצר): ");

        productIDInput.subscribe(this::onProductIDInput);
        itemIDInput.subscribe(this::onItemIDInput);

        page.add(new LogoComponent("Set Item as Defective"));
        page.add(productIDInput);
        page.add(itemIDInput);
    }

    private void onProductIDInput(StateEvent event) {
        try {
            this.productID = Long.parseUnsignedLong(event.getData());
            printAvailableItems();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            productIDInput.render(out);
        }
    }

    private void onItemIDInput(StateEvent event) {
        try {
            this.itemID = Long.parseUnsignedLong(event.getData());
            setItemDefective();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            itemIDInput.render(out);
        }
    }

    private void printAvailableItems() {

        try {
            ProductDto response = restTemplate.getForObject(ROUTE + "/" + productID, ProductDto.class);
            if(response == null) {
                out.println("Product with ID " + productID + " not found.");
                return;
            }
            out.println("Available items for product " + productID + ":");
            out.println("Shelves:");
            for(ItemDto item : response.shelves()) {
                out.println("\t- Item ID: " + item.itemID() + ", Expiration Date: " + item.expirationDate());
            }
            out.println("Storage:");
            for(ItemDto item : response.storage()) {
                out.println("\t- Item ID: " + item.itemID() + ", Expiration Date: " + item.expirationDate() +
                        ", Defective: " + item.isDefected());
            }
        } catch (Exception e) { out.println(e.getMessage()); }
    }

    private void setItemDefective() {

        try {
            restTemplate.put(ROUTE + "/item/defective/" + productID,
                    new ItemDto(this.itemID, null, true, employee.branchName()));
            out.println("Item with ID " + itemID + " was set as defective.");
        } catch (Exception e) { out.println(e.getMessage()); }
    }

}
