package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    /**
     * Discount constructor
     * @param discountID discount id
     * @param startDate discount start date
     * @param endDate discount end date
     * @param discountPercent discount percent
     */
    public Discount(long discountID, LocalDate startDate, LocalDate endDate, float discountPercent) {
        this.discountID = discountID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discountPercent = discountPercent;
    }

    public Discount() { }

    /**
     * Get discount id
     * @return discount id
     */
    public long getDiscountID() {
        return discountID;
    }

    /**
     * Set discount id
     * @param discountID discount id
     */
    public void setDiscountID(long discountID) {
        this.discountID = discountID;
    }

    /**
     * Get discount start date
     * @return discount start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Get discount end date
     * @return discount end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Get discount percent
     * @return discount percent
     */
    public float getDiscountPercent() {
        return discountPercent;
    }

    public boolean isValid(){
        return LocalDateTime.now().isAfter(startDate.atStartOfDay()) &&
                LocalDateTime.now().isBefore(endDate.plusDays(1).atStartOfDay());
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
