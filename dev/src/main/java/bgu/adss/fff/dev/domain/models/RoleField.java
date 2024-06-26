package bgu.adss.fff.dev.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

@Entity
public class RoleField {

    @Id
    private RoleFieldKey id;
    @Column
    private String value;

    public RoleField() { }

    public RoleField(Employee employee, Role role, String field, String value) {
        this.id = new RoleFieldKey(employee, role, field);
        this.value = value;
    }

    public RoleField(RoleFieldKey id, String value) {
        this.id = id;
        this.value = value;
    }


    public Employee getEmployee() {
        return id.employee;
    }

    public Role getRole() {
        return id.role;
    }

    public String getField() {
        return id.field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Embeddable
    public static class RoleFieldKey implements Serializable {

        @ManyToOne
        private Employee employee;
        @ManyToOne
        private Role role;
        @Column
        private String field;

        public RoleFieldKey() { }

        public RoleFieldKey(Employee employee, Role role, String field) {
            this.employee = employee;
            this.role = role;
            this.field = field;
        }
    }
}
