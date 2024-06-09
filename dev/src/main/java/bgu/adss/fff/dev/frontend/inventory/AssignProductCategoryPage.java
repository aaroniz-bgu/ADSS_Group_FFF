package bgu.adss.fff.dev.frontend.inventory;

import bgu.adss.fff.dev.contracts.RequestCategoriesDto;
import bgu.adss.fff.dev.frontend.cli.components.InputComponent;
import bgu.adss.fff.dev.frontend.cli.components.StateEvent;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import org.springframework.web.client.RestTemplate;

import java.io.PrintStream;

import static bgu.adss.fff.dev.frontend.FrontendApp.URI_PATH;

public class AssignProductCategoryPage extends AbstractUserComponent {

    private static final String ROUTE = URI_PATH + "/category";
    private final RestTemplate restTemplate;

    private final InputComponent idInput;
    private final InputComponent[] categoriesInput;

    private long id;
    private final String[] categories;

    public AssignProductCategoryPage(PrintStream out) {
        super(out);

        restTemplate = new RestTemplate();

        idInput = new InputComponent("Enter product id (מקט): ");
        categoriesInput = new InputComponent[] {
                new InputComponent("Enter main category (קטגוריה ראשית): "),
                new InputComponent("Enter sub category (קטגוריה משנית): "),
                new InputComponent("Enter size category (קטגוריית גודל): ")
        };

        categories = new String[categoriesInput.length];

        idInput.subscribe(this::onIdInput);
        for (int i = 0; i < categoriesInput.length; i++) {
            int finalI = i;
            categoriesInput[i].subscribe((StateEvent event) -> onCategoryInput(event, finalI));
        }

        page.add(new LogoComponent("Assign Product to Category"));
        page.add(idInput);
        for (InputComponent input : categoriesInput)
            page.add(input);
    }

    private void onIdInput(StateEvent event) {
        try {
            this.id = Long.parseLong(event.getData());
        } catch (Exception e) {
            out.println("Invalid id.");
            idInput.render(out);
        }
    }

    private void onCategoryInput(StateEvent event, int i) {
        try {
            if (event.getData().isBlank())
                throw new IllegalArgumentException("Category cannot be empty.");
            this.categories[i] = event.getData();
            if (i == categoriesInput.length - 1)
                assignProductToCategory();
        } catch (Exception e) {
            out.println(e.getMessage());
            categoriesInput[i].render(out);
        }
    }

    private void assignProductToCategory() {
        try {
            RequestCategoriesDto categoriesDto = new RequestCategoriesDto(categories);
            restTemplate.put(ROUTE + "/product/" + id, categoriesDto);
            out.println("Assigned Category " + String.join("/", categories) + " to Product " + id);
        } catch (Exception e) {
            out.println(e.getMessage());
        }
    }
}
