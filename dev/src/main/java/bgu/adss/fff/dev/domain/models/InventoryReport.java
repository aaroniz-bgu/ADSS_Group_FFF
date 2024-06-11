package bgu.adss.fff.dev.domain.models;

import bgu.adss.fff.dev.data.ProductRepository;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity(name="InventoryReport")
public class InventoryReport extends Report {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "inventoryReport_category",
            joinColumns = @JoinColumn(name = "reportId"),
            inverseJoinColumns = @JoinColumn(name = "categoryID")
    )
    private List<Category> categories;

    public InventoryReport() { }

    @Override
    public void writeReport(ProductRepository repository) {
        if (repository == null)
            throw new NullPointerException("Product repository is null");
        if (categories == null)
            throw new NullPointerException("Categories list is null");
        if (categories.isEmpty())
            throw new IllegalArgumentException("Categories list is empty");

        // write report
        StringBuilder content = new StringBuilder();

        for (Category category : categories) {
            content.append("\n").append(category.getCategoryName()).append("\n");
            for (Product product : category.getProducts()) {
                String productRow = "\t" + product.getProductName() + " - " + product.getQuantity() + " items\n";
                content.append(productRow);
            }
        }

        setContent(content.toString());
    }

    public InventoryReport(
            long reportId, LocalDateTime reportDate, String title, String content, List<Category> categories) {
        super(reportId, reportDate, title, content, ReportType.INVENTORY);
        this.categories = categories;
    }

    public InventoryReport(long reportId, LocalDateTime reportDate, String title, String content) {
        super(reportId, reportDate, title, content, ReportType.INVENTORY);
        this.categories = new LinkedList<>();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

}
