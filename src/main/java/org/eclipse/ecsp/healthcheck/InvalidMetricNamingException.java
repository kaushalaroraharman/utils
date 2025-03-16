package org.eclipse.ecsp.healthcheck;

/**
 * Custom exception for duplicate metric name.
 *
 * <p>This exception is thrown when there is an attempt to register two metrics with the same name.
 * It extends the {@link RuntimeException} class, allowing it to be used as an unchecked exception.</p>
 *
 * @since 1.0
 * @version 1.0
 *
 * @see java.lang.RuntimeException
 */
public class InvalidMetricNamingException extends RuntimeException {

    /**
     * The serial version UID for serialization.
     */
    private static final long serialVersionUID = 77898657865L;

    /**
     * Constructs a new InvalidMetricNamingException with the specified detail message.
     *
     * @param s the detail message
     */
    public InvalidMetricNamingException(String s) {
        super(s);
    }
}