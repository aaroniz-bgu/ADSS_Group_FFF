package bgu.adss.fff.dev.domain.models;

import bgu.adss.fff.dev.data.ProductRepository;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;


@Entity(name="OutOfStockReport")
public class OutOfStockReport extends Report {

    public OutOfStockReport() {}

    public OutOfStockReport(long reportId, LocalDateTime reportDate, String title, String content) {
        super(reportId, reportDate, title, content, ReportType.OUT_OF_STOCK);
    }

    @Override
    public void writeReport(ProductRepository repository) {
        if (repository == null)
            throw new NullPointerException("Product repository is null");

        // write report

    }

}
