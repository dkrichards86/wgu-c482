package InventoryApp.models;

import InventoryApp.exceptions.ValidationException;

/**
 * Part represents a part in the inventory which can be used in conjunction with
 * other parts to form a product.
 * <p>
 * This abstract class is intended to be extended.
 * 
 * @see InhousePart, OutsourcePart
 * @author Dale Richards <dric123@wgu.edu>
 */
public abstract class Part {
    
    // Internal part ID
    private int partID;
    
    // Common name of the part
    private String name;
    
    // Price per unit of the part
    private double price;
    
    // Current inventory
    private int inStock;
    
    // Minimum required inventory
    private int min;
    
    // Maximum required inventory
    private int max;
    
    /**
     * Constructor
     */
    public Part() {
    }
    
    /**
     * Get current inventory
     * 
     * @return current inventory
     */
    public int getInStock() {
        return inStock;
    }
    
    /**
     * Get the max required inventory
     * 
     * @return max required inventory
     */
    public int getMax() {
        return max;
    }
    
    /**
     * Get the min required inventory
     * 
     * @return min required inventory
     */
    public int getMin() {
        return min;
    }
    
    /**
     * Get the part's common name
     * 
     * @return this part's common name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get the internal part ID
     * 
     * @return internal part ID
     */
    public int getPartID() {
        return partID;
    }
    
    /**
     * Get the current price per unit
     * 
     * @return price per unit
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Ensure a part is valid. Throw a custom exception as required.
     * 
     * @return
     * @throws ValidationException 
     */
    public boolean isValid() throws ValidationException {
        // Name is required
        if (getName().equals("")) {
            throw new ValidationException("The name field cannot be empty.");
        }
        
        // inventory must be positive
        if (getInStock() < 0) {
            throw new ValidationException("The current inventory must be greater than 0.");
        }
        
        // part price must be positive
        if (getPrice() < 0) {
            throw new ValidationException("The price must be greater than $0");
        }
        
        // the minimum must be positive
        if (getMin() < 0) {
            throw new ValidationException("The minimum inventory must be greater than 0.");
        }
        
        // the maximum must be greater than the minimum
        if (getMin() > getMax()) {
            throw new ValidationException("The minimum inventory must be less than the maximum.");
        }
        
        // the in stock inventory must be between min and max
        if (getInStock() < getMin() || getInStock() > getMax()) {
            throw new ValidationException("The current inventory must be between the minimum and maximum inventory.");
        }
        
        return true;
    }
    
    /**
     * Set a current inventory
     * 
     * @param inStock 
     */
    public void setInStock(int inStock) {
        this.inStock = inStock;
    }
    
    /**
     * Set a maximum required inventory
     * 
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }
    
    /**
     * Set a minimum required inventory
     * 
     * @param min 
     */
    public void setMin(int min) {
        this.min = min;
    }
    
    /**
     * Set the part's common name
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Set the internal part ID
     * 
     * @param partID 
     */
    public void setPartID(int partID) {
        this.partID = partID;
    }
    
    /**
     * Set the price per unit
     * 
     * @param price 
     */
    public void setPrice(double price) {
        this.price = price;
    }
}
