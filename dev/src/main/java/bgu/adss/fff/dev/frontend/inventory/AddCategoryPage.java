package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.contracts.RequestCategoryDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.util.Objects;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class AddCategoryPage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/category";
    private final RestTemplate restTemplate;

    private final InputComponent nameInput;
    private final InputComponent parentCategoryInput;

    private String name;
    private String parentCategory;

    public AddCategoryPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        nameInput = new InputComponent("Enter product name (שם): ");
        parentCategoryInput = new InputComponent(
                "Enter parent category (קטגורית אב) - (Enter 'Super' for no parent category): ");

        nameInput.subscribe(this::onNameInput);
        parentCategoryInput.subscribe(this::onParentCategoryInput);

        page.add(new LogoComponent("Create New Category"));
        page.add(nameInput);
        page.add(parentCategoryInput);
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

    private void onParentCategoryInput(StateEvent event) {
        try {
            if (event.getData().isBlank())
                throw new IllegalArgumentException("Parent category cannot be empty.");
            this.parentCategory = event.getData();
        } catch (Exception e) {
            out.println(e.getMessage());
            parentCategoryInput.render(out);
        } finally {
            createCategory();
        }
    }

    private void createCategory() {
        try {
            RequestCategoryDto category = new RequestCategoryDto(name);
            CategoryDto response = restTemplate.postForObject(
                    ROUTE + "/" + parentCategory, category, CategoryDto.class);
            Objects.requireNonNull(response);

            out.println("Category Created - " + response.categoryName());
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }
}
