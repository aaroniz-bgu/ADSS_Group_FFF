package bgu.adss.fff.dev.domain.models;

public enum CustomField {
    LICENSE("license");

    private final String name;
    CustomField(String name) { this.name = name; }

    public String toString() {
        return name;
    }
}
