package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private Branch source;
    @ManyToOne
    private Employee driver;
    @Column
    private LocalDateTime startTime;
    @Column
    private long truckNumber;
    @Column
    private String license;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "delivery_destinations",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = @JoinColumn(name = "name")
    )
    private List<Branch> destinations;

    public Delivery() { }
    public Delivery(
            Branch source, Employee driver, LocalDateTime startTime,
            long truckNumber, String license, List<Branch> destinations) {
        this.source = source;
        this.driver = driver;
        this.startTime = startTime;
        this.truckNumber = truckNumber;
        this.license = license;
        this.destinations = destinations;
    }

    public long getId() {
        return id;
    }

    public Branch getSource() {
        return source;
    }

    public void setSource(Branch source) {
        this.source = source;
    }

    public Employee getDriver() {
        return driver;
    }

    public void setDriver(Employee driver) {
        this.driver = driver;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTruckNumber() {
        return truckNumber;
    }

    public void setTruckNumber(long truckNumber) {
        this.truckNumber = truckNumber;
    }

    public List<Branch> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Branch> destinations) {
        this.destinations = destinations;
    }
}
