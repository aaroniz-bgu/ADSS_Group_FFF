package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import bgu.adss.fff.dev.util.CategoryUtil;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class GetCategoryPage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/category";
    private final RestTemplate restTemplate;

    private final InputComponent nameInput;

    private String name;

    public GetCategoryPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        nameInput = new InputComponent("Enter product name (שם): ");

        nameInput.subscribe(this::onNameInput);

        page.add(new LogoComponent("Get Category"));
        printAllCategoryNames();
        page.add(nameInput);
    }

    private void onNameInput(StateEvent event) {
        try {
            if (event.getData().isBlank())
                throw new IllegalArgumentException("Name cannot be empty.");
            this.name = event.getData();
            createCategory();
        } catch (Exception e) {
            out.println(e.getMessage());
            nameInput.render(out);
        }
    }

    private void printAllCategoryNames() {
        CategoryDto allCategories = restTemplate.getForObject(ROUTE + "/Super", CategoryDto.class);
        Objects.requireNonNull(allCategories);
        CategoryUtil.printCategoryNamesByLevel(out, allCategories);
    }

    private void createCategory() {
        try {
            CategoryDto response = restTemplate.getForObject(ROUTE + '/' + name, CategoryDto.class);
            Objects.requireNonNull(response);
            CategoryUtil.printCategoryNamesByTree(out, response);
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }
}
