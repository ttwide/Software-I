/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author lisakim
 */
public class Inventory {
    
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();    
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    

    
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }
    
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }
    
    public static Part lookupPart(int partId){
        ObservableList<Part> allParts = getAllParts();
        
        for (Part part : allParts){
            if (part.getId() == partId){
                return part;
            }
        }
        return null;
    }
    
    public static Product lookupProduct(int productId){
        
        ObservableList<Product> allProducts = getAllProducts();
        for (Product product : allProducts){
            if (product.getId() == productId){
                return product;
            }
        }
        return null;
    }
    
    public static ObservableList<Part> lookupPart(String partName){
        ObservableList<Part> allParts = getAllParts();
        ObservableList<Part> foundParts = FXCollections.observableArrayList();

        for (Part part : allParts){
            if (part.getName().contains(partName)){
                foundParts.add(part);
            }
        }
        return foundParts;
    }
    
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> allProducts = Inventory.getAllProducts();
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        
        for (Product product : allProducts){
            if (product.getName().contains(productName)){
                foundProducts.add(product);
            }
        }
        return foundProducts;
    }
    
    public static void updatePart(int index, Part selectedPart){
        
        getAllParts().set(index, selectedPart);
//        index = -1;
//        for (Part part : getAllParts()){
//            index++;
//            if (part.getId() == selectedPart.getId()){
//                getAllParts().set(index, part);
//            }  
//        }
    }
    
//        public boolean update(int id, Animal animal){
//        int index = -1;
//        
//        for(Animal dog : DataProvider.getAllAnimals())
//        {
//            index++;
//            
//            if (dog.getId() == id){
//                DataProvider.getAllAnimals().set(index, animal);
//                return true;
//            }
//        }
//        return false;
//    }
    
    
    public static void updateProduct(int index, Product newProduct){
        //fix me
    }
    
    public static boolean deletePart(Part selectedPart){
        ObservableList<Part> allParts = getAllParts();
        for (Part part : allParts){
            if (part.getId() == selectedPart.getId()){
                allParts.remove(part);
                return true;
            }
        }
        return false;
    }
    
    public static boolean deleteProduct(Product selectedProduct){
        ObservableList<Product> allProducts = getAllProducts();
        for (Product product : allProducts){
            if (product.getId() == selectedProduct.getId()){
                allProducts.remove(selectedProduct);
                return true;
            }
        }
        return false;
    }
    
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }
    
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

}