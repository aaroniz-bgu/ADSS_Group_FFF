package bgu.adss.fff.dev.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ConfigurationPair {
    @Id
    private String key;
    @Column
    private String value;

    public ConfigurationPair() {

    }

    public ConfigurationPair(String cutOff, String fallbackCutoff) {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
