package InventoryApp.models;

/**
 * InhousePart is a derivative of Part, adding a machine ID to the instance.
 * 
 * @see Part
 * @author Dale Richards <dric123@wgu.edu>
 */
public class InhousePart extends Part {

    // Machine ID of the part
    private int machineID;
        
    /**
     * Get the machine ID
     * 
     * @return int machine ID 
     */
    public int getMachineID() {
        return machineID;
    }
    
    /**
     * Set the machine ID
     * 
     * @param machineID 
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
}
