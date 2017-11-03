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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import InventoryApp.InventoryApp;
import InventoryApp.models.Inventory;
import static InventoryApp.models.Inventory.canDeleteProduct;
import static InventoryApp.models.Inventory.getParts;
import static InventoryApp.models.Inventory.getProducts;
import static InventoryApp.models.Inventory.removePart;
import static InventoryApp.models.Inventory.removeProduct;
import InventoryApp.models.Part;
import InventoryApp.models.Product;

/**
 * Main Controller. This class controls the main inventory screen containing
 * lists of parts and products. It also broadcasts modification of components to
 * Products or Parts controllers.
 * 
 * @author Dale Richards <dric123@wgu.edu>
 */
public class MainController implements Initializable {

    // the whole parts table
    @FXML
    private TableView<Part> MainPartsTable;
    
    // parts table ID column
    @FXML
    private TableColumn<Part, Integer> MainPartIDCol;
    
    // parts table name column
    @FXML
    private TableColumn<Part, String> MainPartNameCol;
    
    // parts table current inventory column
    @FXML
    private TableColumn<Part, Integer> MainPartInStockCol;
    
    // parts table price column
    @FXML
    private TableColumn<Part, Double> MainPartPriceCol;
    
    // the whole products tabls
    @FXML
    private TableView<Product> MainProductsTable;
    
    // products table ID column
    @FXML
    private TableColumn<Product, Integer> MainProductIDCol;
    
    // products table name column
    @FXML
    private TableColumn<Product, String> MainProductNameCol;
    
    // products table current inventory column
    @FXML
    private TableColumn<Product, Integer> MainProductInStockCol;
    
    // products table price column
    @FXML
    private TableColumn<Product, Double> MainProductPriceCol;
    
    // parts search field
    @FXML
    private TextField MainPartsSearchField;
    
    // products search field
    @FXML
    private TextField MainProductsSearchField;

    // the current modified part (if applicable)
    private static Part modifiedPart;
    
    // the current modified product (if applicable)
    private static Product modifiedProduct;
    
    /**
     * Constructor
     */
    public MainController() {
    }
    
    /**
     * Get the current modified part.
     * 
     * @return current modified part
     */
    public static Part getModifiedPart() {
        return modifiedPart;
    }
    
    /**
     * Set a part as current modified.
     * 
     * @param modifyPart 
     */
    public void setModifiedPart(Part modifyPart) {
        MainController.modifiedPart = modifyPart;
    }
    
    /**
     * Get the current modified product.
     * 
     * @return current modified product
     */
    public static Product getModifiedProduct() {
        return modifiedProduct;
    }
    
    /**
     * Set a product as current modified.
     * 
     * @param modifiedProduct 
     */
    public void setModifiedProduct(Product modifiedProduct) {
        MainController.modifiedProduct = modifiedProduct;
    }

    /**
     * Handle exit. Render a confirmation modal and close GUI as applicable.
     * 
     * @param event 
     */
    @FXML
    void handleExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm exit!");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    /**
     * Switch to the add parts screen.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleAddPart(ActionEvent event) throws IOException {        
        showPartsScreen(event);
    }

    /**
     * Switch to the add product screen.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleAddProduct(ActionEvent event) throws IOException {
        showProductScreen(event);
    }

    /**
     * Handle the deletion of a part.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleDeletePart(ActionEvent event) throws IOException {
        Part part = MainPartsTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Part Delete");
        alert.setHeaderText("Confirm deletion?");
        alert.setContentText("Are you sure you want to delete " + part.getName() + "?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == ButtonType.OK) {
            removePart(part.getPartID());
            populatePartsTable();
        }
    }
    
    /**
     * Delete a product.
     * <p>
     * <b>Note:</b> The Taskstream document suggests a product must always have
     * at least one part, but can only be deleted if no parts are associated.
     * This means once a product is added, to include associating a part, it can
     * never be deleted.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleDeleteProduct(ActionEvent event) throws IOException {
        Product product = MainProductsTable.getSelectionModel().getSelectedItem();

        if (!canDeleteProduct(product)) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Produt Deletion Error!");
            alert.setHeaderText("Produt cannot be removed!");
            alert.setContentText("This product has associated parts.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.initModality(Modality.NONE);
            alert.setTitle("Product Delete");
            alert.setHeaderText("Confirm deletion?");
            alert.setContentText("Are you sure you want to delete " + product.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                removeProduct(product.getProductID());
                populatePartsTable();
            }
        }
    }
    
    /**
     * Set modifiedPart. modifiedPart is used to dynamically switch between add
     * and modify view.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleModifyPart(ActionEvent event) throws IOException {
        modifiedPart = MainPartsTable.getSelectionModel().getSelectedItem();
        setModifiedPart(modifiedPart);

        showPartsScreen(event);
    }

    /**
     * Set modifiedProduct. modifiedProduct is used to dynamically switch
     * between add and modify view.
     * 
     * @param event
     * @throws IOException 
     */
    @FXML
    void handleModifyProduct(ActionEvent event) throws IOException {
        modifiedProduct = MainProductsTable.getSelectionModel().getSelectedItem();
        setModifiedProduct(modifiedProduct);
        
        showProductScreen(event);
    }
    
    /**
     * Part search handler. Update the parts table with items matching the input
     * field value. Render a modal if no parts match.
     * 
     * Ideally this would perform a substring search on name, but the UML
     * required integer parameter for lookupPart.
     * 
     * @param field
     * @param table 
     */
    @FXML
    void handleSearchPart(ActionEvent event) throws IOException {
        String partsSearchIdString = MainPartsSearchField.getText();
        Part searchedPart = Inventory.lookupPart(Integer.parseInt(partsSearchIdString));

        if (searchedPart != null) {
            ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
            filteredPartsList.add(searchedPart);
            MainPartsTable.setItems(filteredPartsList);
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Part not found");
            alert.setContentText("The search term entered does not match any part!");
            alert.showAndWait();
        }
    }
    
    /**
     * Product search handler. Update the parts table with items matching the
     * input field value. Render a modal if no products match.
     * 
     * Ideally this would perform a substring search on name, but the UML
     * required integer parameter for lookupProduct.
     * 
     * @param field
     * @param table 
     */
    @FXML
    void handleSearchProduct(ActionEvent event) throws IOException {
        String productSearchIdString = MainProductsSearchField.getText();
        Product searchedProduct= Inventory.lookupProduct(Integer.parseInt(productSearchIdString));

        if (searchedProduct != null) {
            ObservableList<Product> filteredProductList = FXCollections.observableArrayList();
            filteredProductList.add(searchedProduct);
            MainProductsTable.setItems(filteredProductList);
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Search Error");
            alert.setHeaderText("Product not found");
            alert.setContentText("The search term entered does not match any product!");
            alert.showAndWait();
        }
    }
   
    /**
     * Initialize the controller.
     * 
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // initialize part and product with nulls
        setModifiedPart(null);
        setModifiedProduct(null);

        MainPartIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartID()).asObject());
        MainPartNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        MainPartInStockCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        MainPartPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        MainProductIDCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getProductID()).asObject());
        MainProductNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        MainProductInStockCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getInStock()).asObject());
        MainProductPriceCol.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
        
        populatePartsTable();
        populateProductsTable();
    }
    
    /**
     * Populate the parts table.
     */
    public void populatePartsTable() {
        MainPartsTable.setItems(getParts());
    }

    /**
     * Populate the product table.
     */
    public void populateProductsTable() {
        MainProductsTable.setItems(getProducts());
    }
    
    /**
     * Set the main app. Populate the parts and products tables.
     * 
     * @param mainApp 
     */
    public void setMainApp(InventoryApp mainApp) {
        populatePartsTable();
        populateProductsTable();
    }
    
    /**
     * Render the parts screen. Both add and modify parts functionality is
     * handled by the same view and controller, so we can make a generic handler
     * for it.
     * 
     * @param event
     * @throws IOException 
     */
    public void showPartsScreen(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Parts.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
    
    /**
     * Render the products screen. Both add and modify products functionality is
     * handled by the same view and controller, so we can make a generic handler
     * for it.
     * 
     * @param event
     * @throws IOException 
     */
    public void showProductScreen(ActionEvent event) throws IOException {
        Parent loader = FXMLLoader.load(getClass().getResource("Products.fxml"));
        Scene scene = new Scene(loader);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(scene);
        window.show();
    }
}
