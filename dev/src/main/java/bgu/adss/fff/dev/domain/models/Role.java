package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a role within the organization.
 */
@Entity(name="role")
public class Role implements Serializable {
    @Id
    private String name;
    private boolean isShiftManager;
    private boolean isHrManager;

    public Role(String name, boolean isShiftManger, boolean isHrManager) {
        this.name = name;
        this.isShiftManager = isShiftManger;
        this.isHrManager = isHrManager;
    }

    public Role() { }

    /**
     * Gets the name of the role.
     *
     * @return The name of the role.
     */
    public String getName() {
        return name;
    }

    /**
     * Checks if the role is for a shift manager.
     *
     * @return True if the role is for a shift manager, false otherwise.
     */
    public boolean isShiftManager() {
        return isShiftManager;
    }

    /**
     * Checks if the role is for an HR manager.
     *
     * @return True if the role is for an HR manager, false otherwise.
     */
    public boolean isHrManager() {
        return isHrManager;
    }

    /**
     * Sets whether the role is for a shift manager.
     *
     * @param shiftManager True if the role is for a shift manager, false otherwise.
     */
    public void setShiftManager(boolean shiftManager) {
        isShiftManager = shiftManager;
    }

    /**
     * Sets whether the role is for an HR manager.
     *
     * @param hrManager True if the role is for an HR manager, false otherwise.
     */
    public void setHrManager(boolean hrManager) {
        isHrManager = hrManager;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Role) {
            Role other = (Role) obj;
            return this.name.equals(other.name) && this.isShiftManager == other.isShiftManager && this.isHrManager == other.isHrManager;
        }
        return false;
    }
}
