package cz.cvut.fel.ear.library.exception;

public class BookNotAvailable extends LibrarySystemException{
    public BookNotAvailable(String message) {
        super(message);
    }
}
