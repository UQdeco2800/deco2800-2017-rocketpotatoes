package com.deco2800.potatoes.exceptions;

/**
 * An exception that is thrown to indicate an invalid quantity of resource.
 */
@SuppressWarnings("serial")
public class InvalidResourceException extends RuntimeException {

    /**
     * Constructs a new exception with null as its detail message.
     */
    public InvalidResourceException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message
     *            the detail error message
     */
    public InvalidResourceException(String message) {
        super(message);
    }

}