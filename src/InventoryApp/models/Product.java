package InventoryApp.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import InventoryApp.exceptions.ValidationException;

/**
 * Product represents a part in the inventory which is comprised of Parts.
 * 
 * @author Dale Richards <dric123@wgu.edu>
 */
public class Product {
    
    // Internal product ID
    private int productID;
    
    // Common name of the product
    private String name;
    
    // Price per unit of the product
    private double price;
    
    // Current inventory
    private int inStock;
    
    // Minimum required inventory
    private int min;
    
    // Maximum required inventory
    private int max;
    
    // List of associated parts
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public Product() {
    }
    
    /**
     * Add an associated part.
     * 
     * @param associatedPart 
     */
    public void addAssociatedPart(Part associatedPart) {
        this.associatedParts.add(associatedPart);
    }
    
    /**
     * Get a list of associated parts.
     * 
     * @return associated parts
     */
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }
    
    /**
     * Get the number of associated parts.
     * 
     * @return associated parts
     */
    public int getAssociatedPartsCount() {
        return associatedParts.size();
    }
    
    /**
     * Get the current inventory.
     * 
     * @return current inventory
     */
    public int getInStock() {
        return inStock;
    }
    
    /**
     * Get max required inventory
     * 
     * @return  max required inventory
     */
    public int getMax() {
        return max;
    }
    
    /**
     * Get min required inventory
     * 
     * @return min required inventory
     */
    public int getMin() {
        return min;
    }
    
    /**
     * Get product common name
     * 
     * @return product common name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Get price per unit
     * 
     * @return price per unit
     */
    public double getPrice() {
        return price;
    }
    
    /**
     * Get product internal ID
     * 
     * @return product internal ID
     */
    public int getProductID() {
        return productID;
    }
    
    /**
     * Ensure a product is valid. Throw a custom exception as required.
     * 
     * @return
     * @throws ValidationException 
     */
    public boolean isValid() throws ValidationException {
        double totalPartsPrice = 0.00;
        
        // summate the price of associated parts
        for(Part p : getAssociatedParts()) {
            totalPartsPrice += p.getPrice();
        }
        
        // ensure a product is named
        if (getName().equals("")) {
            throw new ValidationException("The name field cannot be empty.");
        }
        
        // inventory must be at least 1
        if (getInStock() < 0) {
            throw new ValidationException("The current inventory must be greater than 0.");
        }
        
        // price must be positive
        if (getPrice() < 0) {
            throw new ValidationException("The price must be greater than $0");
        }
        
        // a product must have at least one part
        if (getAssociatedPartsCount() < 1) {
            throw new ValidationException("The product must contain at least 1 part.");
        }
        
        // the sum of parts must be less than the price of the product
        if (totalPartsPrice > getPrice()) {
            throw new ValidationException("The product price must be greater than total cost of associated parts.");
        }
        
        // min inventory must be greater than zero
        if (getMin() < 0) {
            throw new ValidationException("The minimum inventory must be greater than 0.");
        }
        
        // max inventory must be greater than minimum
        if (getMin() > getMax()) {
            throw new ValidationException("The minimum inventory must be less than the maximum.");
        }
        
        // current inventory must be between the min and max.
        if (getInStock() < getMin() || getInStock() > getMax()) {
            throw new ValidationException("The current inventory must be between the minimum and maximum inventory.");
        }
        
        return true;
    }
    
    /**
     * Lookup associated part by part ID
     * 
     * @param partID
     * @return associated parts matching filter
     */
    public Part lookupAssociatedPart(int partID) {
        for (Part p : associatedParts) {
            if (p.getPartID() == partID) {
                return p;
            }
        }
        
        return null;
    }
    
    /**
     * Delete all associated parts
     */
    public void purgeAssociatedParts() {
        associatedParts = FXCollections.observableArrayList();
    }
    
    /**
     * Remove an associated part
     * 
     * @param partID
     * @return 
     */
    public boolean removeAssociatedPart(int partID) {
        for (Part p : associatedParts) {
            if (p.getPartID() == partID) {
                associatedParts.remove(p);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Set the current inventory
     * 
     * @param inStock 
     */
    public void setInStock(int inStock) {
        this.inStock = inStock;
    }
    
    /**
     * Set the max required inventory
     * 
     * @param max 
     */
    public void setMax(int max) {
        this.max = max;
    }
    
    /**
     * Set the min required inventory
     * 
     * @param min 
     */
    public void setMin(int min) {
        this.min = min;
    }
    
    /**
     * Set common name
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /** 
     * Set price per unit
     * 
     * @param price 
     */
    public void setPrice(double price) {
        this.price = price;
    }
    
    /**
     * Set product ID
     * 
     * @param productID 
     */
    public void setProductID(int productID) {
        this.productID = productID;
    }
}
