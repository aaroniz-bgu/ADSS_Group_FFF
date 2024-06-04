package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;


@Entity(name="OutOfStockReport")
public class OutOfStockReport extends Report {

    public OutOfStockReport() {}

    public OutOfStockReport(long reportId, LocalDateTime reportDate, String title, String content){
        super(reportId, reportDate, title, content);
    }

    @Override
    public void writeReport() {

    }

}
