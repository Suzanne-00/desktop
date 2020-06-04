/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.view;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardViewHeaderArea;
import suzane00.inventory.Brand;
import suzane00.inventory.Item;
import suzane00.inventory.ProductType;
import suzane00.inventory.Unit;
/**
 *
 * @author Usere
 */
public class ViewItem extends VBox implements Observable {

    protected LinkedList<Observer> m_observers;
    
    /*----------------------------------------------- GUI COMPONENTS ---------------------------------------------- */

    /* level 1 area */
    
    protected StandardViewHeaderArea area_header;
    protected TableView<Item> tbl_item;
    
    /* level 2 area */
    
    // area_header
    protected GridPane area_upperHeader;
    
    
    /* level 3 area */
    
    // area_header -> area_upperHeader
    protected Label lbl_name;
    protected Label lbl_code;
    protected Label lbl_brand;
    protected Label lbl_type;
    protected Label lbl_unit;
    protected Label lbl_note;
    protected TextField txt_name;
    protected TextField txt_code;
    protected TextField txt_note;
    protected ComboBox cmb_brand;
    protected ComboBox cmb_type;
    protected ComboBox cmb_unit;
    
    /*----------------------------------------------- END GUI COMPONENTS ---------------------------------------------- */
    
    
    public ViewItem() {
        setAppearance();
        setData();
        setActionPerformed();
    }
    
    public ObservableList<Item> getSelectedItems() {
        return  tbl_item.getSelectionModel().getSelectedItems();
    }
    
    public TableView getTable() {
        return tbl_item;
    }
    
    @Override
    public void addObserver(Observer _o) {
        if( m_observers == null)
            m_observers = new LinkedList<>();
        
        m_observers.add(_o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : m_observers) {
            observer.update(this, tbl_item.getSelectionModel().getSelectedItem());
        }
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        configureItemTable();
        loadBrandData();
        loadUnitData();
        loadProductTypeData();
    }
    
    protected void setActionPerformed() {
        area_header.setButtonSearchActionPerformed(
                e -> {
                    String name = txt_name.getText();
                    String code = txt_code.getText();
                    String note = txt_note.getText();
                    Brand brand = (Brand) cmb_brand.getValue();
                    ProductType type = (ProductType) cmb_type.getValue();
                    Unit unit = (Unit) cmb_unit.getValue();
                    
                    tbl_item.setItems(FXCollections.observableArrayList(Item.getItemsByAttributes(name, code, note, brand, unit, type)));
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setHeaderArea();
        tbl_item = new TableView<>();
        tbl_item.setPrefWidth(Utility.STANDARD_TABLE_WIDTH);
        tbl_item.setPrefHeight(Utility.STANDARD_TABLE_HEIGHT);
        VBox.setVgrow(tbl_item, Priority.ALWAYS);
        setSpacing(Utility.STANDARD_GAP);
        getChildren().addAll(area_header, tbl_item);
        setBackground();
    }
    
    /* level 1 area */
    
    protected void setHeaderArea() {
        setUpperHeaderArea();
        area_header = new StandardViewHeaderArea();
        area_header.setContent(area_upperHeader);
        
    }
    
    /* level 2 area */
    
    // setHeaderArea() -> setUpperHeaderArea()
    protected void setUpperHeaderArea() {
        lbl_name = new Label("Name: ");
        lbl_code = new Label("Code: ");
        lbl_brand = new Label("Brand: ");
        lbl_type = new Label("Type: ");
        lbl_unit = new Label("Unit: ");
        lbl_note = new Label("Note: ");
        txt_name = new TextField();
        txt_code = new TextField();
        txt_note = new TextField();
        cmb_brand = new ComboBox();
        cmb_type = new ComboBox();
        cmb_unit = new ComboBox();
        area_upperHeader = new GridPane();
        
        txt_name.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_code.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_note.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_type.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_unit.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_brand.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_upperHeader.setHgap(Utility.SHORT_GAP);
        area_upperHeader.setVgap(Utility.SHORT_GAP);
        
        area_upperHeader.add(lbl_name, 0, 0);
        area_upperHeader.add(txt_name, 1, 0);
        area_upperHeader.add(lbl_brand, 2, 0);
        area_upperHeader.add(cmb_brand, 3, 0);
        area_upperHeader.add(lbl_code, 0, 1);
        area_upperHeader.add(txt_code, 1, 1);
        area_upperHeader.add(lbl_type, 2, 1);
        area_upperHeader.add(cmb_type, 3, 1);
        area_upperHeader.add(lbl_note, 0, 2);
        area_upperHeader.add(txt_note, 1, 2);
        area_upperHeader.add(lbl_unit, 2, 2);
        area_upperHeader.add(cmb_unit, 3, 2);
    }
    
    protected void setBackground() {
        area_header.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        area_upperHeader.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */

    
    protected void configureItemTable() {
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
 
        TableColumn codeCol = new TableColumn("Code");
        codeCol.setMinWidth(100);
        codeCol.setCellValueFactory(
                new PropertyValueFactory<>("code"));
        
        TableColumn brandCol = new TableColumn("Brand");
        brandCol.setMinWidth(100);
        brandCol.setCellValueFactory(
                new PropertyValueFactory<>("brand"));
 
        TableColumn noteCol = new TableColumn("Note");
        noteCol.setMinWidth(100);
        noteCol.setCellValueFactory(
                new PropertyValueFactory<>("note"));
 
        tbl_item.getColumns().addAll(nameCol, codeCol, brandCol, noteCol);
    }
    
    protected void loadBrandData() {
        cmb_brand.setItems(FXCollections.observableArrayList(Brand.getAllBrands()));
    }
    
    protected void loadUnitData() {
        cmb_unit.setItems(FXCollections.observableArrayList(Unit.getAllUnits()));
    }
    
    protected void loadProductTypeData() {
        
    }
}
