package bgu.adss.fff.dev.domain.models;

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
    public InventoryReport() {}

    @Override
    public void writeReport() {

    }

    public InventoryReport(long reportId, LocalDateTime reportDate, String title, String content, List<Category> categories) {
        super(reportId, reportDate, title, content, ReportType.INVENTORY);
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }

}
