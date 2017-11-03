package InventoryApp.models;

/**
 * OutsourcedPart is a derivative of Part, adding a machine ID to the instance.
 * 
 * @see Part
 * @author Dale Richards <dric123@wgu.edu>
 */
public class OutsourcedPart extends Part {

    // Name of the part's manufacturer
    private String companyName;
    
    /**
     * Get the manufacturer's name
     * 
     * @return String name of the manufacturer of this part 
     */
    public String getCompanyName() {
        return companyName;
    }
        
    /**
     * Set the manufacturer's name
     * 
     * @param companyName 
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
