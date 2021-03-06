package com.deco2800.potatoes.exceptions;

/**
 * An exception that is thrown to indicate an invalid quantity of resource.
 */
@SuppressWarnings("serial")
public class InvalidInventoryException extends RuntimeException {

    /**
     * Constructs a new exception with null as its detail message.
     */
    public InvalidInventoryException() {
        super();
    }

    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message
     *            the detail error message
     */
    public InvalidInventoryException(String message) {
        super(message);
    }

}