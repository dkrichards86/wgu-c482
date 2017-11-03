package InventoryApp.exceptions;

/**
 * Custom validation exception. This is thrown when a Part or Product does not
 * meet validation requirements prior to creation or modification.
 * 
 * @see Part, Product
 * @author Dale Richards <dric123@wgu.edu>
 */
public class ValidationException extends Exception {
    /**
     * Constructor. Simply passes the exception message to the base handler.
     * 
     * @param message 
     */
    public ValidationException(String message) {
        super(message);
    }
}
