package cz.cvut.fel.ear.library.exception;


public class LibrarySystemException extends RuntimeException {

    public LibrarySystemException() {
    }

    public LibrarySystemException(String message) {
        super(message);
    }

    public LibrarySystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public LibrarySystemException(Throwable cause) {
        super(cause);
    }
}

