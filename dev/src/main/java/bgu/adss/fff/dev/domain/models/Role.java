package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

/**
 * Represents a role within the organization.
 */
@Entity(name="role")
public class Role {
    @Id
    private String name;
    private boolean isShiftManager;

    public Role(String name, boolean isShiftManger) {
        this.name = name;
        this.isShiftManager = isShiftManger;
    }

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
     * Sets whether the role is for a shift manager.
     *
     * @param shiftManager True if the role is for a shift manager, false otherwise.
     */
    public void setShiftManager(boolean shiftManager) {
        isShiftManager = shiftManager;
    }
}
