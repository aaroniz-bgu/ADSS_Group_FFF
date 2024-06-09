package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.*;
import bgu.adss.fff.dev.exceptions.ProductException;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.format.DateTimeFormatter;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class MoveItemsToShelvesPage extends AbstractUserComponent {

    private static final String PRODUCT_ROUTE = URI_PATH + "/product";
    private static final String AMOUNT_ROUTE = URI_PATH + "/item";
    private final RestTemplate restTemplate;

    private final InputComponent idInput;
    private final InputComponent quantityInput;

    private long id;
    private int amount;

    public MoveItemsToShelvesPage(PrintStream out) {
        super(out);
        restTemplate = new RestTemplate();

        idInput = new InputComponent("Enter product ID (מק''ט): ");
        quantityInput = new InputComponent("Enter quantity to move: ");

        idInput.subscribe(this::onIdInput);
        quantityInput.subscribe(this::onQuantityInput);

        page.add(new LogoComponent("Move items to shelves"));
        page.add(idInput);
        page.add(quantityInput);
    }

    private void onIdInput(StateEvent event) {
        try {
            this.id = Long.parseUnsignedLong(event.getData());
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            idInput.render(out);
        }
    }

    private void onQuantityInput(StateEvent event) {
        try {
            this.amount = Integer.parseInt(event.getData());
            moveItemsToShelves();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            quantityInput.render(out);
        }
    }

    private void moveItemsToShelves() {
        ProductDto productDto = restTemplate.getForObject(PRODUCT_ROUTE + "/" + id, ProductDto.class);
        if(productDto == null) {
            out.println("Product with ID " + id + " not found.");
            return;
        }
        try{
            restTemplate.put(PRODUCT_ROUTE + "/item/" + id, new RequestAmountDto(amount));
            out.println("Moved " + amount + " items to shelves from storage.");
        } catch (Exception e) {
            out.println("Not enough items in storage.");
            return;
        }
    }

}
