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

    /**
     * Write a report
     * @param repository product repository
     */
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

    /**
     * InventoryReport constructor
     * @param reportId report id
     * @param reportDate report date
     * @param title report title
     * @param content report content
     * @param categories report categories
     */
    public InventoryReport(long reportId, LocalDateTime reportDate, String title,
                           String content, List<Category> categories, Branch branch) {
        super(reportId, reportDate, title, content, ReportType.INVENTORY, branch);
        this.categories = categories;
    }

    /**
     * InventoryReport constructor
     * @param reportId report id
     * @param reportDate report date
     * @param title report title
     * @param content report content
     */
    public InventoryReport(long reportId, LocalDateTime reportDate, String title, String content, Branch branch) {
        super(reportId, reportDate, title, content, ReportType.INVENTORY, branch);
        this.categories = new LinkedList<>();
    }

    /**
     * Get categories
     * @return list of categories
     */
    public List<Category> getCategories() {
        return categories;
    }

    /**
     * Set categories
     * @param categories list of categories
     */
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

}
