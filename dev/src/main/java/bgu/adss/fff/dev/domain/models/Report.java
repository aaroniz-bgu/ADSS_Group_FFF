package bgu.adss.fff.dev.domain.models;

import bgu.adss.fff.dev.data.ProductRepository;
import jakarta.persistence.*;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch")
    private Branch branch;


    public Report(){}

    /**
     * Report constructor
     * @param reportId report id
     * @param reportDate report date
     * @param title report title
     * @param content report content
     * @param reportType report type
     * @param branch report branch
     */
    public Report(long reportId, LocalDateTime reportDate, String title, String content, ReportType reportType, Branch branch) {
        this.reportId = reportId;
        this.reportDate = reportDate;
        this.title = title;
        this.content = content;
        this.reportType = reportType;
        this.branch = branch;
    }

    /**
     * Write a report
     * @param repository product repository
     */
    public abstract void writeReport(ProductRepository repository);

    /**
     * Get report id
     * @return report id
     */
    public long getReportId() {
        return reportId;
    }

    /**
     * Set report id
     * @param reportId report id
     */
    public void setReportId(long reportId) {
        this.reportId = reportId;
    }

    /**
     * Get report date
     * @return report date
     */
    public LocalDateTime getReportDate() {
        return reportDate;
    }

    /**
     * Get report title
     * @return report title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get report content
     * @return report content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set report content
     * @param content report content
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get report type
     * @return report type
     */
    public ReportType getReportType() {
        return reportType;
    }

    /**
     * Get report branch
     * @return report branch
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * Set report branch
     * @param branch report branch
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

}
