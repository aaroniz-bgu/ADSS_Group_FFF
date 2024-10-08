package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.ItemDto;
import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import java.util.Objects;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class AddProductPage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/product";
    private final RestTemplate restTemplate;

    private final InputComponent idInput;
    private final InputComponent nameInput;
    private final InputComponent priceInput;
    private final InputComponent minimalAmountInput;
    private final InputComponent supplierInput;
    private final InputComponent supplierPriceInput;

    private long id;
    private String name;
    private float price;
    private int minimalAmount;
    private long supplierID;
    private float supplierPrice;

    public AddProductPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        idInput = new InputComponent("Enter product ID (מק''ט): ");
        nameInput = new InputComponent("Enter product name (שם): ");
        priceInput = new InputComponent("Enter product price (מחיר): ");
        minimalAmountInput = new InputComponent("Enter minimal amount (כמות מינימלית): ");
        supplierInput = new InputComponent("Enter supplier ID (מספר ספק): ");
        supplierPriceInput = new InputComponent("Enter supplier price (מחיר ספק): ");

        idInput.subscribe(this::onIdInput);
        nameInput.subscribe(this::onNameInput);
        priceInput.subscribe(this::onPriceInput);
        minimalAmountInput.subscribe(this::onMinimalAmountInput);
        supplierInput.subscribe(this::onSupplierInput);
        supplierPriceInput.subscribe(this::onSupplierPriceInput);

        page.add(new LogoComponent("Create New Product"));
        page.add(idInput);
        page.add(nameInput);
        page.add(priceInput);
        page.add(minimalAmountInput);
        page.add(supplierInput);
        page.add(supplierPriceInput);
    }

    private void onIdInput(StateEvent event) {
        try {
            this.id = Long.parseUnsignedLong(event.getData());
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            idInput.render(out);
        }
    }

    private void onNameInput(StateEvent event) {
        try {
            if (event.getData().isBlank())
                throw new IllegalArgumentException("Name cannot be empty.");
            this.name = event.getData();
        } catch (Exception e) {
            out.println(e.getMessage());
            nameInput.render(out);
        }
    }

    private void onPriceInput(StateEvent event) {
        try {
            float price = Float.parseFloat(event.getData());
            if (price < 0)
                throw new IllegalArgumentException("Price cannot be negative.");
            this.price = price;
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            priceInput.render(out);
        }
    }

    private void onMinimalAmountInput(StateEvent event) {
        try {
            int minimalAmount = Integer.parseUnsignedInt(event.getData());
            if (minimalAmount < 0)
                throw new IllegalArgumentException("Minimal amount cannot be negative.");
            this.minimalAmount = minimalAmount;
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            minimalAmountInput.render(out);
        }
    }

    private void onSupplierInput(StateEvent event) {
        try {
            this.supplierID = Long.parseUnsignedLong(event.getData());
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            supplierInput.render(out);
        }
    }

    private void onSupplierPriceInput(StateEvent event) {
        try {
            float supplierPrice = Float.parseFloat(event.getData());
            if (supplierPrice < 0)
                throw new IllegalArgumentException("Supplier price cannot be negative.");
            this.supplierPrice = supplierPrice;
            createProduct();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            supplierPriceInput.render(out);
        }
    }

    private void createProduct() {

        try {
            ProductDto product = new ProductDto(id, name, price, null,
                    new ItemDto[0], new ItemDto[0], minimalAmount, supplierID, supplierPrice);
            ProductDto response = restTemplate.postForObject(ROUTE, product, ProductDto.class);
            Objects.requireNonNull(response);

            out.println("Product Created - " + response.productID() + ":" + response.productName());
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }
}
