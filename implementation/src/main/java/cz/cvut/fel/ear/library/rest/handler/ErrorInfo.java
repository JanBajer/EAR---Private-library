package cz.cvut.fel.ear.library.rest.handler;

import java.util.Objects;


public class ErrorInfo {
    private String message;
    private String requestURL;

    /**
     * Instantiates a new Error info.
     */
    public ErrorInfo() {
    }

    /**
     * Instantiates a new Error info.
     *
     * @param message    the message
     * @param requestURL the request url
     */
    public ErrorInfo(String message, String requestURL) {
        this.message = message;
        this.requestURL = requestURL;
    }

    /**
     * Gets message.
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets message.
     *
     * @param message the message
     */
    public void setMessage(String message) {
        Objects.requireNonNull(message);

        this.message = message;
    }

    /**
     * Gets request url.
     *
     * @return the request url
     */
    public String getRequestURL() {
        return requestURL;
    }

    /**
     * Sets request url.
     *
     * @param requestURL the request url
     */
    public void setRequestURL(String requestURL) {
        Objects.requireNonNull(requestURL);

        this.requestURL = requestURL;
    }
}