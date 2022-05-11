package cz.cvut.fel.ear.library.model;

public enum UserType {
    LIBRARIAN("ROLE_LIBRARIAN"),
    GUEST("ROLE_GUEST");

    private final String username;

    UserType(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username;
    }

    public String getUsername() {
        return username;
    }
}
