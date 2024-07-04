package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.contracts.DiscountDto;
import bgu.adss.fff.dev.contracts.RequestDiscountDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class AddCategoryDiscountPage extends AbstractUserComponent {

    private static final String CATEGORY_ROUTE = URI_PATH + "/category";
    private static final String DISCOUNT_ROUTE = URI_PATH + "/discount";
    private final RestTemplate restTemplate;

    private final InputComponent categoryNameInput;
    private final InputComponent startDateInput;
    private final InputComponent endDateInput;
    private final InputComponent discountPercentInput;

    private String categoryName;
    private LocalDate startDate;
    private LocalDate endDate;
    private float discountPercent;

    public AddCategoryDiscountPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        categoryNameInput = new InputComponent("Enter category name (שם קטגוריה): ");
        startDateInput = new InputComponent("Enter discount start date (תאריך התחלה): ");
        endDateInput = new InputComponent("Enter discount end date (תאריך סיום): ");
        discountPercentInput = new InputComponent("Enter discount percent (אחוז הנחה): ");

        categoryNameInput.subscribe(this::onCategoryNameInput);
        startDateInput.subscribe(this::onStartDateInput);
        endDateInput.subscribe(this::onEndDateInput);
        discountPercentInput.subscribe(this::onDiscountPercentInput);

        page.add(new LogoComponent("Add discount properties for the Category"));
        page.add(categoryNameInput);
        page.add(startDateInput);
        page.add(endDateInput);
        page.add(discountPercentInput);
    }

    private void onCategoryNameInput(StateEvent event) {
        try {
            String categoryName = event.getData();
            if (categoryName.isBlank())
                throw new IllegalArgumentException("Category name cannot be empty.");
            this.categoryName = categoryName;
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            categoryNameInput.render(out);
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
            addCategoryDiscount();
        } catch (NumberFormatException e) {
            out.println(e.getMessage());
            discountPercentInput.render(out);
        }
    }

    private void addCategoryDiscount() {

        try {

            CategoryDto categoryDto = restTemplate.getForObject(CATEGORY_ROUTE + "/" + categoryName, CategoryDto.class);
            if (categoryDto == null) {
                out.println("Category " + categoryName + " not found.");
                return;
            }
            DiscountDto discountDto = restTemplate.postForObject(
                    DISCOUNT_ROUTE,
                    new RequestDiscountDto(
                            startDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                            endDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                            discountPercent),
                    DiscountDto.class
            );
            restTemplate.put(CATEGORY_ROUTE + "/discount/" + categoryName, discountDto);

            out.println("Discount added to category " + categoryDto.categoryName() + " successfully.");

        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }
}
