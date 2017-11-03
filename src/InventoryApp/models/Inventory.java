package InventoryApp.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Inventory is a master list of all parts and products.
 * 
 * @see Part, Product
 * @author Dale Richards <dric123@wgu.edu>
 */
public class Inventory {
    
    // Observable list of parts in the inventory
    private final static ObservableList<Part> allParts = FXCollections.observableArrayList();
    
    // Observable list of products in the inventory
    private final static ObservableList<Product> products = FXCollections.observableArrayList();

    /**
     * Constructor
     */
    public Inventory() {
    }

    /**
     * Add a new part to the inventory
     * 
     * @param newPart 
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }
    
    /**
     * Add a new product to the inventory
     * @param newProduct 
     */
    public static void addProduct(Product newProduct){
        products.add(newProduct);
    }
    
    /**
     * Determine whether or not this product can be deleted. If a product has 
     * parts, we cannot delete.
     * <p>
     * This is called by MainController to determine which modal to display,
     * allowing the user to cancel deletion.
     * 
     * @see MainController
     * 
     * @param product
     * @return true if the product can be deleted, else false
     */
    public static boolean canDeleteProduct(Product product) {
        return product.getAssociatedPartsCount() == 0;
    }
    
    /**
     * Get a list of all current parts
     * 
     * @return list of parts in inventory
     */
    public static ObservableList<Part> getParts() {
        return allParts;
    }
    
    /**
     * Get the number of parts in the inventory
     * 
     * @return number of parts.
     */
    public static int getPartsCount() {
        return allParts.size();
    }
    
    /**
     * Get the number of products in the inventory
     * 
     * @return number of products.
     */
    public static int getProductsCount() {
        return products.size();
    }
    
    /**
     * Get a list of current products
     * 
     * @return list of products in inventory
     */
    public static ObservableList<Product> getProducts() {
        return products;
    }
    
    /**
     * Look up a part by ID
     * 
     * @param partID
     * @return Part if applicable, else null
     */
    public static Part lookupPart(int partID) {
        for (Part p : allParts) {
            if (p.getPartID() == partID) {
                return p;
            }
        }

        return null;
    }
    
    /**
     * Look up a product by ID
     * 
     * @param productID
     * @return Product if applicable, else null
     */
    public static Product lookupProduct(int productID) {
        for (Product p : products) {
            if (p.getProductID() == productID) {
                return p;
            }
        }
        
        return null;
    }
    
    /**
     * Remove a part from the inventory
     * <p>
     * <b>Note:</b> This method was identified by `deletePart()` in the UML diagram, 
     * but was renamed `removePart()` for API consistency.
     * 
     * @param partID
     * @return true if the product was removed successfully, else false
     */
    public static boolean removePart(int partID) {
        for (Part p : allParts) {
            if (p.getPartID() == partID) {
                allParts.remove(p);
                return true;
            }
        }
        
        return false;
    }

    /**
     * Remove a product from the inventory
     * 
     * @param productID
     * @return true if the product was removed successfully, else false
     */
    public static boolean removeProduct(int productID) {
        for (Product p : products) {
            if (p.getProductID() == productID) {
                products.remove(p);
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Update a part in the inventory
     * 
     * @param updatedPart 
     */
    public static void updatePart(Part updatedPart) {
        allParts.set(updatedPart.getPartID(), updatedPart);
    }
    
    /**
     * Update a product in the inventory
     * 
     * @param updatedProduct 
     */
    public static void updateProduct(Product updatedProduct) {
        products.set(updatedProduct.getProductID(), updatedProduct);
    }
}
