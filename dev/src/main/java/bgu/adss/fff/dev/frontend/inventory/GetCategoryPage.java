package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.contracts.ProductDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

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

        out.println("Available categories: ");
        for (int i = 1; i <= 3; i++)
            out.println("\t Level " + i + ": " + getCategoryNames(i).length + " categories");

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

    private String[] getCategoryNames(int level) {
        CategoryDto allCategories = restTemplate.getForObject(ROUTE + "/Super", CategoryDto.class);
        Objects.requireNonNull(allCategories);
        return getCategoryNamesRecursive(allCategories, level);
    }

    private String[] getCategoryNamesRecursive(CategoryDto category, int levelAccepted) {
        if (category.level() == levelAccepted)
            return new String[]{category.categoryName()};
        return Arrays.stream(category.children())
                .flatMap(subCategory -> Arrays.stream(getCategoryNamesRecursive(subCategory, levelAccepted)))
                .toArray(String[]::new);
    }

    private void createCategory() {
        try {
            CategoryDto response = restTemplate.getForObject(ROUTE + '/' + name, CategoryDto.class);
            Objects.requireNonNull(response);

            printCategory(response, 0);
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }

    private void printCategory(CategoryDto category, int depth) {
        for (int i = 0; i < depth; i++)
            System.out.print("\t");
        out.println("-" + category.categoryName());
        for (CategoryDto subCategory : category.children())
            printCategory(subCategory, depth + 1);
    }
}
