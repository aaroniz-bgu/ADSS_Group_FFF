package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.EmployeeDto;
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
    private final EmployeeDto employee;

    private final InputComponent idInput;

    private long id;

    public GetProductPage(PrintStream out, EmployeeDto employee) {
        super(out);

        this.employee = employee;

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

        try {
            String connection = ROUTE + "/" + id;
            ProductDto response = restTemplate.getForObject(connection, ProductDto.class);
            if (response == null) {
                out.println("Product with ID " + id + " not found.");
                return;
            }

            String discountToString = "Discount: No discount";
            if (response.discount() != null) {
                discountToString = "Discount: " + "\n" + "\t" +
                        "Discount ID: " + response.discount().discountID() + "\n" + "\t" +
                        "Discount Start Date: " + response.discount().startDate() + "\n" + "\t" +
                        "Discount End Date: " + response.discount().endDate() + "\n" + "\t" +
                        "Discount Percent: " + response.discount().discountPercent();
            }

            // Counts the amount of items of this product in this branch.
            int shelvesCount = 0, storageCount = 0;
            for (var item : response.shelves()) {
                if(item.branch().equals(employee.branchName())) shelvesCount++;
            }
            for (var item : response.storage()) {
                if(item.branch().equals(employee.branchName())) storageCount++;
            }

            String product = "Product ID: " + response.productID() + "\n" +
                    "Product Name: " + response.productName() + "\n" +
                    "Product Price: " + response.price() + "\n" +
                    "Minimal Amount: " + response.minimalQuantity() + "\n" +
                    "Items on shelves: " + shelvesCount + "\n" +
                    "Items in storage: " + storageCount + "\n" +
                    discountToString;
            out.println(product);
        } catch (Exception e) { out.println(e.getMessage()); }
    }


}
