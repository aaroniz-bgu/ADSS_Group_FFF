package bgu.adss.fff.dev.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class ConfigurationPair {
    @Id
    private String ckey;
    @Column
    private String cvalue;

    public ConfigurationPair() {

    }

    public ConfigurationPair(String ckey, String cvalue) {
        this.ckey = ckey;
        this.cvalue = cvalue;
    }

    public String getKey() {
        return ckey;
    }

    public void setKey(String key) {
        this.ckey = key;
    }

    public String getValue() {
        return cvalue;
    }

    public void setValue(String value) {
        this.cvalue = value;
    }
}
