/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author lisakim
 */
public class ModifyProductController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    @FXML
    private TextField searchPartsTxt;
    
    @FXML
    private TableView<Part> modifyProductTopTV;

    @FXML
    private TableColumn<Part, Integer> topTVPartIdCol;

    @FXML
    private TableColumn<Part, String> topTVPartNameCol;

    @FXML
    private TableColumn<Part, Integer> topTVInventoryLevelCol;

    @FXML
    private TableColumn<Part, Double> topTVPricePerUnitCol;

    @FXML
    private TableView<Part> modifyProductBottomTV;

    @FXML
    private TableColumn<Part, Integer> botTVPartIdCol;

    @FXML
    private TableColumn<Part, String> botTVPartNameCol;

    @FXML
    private TableColumn<Part, Integer> botTVInventoryLevelCol;

    @FXML
    private TableColumn<Part, Double> botTVPricePerUnitCol;

    @FXML
    private TextField modifyProductIdTxt;

    @FXML
    private TextField modifyProductNameTxt;

    @FXML
    private TextField modifyProductInvTxt;

    @FXML
    private TextField modifyProductPriceTxt;

    @FXML
    private TextField modifyProductMaxTxt;

    @FXML
    private TextField modifyProductMinTxt;
    

    @FXML
    void onActionAddModifyProduct(ActionEvent event) throws IOException {
        Part selectedPart = modifyProductTopTV.getSelectionModel().getSelectedItem();
        if (selectedPart != null){
            modifyProductBottomTV.getItems().add(selectedPart);
        }
        modifyProductTopTV.getSelectionModel().clearSelection();
       
    }

    @FXML
    void onActionCancelModifyProduct(ActionEvent event) throws IOException { 
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete"
                + " any populated information.  Click OK to continue.");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get().equals(ButtonType.OK)){
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

    }

    @FXML
    void onActionDeleteModifyProduct(ActionEvent event) throws IOException {
        Part selectedPart = modifyProductBottomTV.getSelectionModel().getSelectedItem();
        
        if (selectedPart == null){
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete"
                + " the selected part.  Click OK to continue.");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get().equals(ButtonType.OK)){
            modifyProductBottomTV.getItems().remove(selectedPart);
            modifyProductBottomTV.getSelectionModel().clearSelection();
        }
        modifyProductBottomTV.getSelectionModel().clearSelection();

    }

    @FXML
    void onActionSaveModifyProduct(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(modifyProductIdTxt.getText());
            Product product = Inventory.lookupProduct(id);
            String name = modifyProductNameTxt.getText();
            int stock = Integer.parseInt(modifyProductInvTxt.getText());
            double price = Double.parseDouble(modifyProductPriceTxt.getText());
            int min = Integer.parseInt(modifyProductMinTxt.getText());
            int max = Integer.parseInt(modifyProductMaxTxt.getText());
            
            if (min > max){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Minimum quantity is greater that maximum.");
                alert.showAndWait();  
                return;
            } 
            
            if (stock > max || stock < min){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Inventory is not within minimum and maximum ranges.");
                alert.showAndWait();  
                return;
            }
            
            if (modifyProductBottomTV.getItems().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Product must have at least one associated part.");
                alert.showAndWait();  
                return;
            }
            
            ObservableList<Part> copyOfParts = FXCollections.observableArrayList();

            for (Part part: product.getAllAssociatedParts()){
                copyOfParts.add(part);
            }

            for (Part part: copyOfParts){
                product.deleteAssociatedPart(part);
            }

            for (Part part : modifyProductBottomTV.getItems()){
                product.addAssociatedPart(part);
            }
            
            product.setName(name);
            product.setStock(stock);
            product.setPrice(price);
            product.setMax(max);
            product.setMin(min);
  
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each field.");
            alert.showAndWait();
        }    
    }

    @FXML
    void onActionSearchModifyProduct(ActionEvent event) throws IOException {
        ObservableList<Part> returnedParts = FXCollections.observableArrayList();
        String q = searchPartsTxt.getText();
        
        returnedParts = Inventory.lookupPart(q);
        if (returnedParts.isEmpty()){
            try {
               int id = Integer.parseInt(searchPartsTxt.getText());
               Part item = Inventory.lookupPart(id);
               if (item != null){
                    returnedParts.add(item);
               }
            } catch(NumberFormatException e) {
                System.out.println("Number format exception");
            }
        }

        modifyProductTopTV.setItems(returnedParts);
        searchPartsTxt.setText("");
    }
    
    public void sendProduct(Product product){
       modifyProductBottomTV.setItems(copyList(product));

       modifyProductIdTxt.setText(String.valueOf(product.getId()));
       modifyProductNameTxt.setText(product.getName());
       modifyProductInvTxt.setText(String.valueOf(product.getStock()));
       modifyProductPriceTxt.setText(String.valueOf(product.getPrice()));
       modifyProductMaxTxt.setText(String.valueOf(product.getMax()));
       modifyProductMinTxt.setText(String.valueOf(product.getMin()));

        
    }
    
    public ObservableList<Part> copyList(Product product){
        ObservableList<Part> copy = FXCollections.observableArrayList();
        for (Part part : product.getAllAssociatedParts()){
            copy.add(part);
        }
        return copy;
    }


 
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        modifyProductTopTV.setItems(Inventory.getAllParts());
        topTVPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        topTVPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        topTVInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        topTVPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        botTVPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        botTVPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        botTVInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        botTVPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price")); 
        
    }    

}
