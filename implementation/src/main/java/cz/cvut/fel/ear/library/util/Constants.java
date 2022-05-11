package cz.cvut.fel.ear.library.util;
import cz.cvut.fel.ear.library.model.UserType;

public final class Constants {

    /**
     * Default user role.
     */
    public static final UserType DEFAULT_ROLE = UserType.GUEST;
    public static final int DAYS_OF_RESERVATION = 5;
    public static final int DAYS_OF_BOOKLOAN = 30;

    private Constants() {
        throw new AssertionError();
    }
}

