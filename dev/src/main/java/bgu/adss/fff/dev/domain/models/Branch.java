package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Represents a branch that an employee can work at.
 */
@Entity(name="branch")
public class Branch {
    @Id
    private String name;

    public Branch(String name) {
        this.name = name;
    }

    public Branch() {

    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Branch) {
            Branch other = (Branch) obj;
            return this.name.equals(other.name);
        }
        return false;
    }
}
