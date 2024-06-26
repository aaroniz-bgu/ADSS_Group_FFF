package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class ChangeProductPricePage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/product";
    private final RestTemplate restTemplate;

    private final InputComponent idInput;
    private final InputComponent priceInput;

    private long id;
    private float price;

    public ChangeProductPricePage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        idInput = new InputComponent("Enter product ID (מק''ט): ");
        priceInput = new InputComponent("Enter product new price (מחיר חדש): ");

        idInput.subscribe(this::onIdInput);
        priceInput.subscribe(this::onPriceInput);

        page.add(new LogoComponent("Change price of a Product"));
        page.add(idInput);
        page.add(priceInput);
    }

    private void onIdInput(StateEvent event) {
        try {
            this.id = Long.parseUnsignedLong(event.getData());
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            idInput.render(out);
        }
    }

    private void onPriceInput(StateEvent event) {
        try {
            float price = Float.parseFloat(event.getData());
            if (price < 0)
                throw new IllegalArgumentException("Price cannot be negative.");
            this.price = price;
            changeProductPrice();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            priceInput.render(out);
        }
    }

    private void changeProductPrice() {

        try {

            ProductDto response = restTemplate.getForObject(ROUTE + "/" + id, ProductDto.class);
            if(response == null) {
                out.println("Product with ID " + id + " not found.");
                return;
            }
            ProductDto updatedProduct = new ProductDto(
                    this.id, response.productName(),
                    this.price, response.discount(), response.shelves(), response.storage(),
                    response.minimalQuantity(), response.supplierID(), response.supplierPrice()
            );
            restTemplate.put(ROUTE, updatedProduct);
            out.println("Product's price changed - " + response.productID() + "(" + response.productName()
                    + ") - " + response.price());

        } catch (Exception e) { out.println(e.getMessage()); }
    }

}
