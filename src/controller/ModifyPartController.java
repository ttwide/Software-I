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
public class ModifyPartController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private RadioButton modifyPartInHouseRBtn;

    @FXML
    private RadioButton modifyPartOutsourcedRBtn;

    @FXML
    private TextField partIdTxt;

    @FXML
    private TextField partNameTxt;

    @FXML
    private TextField partInvTxt;

    @FXML
    private TextField partPriceCostTxt;

    @FXML
    private Label machineIdLbl;
    
    @FXML
    private Label companyNameLabel;
            
    @FXML
    private TextField partCompanyNameTxt;

    @FXML
    private TextField partMaxTxt;

    @FXML
    private TextField partMinTxt;
    
    @FXML
    void onActionInHouseRBtn(ActionEvent event) {
        companyNameLabel.setVisible(false);
        machineIdLbl.setVisible(true);
        companyNameLabel.setTranslateX(12);
        companyNameLabel.setPrefWidth(0);
    }

    @FXML
    void onActionOutsourcedRBtn(ActionEvent event) {
        machineIdLbl.setVisible(false);
        companyNameLabel.setVisible(true);
        companyNameLabel.setTranslateX(-12);
        companyNameLabel.setPrefWidth(400);
    }

    @FXML
    void onActionCancelModifyPart(ActionEvent event) throws IOException {
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
    void onActionSaveModifyPart(ActionEvent event) throws IOException {
        
        try{
            int index = -1;

            int id = Integer.parseInt(partIdTxt.getText());
            for(Part part : Inventory.getAllParts()){
                index++;
                if (part.getId() == id){
                    break;
                }
            }
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

            if (modifyPartInHouseRBtn.isSelected()){
                int machineId = Integer.parseInt(partCompanyNameTxt.getText());
                Inventory.updatePart(index, new InHouse(machineId, id, name, price, stock, min, max));
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show(); 
            }
            if (modifyPartOutsourcedRBtn.isSelected()){
                String companyName = partCompanyNameTxt.getText();
                Inventory.updatePart(index,new Outsourced(companyName, id, name, price, stock, min, max));
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
        
//        int index = -1;
//
//        int id = Integer.parseInt(partIdTxt.getText());
//        for(Part part : Inventory.getAllParts()){
//            index++;
//            if (part.getId() == id){
//                break;
//            }
//        }
//        String name = partNameTxt.getText();
//        double price = Double.parseDouble(partPriceCostTxt.getText());
//        int stock = Integer.parseInt(partInvTxt.getText());
//        int min = Integer.parseInt(partMinTxt.getText());
//        int max = Integer.parseInt(partMaxTxt.getText());
//        
//        if (modifyPartInHouseRBtn.isSelected()){
//            try {
//               int machineId = Integer.parseInt(partCompanyNameTxt.getText());
//               Inventory.updatePart(index, new InHouse(machineId, id, name, price, stock, min, max));
//               stage = (Stage)((Button)event.getSource()).getScene().getWindow();
//               scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
//               stage.setScene(new Scene(scene));
//               stage.show(); 
//            } catch (NumberFormatException e){
//               return; 
//            } 
//        }
//        if (modifyPartOutsourcedRBtn.isSelected()){
//            try {
//                int companyName = Integer.parseInt(partCompanyNameTxt.getText());
//            } catch (NumberFormatException e){
//               String companyName = partCompanyNameTxt.getText();
//               Inventory.updatePart(index,new Outsourced(companyName, id, name, price, stock, min, max));
//               stage = (Stage)((Button)event.getSource()).getScene().getWindow();
//               scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
//               stage.setScene(new Scene(scene));
//               stage.show();
//            }
//        }
//        
    }
    
    public void sendInHousePart(InHouse part){
        partIdTxt.setText(String.valueOf(part.getId()));
        partNameTxt.setText(part.getName());
        partInvTxt.setText(String.valueOf(part.getStock()));
        partPriceCostTxt.setText(String.valueOf(part.getPrice()));
        partMaxTxt.setText(String.valueOf(part.getMax()));
        partMinTxt.setText(String.valueOf(part.getMin()));
        partCompanyNameTxt.setText(String.valueOf(part.getMachineId()));
        modifyPartInHouseRBtn.setSelected(true);
        companyNameLabel.setVisible(false);

    }
    
    public void sendOutsourcedPart(Outsourced part){
        partIdTxt.setText(String.valueOf(part.getId()));
        partNameTxt.setText(part.getName());
        partInvTxt.setText(String.valueOf(part.getStock()));
        partPriceCostTxt.setText(String.valueOf(part.getPrice()));
        partMaxTxt.setText(String.valueOf(part.getMax()));
        partMinTxt.setText(String.valueOf(part.getMin()));   
        partCompanyNameTxt.setText(part.getCompanyName());
        modifyPartOutsourcedRBtn.setSelected(true);
        machineIdLbl.setVisible(false);
        companyNameLabel.setVisible(true);
        companyNameLabel.setTranslateX(-12);
        companyNameLabel.setPrefWidth(400);

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
        
    }    
}
