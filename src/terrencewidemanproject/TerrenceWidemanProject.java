/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terrencewidemanproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

/**
 *
 * @author lisakim
 */
public class TerrenceWidemanProject extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        InHouse inHouse1 = new InHouse(67500, 1, "socket", 80.99, 15, 0, 50);
        InHouse inHouse2 = new InHouse(67501, 2, "air filter", 25.99, 75, 0, 200);
        InHouse inHouse3 = new InHouse(67502, 3, "battery", 115.99, 30, 0, 100);
        Outsourced outsourced1 = new Outsourced("XenoGear", 4, "cold-air intake", 99.99, 10, 0, 50);
        Outsourced outsourced2 = new Outsourced("New Life Racing", 5, "struts", 599.99, 2, 0, 10); 
        
        Inventory.addPart(inHouse1);
        Inventory.addPart(inHouse2);
        Inventory.addPart(inHouse3);
        Inventory.addPart(outsourced1);
        Inventory.addPart(outsourced2);
        
        Product product1 = new Product(101, "sprocket", 2.99, 1000, 0, 3000);
        product1.addAssociatedPart(inHouse1);
        product1.addAssociatedPart(inHouse2);

        Product product2 = new Product(102, "nut", 1.99, 100, 0, 5000);
        product2.addAssociatedPart(inHouse1);
        product2.addAssociatedPart(outsourced2);

        Product product3 = new Product(103, "bolt", 0.99, 1000, 0, 3000);
        product3.addAssociatedPart(outsourced2);
        
        Product product4 = new Product(104, "fastener", 1.99, 750, 0, 3000);
        product4.addAssociatedPart(inHouse1);
        product4.addAssociatedPart(inHouse2);
        product4.addAssociatedPart(inHouse3);
        
        Product product5 = new Product(105, "emblem", 10.99, 100, 0, 500);
        
        product5.addAssociatedPart(inHouse3);
        product5.addAssociatedPart(outsourced2);
        
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);
        Inventory.addProduct(product4);
        Inventory.addProduct(product5);
        
        launch(args);
    }
    
}
