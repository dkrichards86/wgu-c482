package InventoryApp.views;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import InventoryApp.exceptions.ValidationException;
import InventoryApp.models.Inventory;
import InventoryApp.models.Part;
import InventoryApp.models.Product;
import static InventoryApp.views.MainController.getModifiedProduct;

/**
 * Products Controller. In accordance with Don't Repeat Yourself (DRY) best
 * practices, this class contains functionality for both adding and modifying a
 * product. Ideally this would be feature more composition instead of relying on 
 * Observables set in the main controller.
 * <br>
 * The associated Products.fxml contains markup for both add and modify product
 * screens. This screen was built using SceneBuilder.
 * 
 * @author Dale Richards <dric123@wgu.edu>
 */
public class ProductsController implements Initializable {

    // Dynamic page label
    @FXML
    private Label ProductsPageLabel;
    
    // Product ID
    @FXML
    private TextField ProductsIDField;
    
    // Product name
    @FXML
    private TextField ProductsNameField;
    
    // Product max required inventory
    @FXML
    private TextField ProductsMaxField;
    
    // Product min required inventory
    @FXML
    private TextField ProductsMinField;
    
    // prododuct inventory
    @FXML
    private TextField ProductsInStockField;
    
    // Product price
    @FXML
    private TextField ProductsPriceField;
    
    // Parts search field
    @FXML
    private TextField ProductPartsSearchField;
    
    // All parts table
    @FXML
    private TableView<Part> ProductAllPartsTable;
    
    // All parts ID
    @FXML
    private TableColumn<Part, Integer> ProductAllPartsIDCol;
    
    // All parts name
    @FXML
    private TableColumn<Part, String> ProductAllPartsNameCol;
    
    // All parts inventory
    @FXML
    private TableColumn<Part, Integer> ProductAllPartsInStockCol;
    
    // All parts price
    @FXML
    private TableColumn<Part, Double> ProductAllPartsPriceCol;
    
    // Current Parts Table
    @FXML
    private TableView<Part> ProductCurrentPartsTable;
    
    // Current Parts ID
    @FXML
    private TableColumn<Part, Integer> ProductCurrentPartsIDCol;
    
    // Current parts name
    @FXML
    private TableColumn<Part, String> ProductCurrentPartsNameCol;
    
    // Current parts inventory
    @FXML
    private TableColumn<Part, Integer> ProductCurrentPartsInStockCol;
    
    // Current parts price
    @FXML
    private TableColumn<Part, Double> ProductCurrentPartsPriceCol;
    
    // List of parts associated with this project
    private ObservableList<Part> productParts = FXCollections.observableArrayList();
    
    // Product being modified if this is a modification, else null 
    private final Product modifiedProduct;
    
    /**
     * Constructor
     */
    public ProductsController() {
        this.modifiedProduct = getModifiedProduct();
    }
    
    /**
     * Add a part to the product.
     * 
     * @param event 
     */
    @FXML
    void handleAddProductPart(ActionEvent event) {
        Part part = ProductAllPartsTable.getSelectionModel().getSelectedItem();
        productParts.add(part);
        populateCurrentPartsTable();
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
        alert.setContentText("Are you sure you want to cancel update of product " + ProductsNameField.getText() + "?");
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
     * Delete a product part.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleDeleteProductPart(ActionEvent event) throws IOException {
        // Since products must have at least one part, we ensure there are at
        //   least two parts prior to deletion. There is probably a more elegant
        //   way to handle this, perhaps using a transient ArrayList.
        if (productParts.size() > 2) {
            Part part = ProductCurrentPartsTable.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Part Delete");
            alert.setHeaderText("Confirm deletion");
            alert.setContentText("Are you sure you want to disassociate " + part.getName() + " ?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                productParts.remove(part);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Part Deletion Error!");
            alert.setHeaderText("Product requires one part!");
            alert.setContentText("This product must have at least one part.");
            alert.showAndWait();
        }
    }

    /**
     * Handle a save event. This saves a new product or updates an existing one
     * based on this.modifiedProduct.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleSave(ActionEvent event) throws IOException {
        String productName = ProductsNameField.getText();
        String productInv = ProductsInStockField.getText();
        String productPrice = ProductsPriceField.getText();
        String productMin = ProductsMinField.getText();
        String productMax = ProductsMaxField.getText();

        Product newProduct = new Product();
        newProduct.setName(productName);
        newProduct.setPrice(Double.parseDouble(productPrice));
        newProduct.setInStock(Integer.parseInt(productInv));
        newProduct.setMin(Integer.parseInt(productMin));
        newProduct.setMax(Integer.parseInt(productMax));
       
        // If this is a modification, clear old parts.
        // This is basically a preemptive dedupe.
        if (modifiedProduct != null) {
            modifiedProduct.purgeAssociatedParts();
        }
        
        // Iterate productParts and add them to the product
        for (Part p: productParts) {
            newProduct.addAssociatedPart(p);
        }
        
        try {
            newProduct.isValid();
            
            // Create or update product as required.
            if (modifiedProduct == null) {
                newProduct.setProductID(Inventory.getProductsCount());
                Inventory.addProduct(newProduct);
            } else {
                newProduct.setProductID(modifiedProduct.getProductID());
                Inventory.updateProduct(newProduct);
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
            alert.setHeaderText("Product not valid");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    
    /**
     * Handle parts search.
     * @param event 
     */
    @FXML
    void handleSearchParts(ActionEvent event) throws IOException {
        String partsSearchIdString = ProductPartsSearchField.getText();
        Part searchedPart = Inventory.lookupPart(Integer.parseInt(partsSearchIdString));

        if (searchedPart != null) {
            ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
            filteredPartsList.add(searchedPart);
            ProductAllPartsTable.setItems(filteredPartsList);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any part!");
            alert.showAndWait();
        }
    }
    
    /**
     * Initializes the controller class.
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // If modifiedProduct is null, we prepare the view in the add format.
        //   Otherwise we populate existing data in the modify format,
        if (modifiedProduct == null) {
            ProductsPageLabel.setText("Add Product");
            int productAutoID = Inventory.getProductsCount();
            ProductsIDField.setText("AUTO GEN: " + productAutoID);
            System.out.println("Here");
        } else {
            ProductsPageLabel.setText("Modify Product");
            
            ProductsIDField.setText(Integer.toString(modifiedProduct.getProductID()));
            ProductsNameField.setText(modifiedProduct.getName());
            ProductsInStockField.setText(Integer.toString(modifiedProduct.getInStock()));
            ProductsPriceField.setText(Double.toString(modifiedProduct.getPrice()));
            ProductsMinField.setText(Integer.toString(modifiedProduct.getMin()));
            ProductsMaxField.setText(Integer.toString(modifiedProduct.getMax()));
        
            productParts = modifiedProduct.getAssociatedParts();
        }
        
        ProductAllPartsIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        ProductAllPartsNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ProductAllPartsInStockCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        ProductAllPartsPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        ProductCurrentPartsIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        ProductCurrentPartsNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        ProductCurrentPartsInStockCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        ProductCurrentPartsPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        // Hydrate the associated tables
        populateAvailablePartsTable();
        populateCurrentPartsTable();
    }
    
    /**
     * Populate the available parts table.
     */
    public void populateAvailablePartsTable() {
        ProductAllPartsTable.setItems(Inventory.getParts());
    }

    /**
     * Populate the current parts table.
     */
    public void populateCurrentPartsTable() {
        ProductCurrentPartsTable.setItems(productParts);
    }

}
