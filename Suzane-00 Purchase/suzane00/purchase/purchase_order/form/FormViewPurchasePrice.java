package suzane00.purchase.purchase_order.form;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package suzanne00.purchase.form;
//
//import suzane00.source.form.*;
//import suzane00.inventory.form.*;
//import suzanne00.purchase.form.*;
//import suzane00.inventory.form.*;
//import java.util.LinkedList;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//import suzane00.global.Environment;
//import suzane00.global.FormConstant;
//import suzane00.global.Observable;
//import suzane00.global.Observer;
//import suzane00.global.Utility;
//import suzane00.global.custom_ui.StandardViewBottomArea;
//import suzane00.inventory.Area;
//import suzane00.inventory.Item;
//import suzane00.inventory.view.ViewArea;
//import suzane00.inventory.view.ViewItem;
//import suzane00.source.City;
//import suzanne00.purchase.PurchaseOrder;
//import suzanne00.purchase.view.ViewItemPurchasePrice;
//import suzanne00.purchase.view.ViewPurchaseOrder;
//import suzanne00.source.view.ViewCity;
//
///**
// *
// * @author Usere
// */
//public class FormViewPurchasePrice extends Stage implements Observable{
//    
//    protected LinkedList<Observer> v_observers;
//    
//    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
//    
//    /* root area */
//    
//    protected VBox area_main;
//    
//    /* level 1 area */
//    
//    
//    protected ViewItemPurchasePrice viewPrice = new ViewItemPurchasePrice();
//    protected StandardViewBottomArea area_bottom = new StandardViewBottomArea();
//    
//    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
//    
//    public FormViewPurchasePrice() {
//        setAppearance();
//        setActionPerformed();
//        Scene scene = new Scene(area_main);
//        setScene(scene);
//        show();
//    }
//    
//    @Override
//    public void addObserver(Observer _o) {
//        if( v_observers == null)
//            v_observers = new LinkedList<>();
//        
//        v_observers.add(_o);
//    }
//
//    @Override
//    public void notifyObservers() {
//        for (Observer observer : v_observers) {
//            observer.update(this, viewPrice.getSelectedItems());
//        }
//    }
//    
//    protected void setAppearance() {
//        area_main = new VBox(viewPrice, area_bottom);
//        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
//    }
//    
//    protected void setActionPerformed() {        
//        
//        area_bottom.btn_view.setVisible(false);
//        
//        area_bottom.setButtonEditActionPerformed(e -> {
//                    City city = viewPrice.getSelectedItems().get(0);
//                    FormCity form = new FormCity(city, FormConstant.MODE_EDIT);
//                    form.show();
//                }
//        );
//        area_bottom.setButtonDeleteActionPerformed(e -> {
//                    Environment.getEntityManager().getTransaction().begin();
//                    City.deleteCity(viewPrice.getSelectedItems().get(0));
//                    Environment.getEntityManager().getTransaction().commit();
//                    TableView tbl = viewPrice.getTable();
//                    tbl.getItems().remove(tbl.getSelectionModel().getSelectedIndex());
//                }
//        );
//        area_bottom.setButtonCloseActionPerformed(e -> close());
//    }
//}
