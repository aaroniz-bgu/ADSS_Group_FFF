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

    public Report(){ }


}
