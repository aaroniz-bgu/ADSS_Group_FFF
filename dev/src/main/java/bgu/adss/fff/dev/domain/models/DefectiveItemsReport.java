package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity(name="DefectiveItemsReport")
public class DefectiveItemsReport extends Report{

    public DefectiveItemsReport() {}

    public DefectiveItemsReport(long reportId, LocalDateTime reportDate, String title, String content){
        super(reportId, reportDate, title, content, ReportType.DEFECTIVE_ITEMS);
    }

    @Override
    public void writeReport() {

    }

}
