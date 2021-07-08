/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
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
import model.Inventory;
import model.Part;
import model.Product;

/**
 * FXML Controller class
 *
 * @author lisakim
 */
public class AddProductController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private TextField searchProductTxt;

    @FXML
    private TableView<Part> topProductTableView;

    @FXML
    private TableColumn<Part, Integer> topTVPartId;

    @FXML
    private TableColumn<Part, String> topTVPartName;

    @FXML
    private TableColumn<Part, Integer> topTVInventoryLevel;

    @FXML
    private TableColumn<Part, Double> topTVPricePerUnit;

    @FXML
    private TableView<Part> bottomProductTV;

    @FXML
    private TableColumn<Part, Integer> botTVPartId;

    @FXML
    private TableColumn<Part, String> botTVPartName;

    @FXML
    private TableColumn<Part, Integer> botTVInventoryLevel;

    @FXML
    private TableColumn<Part, Double> botTVPricePerUnit;

    @FXML
    private TextField productIdTxt;

    @FXML
    private TextField productNameTxt;

    @FXML
    private TextField productInvTxt;

    @FXML
    private TextField productPriceTxt;

    @FXML
    private TextField productMaxTxt;

    @FXML
    private TextField productMinTxt;

    @FXML
    void onActionAddProduct(ActionEvent event) throws IOException {
        Part selectedPart = topProductTableView.getSelectionModel().getSelectedItem();
        if (selectedPart != null) {
            bottomProductTV.getItems().add(topProductTableView.getSelectionModel().getSelectedItem());
        }
    }
    

    @FXML
    void onActionCancelProduct(ActionEvent event) throws IOException {
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
    void onActionDeleteProduct(ActionEvent event) throws IOException {
        Part selectedPart = bottomProductTV.getSelectionModel().getSelectedItem();
        if (selectedPart == null){
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete"
                + " the selected part.  Click OK to continue.");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get().equals(ButtonType.OK)){
            bottomProductTV.getItems().remove(selectedPart);
        }
        bottomProductTV.getSelectionModel().clearSelection();

    }
    private static int id = 105;
    
    @FXML
    void onActionSaveProduct(ActionEvent event) throws IOException {
        
        try {
            id++;
            //int id = Integer.parseInt(productIdTxt.getText());
            String name = productNameTxt.getText();
            Double price = Double.parseDouble(productPriceTxt.getText());
            int stock = Integer.parseInt(productInvTxt.getText());
            int min = Integer.parseInt(productMinTxt.getText());
            int max = Integer.parseInt(productMaxTxt.getText());
            
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
            
            if (bottomProductTV.getItems().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Product must have at least one associated part.");
                alert.showAndWait();  
                return;
            }
            
            Product createdProduct = new Product(id, name, price, stock, min, max);
            for (Part part : bottomProductTV.getItems()){
                createdProduct.addAssociatedPart(part);
            }

            for (Part part : createdProduct.getAllAssociatedParts()){
                System.out.println(part.getName());
            }
            Inventory.addProduct(createdProduct);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            loadMenu("/view/MainMenu.fxml");
            
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each field.");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionSearchProducts(ActionEvent event) throws IOException {
        ObservableList<Part> returnedParts = FXCollections.observableArrayList();
        String q = searchProductTxt.getText();
        
        returnedParts = Inventory.lookupPart(q);
        System.out.println(q);
        if (returnedParts.isEmpty()){
            try {
               int id = Integer.parseInt(searchProductTxt.getText());
               Part item = Inventory.lookupPart(id);
               if (item != null){
                    returnedParts.add(item);
               }
            } catch(NumberFormatException e) {
                System.out.println("Number format exception");
            }
        }

        topProductTableView.setItems(returnedParts);
        searchProductTxt.setText("");
    }
    
    @FXML
    void loadMenu(String menu) throws IOException{
    scene = FXMLLoader.load(getClass().getResource(menu));
    stage.setScene(new Scene(scene));
    stage.show();
        
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        topProductTableView.setItems(Inventory.getAllParts());
        topTVPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        topTVPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        topTVInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        topTVPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        botTVPartId.setCellValueFactory(new PropertyValueFactory<>("id"));
        botTVPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        botTVInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        botTVPricePerUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
    }    
}