package cz.cvut.fel.ear.library.rest.handler;

        import cz.cvut.fel.ear.library.exception.NotFoundException;
        import org.slf4j.Logger;
        import org.slf4j.LoggerFactory;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.web.bind.annotation.ControllerAdvice;
        import org.springframework.web.bind.annotation.ExceptionHandler;

        import javax.persistence.PersistenceException;
        import javax.servlet.http.HttpServletRequest;

/**
 * The type Rest exception handler.
 */
@ControllerAdvice
public class RestExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

    private static void logException(RuntimeException ex) {
        LOG.error("Exception caught: ", ex);
    }

    private static ErrorInfo errorInfo(HttpServletRequest request, Throwable e) {
        return new ErrorInfo(e.getMessage(), request.getRequestURI());
    }

    /**
     * Persistence exception response entity.
     *
     * @param request the request
     * @param e       the e
     * @return the response entity
     */
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ErrorInfo> persistenceException(HttpServletRequest request, PersistenceException e) {
        logException(e);

        return new ResponseEntity<>(errorInfo(request, e.getCause()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorInfo> notFoundException(HttpServletRequest request, NotFoundException e) {
        logException(e);

        return new ResponseEntity<>(errorInfo(request, e), HttpStatus.NOT_FOUND);
    }
}
