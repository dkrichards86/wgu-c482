package InventoryApp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <h1>Inventory Application for C482</h1>
 * This application is a CRUD inventory application, providing a simple JavaFX 
 * GUI for maintaining an inventory. There is no data persistence.
 * 
 * @author Dale Richards <dric123@wgu.edu>
 */
public class InventoryApp extends Application {
    /**
     * Start the GUI by loading the main FXML and setting the GUI viewport
     * 
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(InventoryApp.class.getResource("/InventoryApp/views/Main.fxml"));
        Parent root = loader.load();
    
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Launch the GUI
     * 
     * @param args 
     */
    public static void main(String[] args) {
        launch(args);
    }
}