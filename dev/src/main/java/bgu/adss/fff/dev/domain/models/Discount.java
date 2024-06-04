package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDate;

@Entity(name="Discount")
public class Discount implements Serializable {

    @Id
    private long discountID;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    private float discountPercent;

    public Discount(long discountID, LocalDate startDate, LocalDate endDate, float discountPercent) {
        this.discountID = discountID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPercent = discountPercent;
    }

    public Discount() { }

    public long getDiscountID() {
        return discountID;
    }

    public void setDiscountID(long discountID) {
        this.discountID = discountID;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(float discountPercent) {
        this.discountPercent = discountPercent;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                ", discountPercent=" + discountPercent +
                '}';
    }
}
