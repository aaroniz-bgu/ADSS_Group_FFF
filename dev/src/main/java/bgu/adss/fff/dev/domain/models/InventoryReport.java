package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Entity(name="InventoryReport")
public class InventoryReport extends Report {

    @ElementCollection
    private List<Category> categories;
    public InventoryReport() {}

    public InventoryReport(long reportId, LocalDateTime reportDate, String title, String content, List<Category> categories) {
        super(reportId, reportDate, title, content);
        this.categories = categories;
    }

}
