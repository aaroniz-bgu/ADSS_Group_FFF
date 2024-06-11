package bgu.adss.fff.dev.frontend;

import bgu.adss.fff.dev.contracts.CategoryDto;
import bgu.adss.fff.dev.contracts.RequestCategoriesDto;
import bgu.adss.fff.dev.contracts.RequestCategoryDto;
import bgu.adss.fff.dev.contracts.RequestProductDto;
import bgu.adss.fff.dev.frontend.cli.uikit.AbstractUserComponent;
import bgu.adss.fff.dev.frontend.cli.uikit.TerminalApp;
import bgu.adss.fff.dev.frontend.inventory.CreateStockReportPage;
import bgu.adss.fff.dev.frontend.inventory.InventoryMenuPage;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class FrontendApp extends TerminalApp {
    public static final String URI_PATH = "http://localhost:8080";

    public FrontendApp() {
        before();
        AbstractUserComponent page = new InventoryMenuPage(System.out);
        while(!Thread.currentThread().isInterrupted()) {
            page.render();
        }
        this.close();
    }

    private void before() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            restTemplate.postForLocation(URI_PATH + "/category/Super", new RequestCategoryDto("Food"));
            restTemplate.postForLocation(URI_PATH + "/category/Super", new RequestCategoryDto("Drinks"));

            restTemplate.postForLocation(URI_PATH + "/category/Food", new RequestCategoryDto("Fruits"));
            restTemplate.postForLocation(URI_PATH + "/category/Food", new RequestCategoryDto("Vegetables"));

            restTemplate.postForLocation(URI_PATH + "/category/Drinks", new RequestCategoryDto("Alcoholic"));
            restTemplate.postForLocation(URI_PATH + "/category/Drinks", new RequestCategoryDto("Non-Alcoholic"));

            restTemplate.postForLocation(URI_PATH + "/category/Fruits", new RequestCategoryDto("Apple"));
            restTemplate.postForLocation(URI_PATH + "/category/Fruits", new RequestCategoryDto("Banana"));
            restTemplate.postForLocation(URI_PATH + "/category/Vegetables", new RequestCategoryDto("Carrot"));
            restTemplate.postForLocation(URI_PATH + "/category/Vegetables", new RequestCategoryDto("Tomato"));

            restTemplate.postForLocation(URI_PATH + "/category/Alcoholic", new RequestCategoryDto("Beer"));
            restTemplate.postForLocation(URI_PATH + "/category/Alcoholic", new RequestCategoryDto("Wine"));
            restTemplate.postForLocation(URI_PATH + "/category/Non-Alcoholic", new RequestCategoryDto("Water"));
            restTemplate.postForLocation(URI_PATH + "/category/Non-Alcoholic", new RequestCategoryDto("Soda"));

            restTemplate.postForLocation(URI_PATH + "/product", new RequestProductDto(1, "Red Apples", 5.0f, 20));
            restTemplate.put(URI_PATH + "/category/product/" + 1, new RequestCategoriesDto(new String[] {"Food", "Fruits", "Apple"}));
            restTemplate.postForLocation(URI_PATH + "/product", new RequestProductDto(2, "Small Bananas", 3.0f, 20));
            restTemplate.put(URI_PATH + "/category/product/" + 2, new RequestCategoriesDto(new String[] {"Food", "Fruits", "Banana"}));
            restTemplate.postForLocation(URI_PATH + "/product", new RequestProductDto(3, "Gezer Gamadi", 2.0f, 10));
            restTemplate.put(URI_PATH + "/category/product/" + 3, new RequestCategoriesDto(new String[] {"Food", "Vegetables", "Carrot"}));
            restTemplate.postForLocation(URI_PATH + "/product", new RequestProductDto(4, "Red Tomatoes", 4.0f, 15));
            restTemplate.put(URI_PATH + "/category/product/" + 4, new RequestCategoriesDto(new String[] {"Food", "Vegetables", "Tomato"}));

            restTemplate.postForLocation(URI_PATH + "/product", new RequestProductDto(5, "Goldstar", 10.0f, 100));
            restTemplate.put(URI_PATH + "/category/product/" + 5, new RequestCategoriesDto(new String[] {"Drinks", "Alcoholic", "Beer"}));
            restTemplate.postForLocation(URI_PATH + "/product", new RequestProductDto(6, "Cabernet Sauvignon", 50.0f, 50));
            restTemplate.put(URI_PATH + "/category/product/" + 6, new RequestCategoriesDto(new String[] {"Drinks", "Alcoholic", "Wine"}));
            restTemplate.postForLocation(URI_PATH + "/product", new RequestProductDto(7, "Ein Gedi", 5.0f, 200));
            restTemplate.put(URI_PATH + "/category/product/" + 7, new RequestCategoriesDto(new String[] {"Drinks", "Non-Alcoholic", "Water"}));
            restTemplate.postForLocation(URI_PATH + "/product", new RequestProductDto(8, "Coca Cola", 3.0f, 150));
            restTemplate.put(URI_PATH + "/category/product/" + 8, new RequestCategoriesDto(new String[] {"Drinks", "Non-Alcoholic", "Soda"}));

            // If it is a monday or a thursday, generate and show the report
             if (LocalDate.now().getDayOfWeek() == DayOfWeek.MONDAY || LocalDate.now().getDayOfWeek() == DayOfWeek.THURSDAY) {
                 new CreateStockReportPage(System.out).render();
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}