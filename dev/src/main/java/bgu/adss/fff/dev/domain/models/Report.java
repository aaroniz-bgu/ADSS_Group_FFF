package bgu.adss.fff.dev.domain.models;

import bgu.adss.fff.dev.data.ProductRepository;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity(name="Report")
public abstract class Report implements Serializable {

    @Id
    private long reportId;

    @Column
    private LocalDateTime reportDate;

    @Column
    private String title;

    @Column(length = 1024)
    private String content;

    @Column
    private ReportType reportType;

    public Report(){}

    public Report(long reportId, LocalDateTime reportDate, String title, String content, ReportType reportType) {
        this.reportId = reportId;
        this.reportDate = reportDate;
        this.title = title;
        this.content = content;
        this.reportType = reportType;
    }

    public abstract void writeReport(ProductRepository repository);

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public String getTitle() {
        return title;
    }

//    public void setTitle(String title) {
//        this.title = title;
//    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReportType getReportType() {
        return reportType;
    }

}
