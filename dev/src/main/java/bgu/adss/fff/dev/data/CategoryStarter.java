package bgu.adss.fff.dev.data;

import bgu.adss.fff.dev.domain.models.Category;
import bgu.adss.fff.dev.domain.models.Role;
import bgu.adss.fff.dev.services.CategoryService;
import bgu.adss.fff.dev.services.RoleService;

import java.util.List;
import java.util.Objects;

public class CategoryStarter {

    private static final class Pair {
        final String x, y;
        public Pair(String x, String y) {
            this.x = x;
            this.y = y;
        }
        public String getX() { return x; }
        public String getY() { return y; }
    }
    private final Pair[] categories = {
            new Pair(null, "Super"),
            new Pair("Super", "Foods"),
            new Pair("Super", "Drinks"),
            new Pair("Foods", "Fruits"),
            new Pair("Foods", "Vegetables"),
            new Pair("Drinks", "Alcoholic"),
            new Pair("Drinks", "Non-Alcoholic"),
            new Pair("Fruits","Apple"),
            new Pair("Fruits","Banana"),
            new Pair("Vegetables", "Carrot"),
            new Pair("Vegetables", "Tomato"),
            new Pair("Alcoholic", "Beer"),
            new Pair("Alcoholic", "Wine"),
            new Pair("Non-Alcoholic", "Water"),
            new Pair("Non-Alcoholic", "Soda")
    };

    private final CategoryService service;

    public CategoryStarter(CategoryService service) {
        this.service = service;
    }

    public void loadCategories() {
        for (Pair p : categories) {
            if (p.getX() == null && Objects.equals(p.getY(), "Super")) {
                Category c = new Category("Super", 0, null, null);
                service.createCategory(c, null);
                continue;
            }
            Category c = new Category(p.getY(), 0, null, null);
            service.createCategory(c, p.getX());
        }
    }
}
