package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import bgu.adss.fff.dev.util.CategoryUtil;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.util.Objects;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class SeeCategoryProductsPage extends AbstractUserComponent {
    private static final String ROUTE = URI_PATH + "/category";
    private final RestTemplate restTemplate;

    private final InputComponent nameInput;

    private String name;

    public SeeCategoryProductsPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        nameInput = new InputComponent("Enter category name (שם): ");

        nameInput.subscribe(this::onNameInput);

        page.add(new LogoComponent("Get Category"));
        page.add(nameInput);
    }

    private void onNameInput(StateEvent event) {
        try {
            if (event.getData().isBlank())
                throw new IllegalArgumentException("Name cannot be empty.");
            this.name = event.getData();
            printAllCategoryProducts();
        } catch (Exception e) {
            out.println(e.getMessage());
            nameInput.render(out);
        }
    }

    private void printAllCategoryProducts() {
        try {
            CategoryDto category = restTemplate.getForObject(ROUTE + "/" + name, CategoryDto.class);
            Objects.requireNonNull(category);
            CategoryUtil.printProductsByCategoryTree(out, category);
        } catch (Exception e) { out.println(e.getMessage()); }
    }
}
