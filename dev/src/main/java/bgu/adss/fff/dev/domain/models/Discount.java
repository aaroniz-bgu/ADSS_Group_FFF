package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.time.LocalDate;

public class Discount implements Serializable {

    @Id
    private final long id;

    @Column
    private LocalDate startDate;

    @Column
    private int numOfDays;

    @Column
    private float discountPercent;

    @Column
    private boolean isValid;

    public Discount(long id, LocalDate startDate, int numOfDays, float discountPercent) {
        this.id = id;
        this.startDate = startDate;
        this.numOfDays = numOfDays;
        this.discountPercent = discountPercent;
        this.isValid = true;
    }

    public long getId() {
        return id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getNumOfDays() {
        return numOfDays;
    }

    public void setNumOfDays(int numOfDays) {
        this.numOfDays = numOfDays;
    }

    public float getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(float discountPercent) {
        this.discountPercent = discountPercent;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    @Override
    public String toString() {
        return "Discount{" +
                "startDate=" + startDate +
                ", numOfDays=" + numOfDays +
                ", discountPercent=" + discountPercent +
                ", isValid=" + isValid +
                '}';
    }
}
