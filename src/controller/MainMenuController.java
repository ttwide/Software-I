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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.Product;

/**
 *
 * @author lisakim
 */
public class MainMenuController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    @FXML
    private TextField searchPartsTxt;

    @FXML
    private TextField searchProductsTxt;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partIdCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInventoryLevelCol;

    @FXML
    private TableColumn<Part, Double> partPricePerUnitCol;

    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Integer> productInventoryLevelCol;

    @FXML
    private TableColumn<Product, Double> productPricePerUnitCol;
    
    
    @FXML
    void onActionAddParts(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionAddProducts(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionDeleteParts(ActionEvent event) throws IOException {
        
        Part item = partsTableView.getSelectionModel().getSelectedItem();
        
        if (item == null ){
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete"
                + " the selected part.  Click OK to continue.");
        Optional<ButtonType> result = alert.showAndWait();
        
       
        if (result.isPresent() && result.get().equals(ButtonType.OK)){
             Inventory.deletePart(item);
             partsTableView.setItems(Inventory.getAllParts());
             partsTableView.getSelectionModel().clearSelection();
        }

        partsTableView.getSelectionModel().clearSelection();

    }
    

    @FXML
    void onActionDeleteProducts(ActionEvent event) throws IOException {
        Product item = productsTableView.getSelectionModel().getSelectedItem();
        
        if (item == null){
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete"
                + " the selected product.  Click OK to continue.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get().equals(ButtonType.OK)){
            Inventory.deleteProduct(item);
            productsTableView.setItems(Inventory.getAllProducts());
            productsTableView.getSelectionModel().clearSelection();
        }
        productsTableView.getSelectionModel().clearSelection();   

    }

    @FXML
    void onActionExitMainMenu(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void onActionModifyParts(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
        loader.load();
        
        ModifyPartController MPController = loader.getController();
        if (partsTableView.getSelectionModel().getSelectedItem() != null){
            Part sendingPart = partsTableView.getSelectionModel().getSelectedItem();
            
            if (sendingPart instanceof InHouse) {
                MPController.sendInHousePart((InHouse)sendingPart);
            } 
            
            if (sendingPart instanceof Outsourced){
               MPController.sendOutsourcedPart((Outsourced)sendingPart);
            }
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
           
        }

    }

    @FXML
    void onActionModifyProducts(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
        loader.load();
        
        ModifyProductController MProdController = loader.getController();
        if (productsTableView.getSelectionModel().getSelectedItem() != null){
            Product sendingProduct = productsTableView.getSelectionModel().getSelectedItem();
            
            MProdController.copyList(sendingProduct);
            MProdController.sendProduct(sendingProduct);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        
    }

    @FXML
    void onActionSearchParts(ActionEvent event) throws IOException {
        
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

        partsTableView.setItems(returnedParts);
        searchPartsTxt.setText("");
        
    }
    
    @FXML
    void onActionSearchProducts(ActionEvent event) throws IOException {
        
        ObservableList<Product> returnedProducts = FXCollections.observableArrayList();
        String q = searchProductsTxt.getText();
        returnedProducts = Inventory.lookupProduct(q);
        
        if (returnedProducts.isEmpty()){
            try {
                int id = Integer.parseInt(searchProductsTxt.getText());
                Product item = Inventory.lookupProduct(id);
                if (item != null){
                    returnedProducts.add(item);
                }
            } catch (NumberFormatException e) {
                System.out.println("Number format Exception");
            }
        }
        
        productsTableView.setItems(returnedProducts);
        searchProductsTxt.setText("");      
    }
    


////    MAYBE THIS WAS USED FOR UPDATE OR DELETE    
////    MAY NOT NEED SINCE THE SEARCH IS THE FILTER    
//    public boolean partSearch(int partId){
//        for (Part part : Inventory.getAllParts())
//            if (part.getId() == partId){
//                return true;
//            }
//        return false;  
//    }
//    
//    public boolean productSearch(int productId){
//        for (Product product : Inventory.getAllProducts())
//            if (product.getId() == productId){
//                return true;
//            }
//        return false;  
//    }
//    
//    
//    @FXML
//    void loadMenu(String menu) throws IOException{
//        scene = FXMLLoader.load(getClass().getResource(menu));
//        stage.setScene(new Scene(scene));
//        stage.show();
//        
//    }
        
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partsTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        productsTableView.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
    }    
    
}

