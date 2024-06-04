package bgu.adss.fff.dev.domain.models;

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

    @Column
    private String content;

    public Report(){}

    public Report(long reportId, LocalDateTime reportDate, String title, String content){
        this.reportId = reportId;
        this.reportDate = reportDate;
        this.title = title;
        this.content = content;
    }

    public abstract void writeReport();

    public long getReportId() {
        return reportId;
    }

    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    public LocalDateTime getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDateTime reportDate) {
        this.reportDate = reportDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
