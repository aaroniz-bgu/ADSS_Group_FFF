package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.DiscountDto;
import bgu.adss.fff.dev.contracts.ItemDto;
import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.contracts.RequestItemDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class AddItemBatchPage extends AbstractUserComponent {

    private static final String PRODUCT_ROUTE = URI_PATH + "/product";
    private static final String ITEM_ROUTE = URI_PATH + "/item";
    private final RestTemplate restTemplate;

    private final InputComponent idInput;
    private final InputComponent expirationDateInput;
    private final InputComponent amountInput;

    private long id;
    private LocalDate expirationDate;
    private int amount;

    public AddItemBatchPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        idInput = new InputComponent("Enter product ID (מק''ט): ");
        expirationDateInput = new InputComponent("Enter expiration date (תאריך תפוגה): ");
        amountInput = new InputComponent("Enter amount: ");

        idInput.subscribe(this::onIdInput);
        expirationDateInput.subscribe(this::onExpirationDateInput);
        amountInput.subscribe(this::onAmountInput);

        page.add(new LogoComponent("Add discount properties for the Product"));
        page.add(idInput);
        page.add(expirationDateInput);
        page.add(amountInput);
    }

    private void onIdInput(StateEvent event) {
        try {
            this.id = Long.parseUnsignedLong(event.getData());
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            idInput.render(out);
        }
    }

    private void onExpirationDateInput(StateEvent event) {
        try {
            this.expirationDate = LocalDate.parse(event.getData());
        } catch (Exception e) {
            out.println(e.getMessage());
            expirationDateInput.render(out);
        }
    }

    private void onAmountInput(StateEvent event) {
        try {
            this.amount = Integer.parseInt(event.getData());
            addItems();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            amountInput.render(out);
        }
    }

    private void addItems() {

        System.out.println(id);
        ProductDto productDto = restTemplate.getForObject(PRODUCT_ROUTE + "/" + id, ProductDto.class);
        if(productDto == null) {
            out.println("Product with ID " + id + " not found.");
            return;
        }

        ItemDto[] itemDto = restTemplate.postForObject(
                PRODUCT_ROUTE + "/item/" + id,
                new RequestItemDto(
                        expirationDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                        false,  amount),
                ItemDto[].class);

        out.println("Added " + amount + " items to product " + productDto.productName() +
                " (ID: " + productDto.productID() + ").");
    }

}
