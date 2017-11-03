package InventoryApp.views;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import InventoryApp.exceptions.ValidationException;
import InventoryApp.models.InhousePart;
import InventoryApp.models.Inventory;
import InventoryApp.models.OutsourcedPart;
import InventoryApp.models.Part;
import static InventoryApp.views.MainController.getModifiedPart;

/**
 * Parts Controller. In accordance with Don't Repeat Yourself (DRY) best
 * practices, this class contains functionality for both adding and modifying a
 * part. Ideally this would be feature more composition instead of relying on 
 * Observables set in the main controller.
 * <br>
 * The associated Parts.fxml contains markup for both add and modify parts
 * screens. This screen was built using SceneBuilder.
 * 
 * @author Dale Richards <dric123@wgu.edu>
 */
public class PartsController implements Initializable {

    // GUI part ID
    @FXML
    private TextField PartsIDField;
    
    // GUI part common name
    @FXML
    private TextField PartsNameField;
    
    // GUI part current inventory
    @FXML
    private TextField PartsInStockField;
    
    // GUI part price
    @FXML
    private TextField PartsPriceField;
    
    // GUI part max required inventory
    @FXML
    private TextField PartsMaxField;
    
    // GUI part min required inventory
    @FXML
    private TextField PartsMinField;
    
     // GUI variable manufacturer label (inhouse vs outsourced)
    @FXML
    private Label PartsMfgLabel;

    // GUI part manufacturer ID
    @FXML
    private TextField PartsMfgField;
    
    // GUI page label
    @FXML
    private Label PartsPageLabel;
    
    // GUI in-house radio button
    @FXML
    private RadioButton PartsInHouseRadioButton;
    
    // GUI outsourced radio button
    @FXML
    private RadioButton PartsOutsourcedRadioButton;
    
    // Flag representing the manufacturing status of this part
    //  ie. in-house or outsourced
    private boolean isInHouse;
    
    // Part being modified if this is a modification, else null 
    private final Part modifyPart;

    /**
     * Constructor
     */
    public PartsController() {
        this.modifyPart = getModifiedPart();
    }
    
    /**
     * Handle a switch to in-house. Update the instance data store and GUI to
     * reflect a change to in-house.
     * part.
     * 
     * @param event 
     */
    @FXML
    void handleInHouse(ActionEvent event) {
        isInHouse = true;
        PartsMfgLabel.setText("Mach ID");
    }
    
    /**
     * Handle a switch to outsourced. Update the instance data store and GUI to 
     * reflect a change to outsourced.
     * part.
     * 
     * @param event 
     */
    @FXML
    void handleOutsource(ActionEvent event) {
        isInHouse = false;
        PartsMfgLabel.setText("Company Nm");
    }

    /**
     * Handle a cancel event. This requires the user confirm intent to cancel 
     * add/modify, and returns them to the main view as required.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Cancel Modification");
        alert.setHeaderText("Confirm cancellation");
        alert.setContentText("Are you sure you want to cancel update of part " + PartsNameField.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.get() == ButtonType.OK) {
            Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
            Scene scene = new Scene(loader);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        }
    }

    /**
     * Handle a save event. This saves a new part or updates an existing one
     * based on this.modifiedPart.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleSave(ActionEvent event) throws IOException {
        // Get data from the GUI
        String partName = PartsNameField.getText();
        String partInv = PartsInStockField.getText();
        String partPrice = PartsPriceField.getText();
        String partMin = PartsMinField.getText();
        String partMax = PartsMaxField.getText();
        String partDyn = PartsMfgField.getText();
        
        if ("".equals(partInv)) {
            partInv = "0";
        }
        
        // The next if/else block is a bit unruly. Ideally, in a real world 
        // scenario and with a development budget, I'd implement a visitor
        // pattern, but that's a bit beyond the scope of work for this 
        // assignment.
        if (isInHouse) {
            // Create or modify an instance of InhousePart and set the instance
            //   vars as appropriate.
            InhousePart modifiedPart = new InhousePart();
            modifiedPart.setName(partName);
            modifiedPart.setPrice(Double.parseDouble(partPrice));
            modifiedPart.setInStock(Integer.parseInt(partInv));
            modifiedPart.setMin(Integer.parseInt(partMin));
            modifiedPart.setMax(Integer.parseInt(partMax));
            modifiedPart.setMachineID(Integer.parseInt(partDyn));

            try {
                modifiedPart.isValid();
                
                // If this is a modified part, we ill update it. Otherwise we save a
                //    new part.
                if (modifyPart == null) {
                    modifiedPart.setPartID(Inventory.getPartsCount());
                    Inventory.addPart(modifiedPart);
                } else {
                    int partID = modifyPart.getPartID();
                    modifiedPart.setPartID(partID);
                    Inventory.updatePart(modifiedPart);
                }
                
                // Return to the main screen
                Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
                Scene scene = new Scene(loader);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (ValidationException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ValidationError");
                alert.setHeaderText("Part not valid");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        } else {
            // Create or modify an instance of OutsourcedPart and set the instance
            //   vars as appropriate.
            OutsourcedPart modifiedPart = new OutsourcedPart();
            modifiedPart.setName(partName);
            modifiedPart.setPrice(Double.parseDouble(partPrice));
            modifiedPart.setInStock(Integer.parseInt(partInv));
            modifiedPart.setMin(Integer.parseInt(partMin));
            modifiedPart.setMax(Integer.parseInt(partMax));
            modifiedPart.setCompanyName(partDyn);
            
            try {
                modifiedPart.isValid();
                
                // If this is a modified part, we ill update it. Otherwise we save a
                //    new part.
                if (modifyPart == null) {
                    modifiedPart.setPartID(Inventory.getPartsCount());
                    Inventory.addPart(modifiedPart);
                } else {
                    int partID = modifyPart.getPartID();
                    modifiedPart.setPartID(partID);
                    Inventory.updatePart(modifiedPart);
                }
                
                // Return to the main screen
                Parent loader = FXMLLoader.load(getClass().getResource("Main.fxml"));
                Scene scene = new Scene(loader);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(scene);
                window.show();
            } catch (ValidationException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ValidationError");
                alert.setHeaderText("Part not valid");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Initialize the class. This is done dynamically through the modifyPart
     * instance variable. If modifyPart is not null (that is, it's an instance
     * of Part), we will use it's data to seed the GUI.
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (modifyPart == null) {
            PartsPageLabel.setText("Add Part");
            int partAutoID = Inventory.getPartsCount();
            PartsIDField.setText("AUTO GEN: " + partAutoID);
            
            isInHouse = true;
            PartsMfgLabel.setText("Mach ID");
        }
        else{
            PartsPageLabel.setText("Modify Part");
            PartsIDField.setText(Integer.toString(modifyPart.getPartID()));
            PartsNameField.setText(modifyPart.getName());
            PartsInStockField.setText(Integer.toString(modifyPart.getInStock()));
            PartsPriceField.setText(Double.toString(modifyPart.getPrice()));
            PartsMinField.setText(Integer.toString(modifyPart.getMin()));
            PartsMaxField.setText(Integer.toString(modifyPart.getMax()));
            
            // Since modifyPart belongs to the subclass, we have to cast it to
            //   the appropriate super class.
            if (modifyPart instanceof InhousePart) {
                PartsMfgField.setText(Integer.toString(((InhousePart) modifyPart).getMachineID()));
                
                PartsMfgLabel.setText("Mach ID");
                PartsInHouseRadioButton.setSelected(true);

            } else {
                PartsMfgField.setText(((OutsourcedPart) modifyPart).getCompanyName());
                PartsMfgLabel.setText("Comp Nm");
                PartsOutsourcedRadioButton.setSelected(true);
            }
        }
    }
}
