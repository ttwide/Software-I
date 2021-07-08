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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

/**
 * FXML Controller class
 *
 * @author lisakim
 */
public class AddPartController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private RadioButton partInHouseRBtn;

    @FXML
    private RadioButton partOutsourcedRBtn;

    @FXML
    private TextField partIdTxt;

    @FXML
    private TextField partNameTxt;

    @FXML
    private TextField partInvTxt;

    @FXML
    private TextField partPriceCostTxt;
    
    @FXML
    private Label companyNameLabel;
    
    @FXML
    private TextField partCompanyNameTxt;
    
    @FXML
    private Label machineIdLbl;

    @FXML
    private TextField partMaxTxt;

    @FXML
    private TextField partMinTxt;

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
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
    
    private static int id = 5;

    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {
              
        
        try{
            id++;
            String name = partNameTxt.getText();
            double price = Double.parseDouble(partPriceCostTxt.getText());
            int stock = Integer.parseInt(partInvTxt.getText());
            int min = Integer.parseInt(partMinTxt.getText());
            int max = Integer.parseInt(partMaxTxt.getText());
            
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
           
            
            if (partInHouseRBtn.isSelected()){
                   int machineId = Integer.parseInt(partCompanyNameTxt.getText());
                   Inventory.addPart(new InHouse(machineId, id, name, price, stock, min, max));
                   stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                   scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                   stage.setScene(new Scene(scene));
                   stage.show(); 
            }
            if (partOutsourcedRBtn.isSelected()){   
                   String companyName = partCompanyNameTxt.getText();
                   Inventory.addPart(new Outsourced(companyName, id, name, price, stock, min, max));
                   stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                   scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                   stage.setScene(new Scene(scene));
                   stage.show();     
            }
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each field.");
            alert.showAndWait();
        }
        
//        id++;
//        //System.out.println(id);
//        
//        String name = partNameTxt.getText();
//        double price = Double.parseDouble(partPriceCostTxt.getText());
//        int stock = Integer.parseInt(partInvTxt.getText());
//        int min = Integer.parseInt(partMinTxt.getText());
//        int max = Integer.parseInt(partMaxTxt.getText());
//        if (partInHouseRBtn.isSelected()){
//            try {
//               int machineId = Integer.parseInt(partCompanyNameTxt.getText());
//               Inventory.addPart(new InHouse(machineId, id, name, price, stock, min, max));
//               stage = (Stage)((Button)event.getSource()).getScene().getWindow();
//               scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
//               stage.setScene(new Scene(scene));
//               stage.show(); 
//            } catch (NumberFormatException e){
//               return; 
//            } 
//        }
//        if (partOutsourcedRBtn.isSelected()){
//            try {
//                int companyName = Integer.parseInt(partCompanyNameTxt.getText());
//            } catch (NumberFormatException e){
//               String companyName = partCompanyNameTxt.getText();
//               Inventory.addPart(new Outsourced(companyName, id, name, price, stock, min, max));
//               stage = (Stage)((Button)event.getSource()).getScene().getWindow();
//               scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
//               stage.setScene(new Scene(scene));
//               stage.show();
//            }
//        }
    }
    
    
    @FXML
    void onActionInHouseRBtn(ActionEvent event) {
        companyNameLabel.setVisible(false);
        machineIdLbl.setVisible(true);
        companyNameLabel.setTranslateX(23);
        companyNameLabel.setPrefWidth(0);
        

    }

    @FXML
    void onActionOutsourcedRBtn(ActionEvent event) {
        machineIdLbl.setVisible(false);
        companyNameLabel.setVisible(true);
        companyNameLabel.setTranslateX(-23);
        companyNameLabel.setPrefWidth(400);
        
    }
   


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        companyNameLabel.setVisible(false);
        partInHouseRBtn.setSelected(true);
        
    }    

    
}
