package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.DiscountDto;
import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.contracts.RequestDiscountDto;
import bgu.adss.fff.dev.domain.models.Discount;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class AddProductDiscountPage extends AbstractUserComponent {

    private static final String PRODUCT_ROUTE = URI_PATH + "/product";
    private static final String DISCOUNT_ROUTE = URI_PATH + "/discount";
    private final RestTemplate restTemplate;

    private final InputComponent idInput;
    private final InputComponent startDateInput;
    private final InputComponent endDateInput;
    private final InputComponent discountPercentInput;

    private long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private float discountPercent;

    public AddProductDiscountPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        idInput = new InputComponent("Enter product ID (מק''ט): ");
        startDateInput = new InputComponent("Enter discount start date (תאריך התחלה): ");
        endDateInput = new InputComponent("Enter discount end date (תאריך סיום): ");
        discountPercentInput = new InputComponent("Enter discount percent (אחוז הנחה): ");

        idInput.subscribe(this::onIdInput);
        startDateInput.subscribe(this::onStartDateInput);
        endDateInput.subscribe(this::onEndDateInput);
        discountPercentInput.subscribe(this::onDiscountPercentInput);

        page.add(new LogoComponent("Add discount properties for the Product"));
        page.add(idInput);
        page.add(startDateInput);
        page.add(endDateInput);
        page.add(discountPercentInput);
    }

    private void onIdInput(StateEvent event) {
        try {
            this.id = Long.parseUnsignedLong(event.getData());
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            idInput.render(out);
        }
    }

    private void onStartDateInput(StateEvent event) {
        try {
            this.startDate = LocalDate.parse(event.getData());
        } catch (Exception e) {
            out.println(e.getMessage());
            startDateInput.render(out);
        }
    }

    private void onEndDateInput(StateEvent event) {
        try {
            this.endDate = LocalDate.parse(event.getData());
        } catch (Exception e) {
            out.println(e.getMessage());
            endDateInput.render(out);
        }
    }

    private void onDiscountPercentInput(StateEvent event) {
        try {
            float discountPercent = Float.parseFloat(event.getData());
            if (discountPercent < 0)
                throw new IllegalArgumentException("Discount percent cannot be negative.");
            this.discountPercent = discountPercent;
            addProductDiscount();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            discountPercentInput.render(out);
        }
    }

    private void addProductDiscount() {

        ProductDto productDto = restTemplate.getForObject(PRODUCT_ROUTE + "/" + id, ProductDto.class);
        if(productDto == null) {
            out.println("Product with ID " + id + " not found.");
            return;
        }
        DiscountDto discountDto = restTemplate.postForObject(DISCOUNT_ROUTE, new RequestDiscountDto( startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), discountPercent), DiscountDto.class);
        restTemplate.put(PRODUCT_ROUTE + "/discount/" + id, discountDto);

        out.println("Discount added to product " + productDto.productName() + " successfully.");
    }

}
