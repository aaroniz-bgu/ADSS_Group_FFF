package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class GetProductPage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/product";
    private final RestTemplate restTemplate;

    private final InputComponent idInput;

    private long id;

    public GetProductPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        idInput = new InputComponent("Enter product ID (מק''ט): ");

        idInput.subscribe(this::onIdInput);

        page.add(new LogoComponent("Get Product"));
        page.add(idInput);
    }

    private void onIdInput(StateEvent event) {
        try {
            this.id = Long.parseUnsignedLong(event.getData());
            getProduct();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            idInput.render(out);
        }
    }

    private void getProduct() {
        ProductDto response = restTemplate.getForObject(ROUTE + "/" + id, ProductDto.class);
        if(response == null) {
            out.println("Product with ID " + id + " not found.");
            return;
        }

        String discountToString = "Discount: No discount";
        if(response.discount() != null) {
            discountToString = "Discount: " + "\n" + "\t" +
                    "Discount ID: " + response.discount().discountID() + "\n" + "\t" +
                    "Discount Start Date: " + response.discount().startDate() + "\n" + "\t" +
                    "Discount End Date: " + response.discount().endDate() + "\n" + "\t" +
                    "Discount Percent: " + response.discount().discountPercent();
        }

        String product = "Product ID: " + response.productID() + "\n" +
                "Product Name: " + response.productName() + "\n" +
                "Product Price: " + response.price() + "\n" +
                "Minimal Amount: " + response.minimalQuantity() + "\n" +
                "Items on shelves: " + response.shelves().length + "\n" +
                "Items in storage: " + response.storage().length + "\n" +
                discountToString;
        out.println(product);
    }

}
