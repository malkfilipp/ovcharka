package ovcharka.userservice.domain;

public enum Role {
    USER("USER"),
    ADMIN("ADMIN");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}
