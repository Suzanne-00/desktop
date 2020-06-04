/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order.view;

import suzane00.inventory.view.*;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import java.util.LinkedList;
import java.util.List;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardViewHeaderArea;
import suzane00.inventory.Area;
import suzane00.inventory.Brand;
import suzane00.inventory.Item;
import suzane00.inventory.PackedItem;
import suzane00.inventory.ProductType;
import suzane00.inventory.Unit;
import suzane00.inventory.custom_ui.NumberTextField;
import suzane00.purchase.purchase_order.ItemPurchasePrice;
import suzane00.source.Supplier;

/**
 *
 * @author Usere
 */
public class ViewItemPurchasePrice extends VBox implements Observable {

    protected LinkedList<Observer> m_observers;
    
    /*----------------------------------------------- GUI COMPONENTS ---------------------------------------------- */

    /* level 1 area */
    
    protected StandardViewHeaderArea area_header;
    protected TableView<ItemPurchasePrice> tbl_price;
    
    /*level 2 area */
    
    // area_header
    protected GridPane area_upperHeader;
    
    /* level 3 area */
    
    // area_header -> area_upperHeader
    protected Label lbl_supplier;
    protected Label lbl_item;
    protected Label lbl_unit;
    protected Label lbl_price;
    protected Label lbl_priceTo;
    protected NumberTextField ntxt_min;
    protected NumberTextField ntxt_max;
    protected ComboBox cmb_supplier;
    protected ComboBox<Item> cmb_item;
    protected ComboBox cmb_unit;
  
    
    /*----------------------------------------------- END GUI COMPONENTS ---------------------------------------------- */
    
    
    public ViewItemPurchasePrice() {
        setAppearance();
        setData();
        setActionPerformed();
    }
    
    public ObservableList<ItemPurchasePrice> getSelectedItems() {
        return tbl_price.getSelectionModel().getSelectedItems();
    }
    
    public TableView getTable() {
        return tbl_price;
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
            observer.update(this, tbl_price.getSelectionModel().getSelectedItem());
        }
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        configureItemTable();
        loadSupplierData();
        loadItemData();
    }
    
    protected void setActionPerformed() {
        area_header.setButtonSearchActionPerformed(e -> {
                    Supplier supplier = (Supplier) cmb_supplier.getValue();
                    PackedItem pItem = PackedItem.getPackedItem
                        ((Item) cmb_item.getValue(), (Unit) cmb_unit.getValue());
                    double min = ntxt_min.getValue();
                    double max = ntxt_max.getValue();
                    
                    tbl_price.setItems(FXCollections.observableArrayList(
                            ItemPurchasePrice.getIPPByAttributes(supplier, pItem, min, max)));
                }
        );
        
        cmb_item.valueProperty().addListener(
            (ObservableValue<? extends Item> observable, Item oldValue, Item newValue) -> {
                cmb_unit.setItems(Unit.getUnitsByItemId((Item) cmb_item.getValue()));
            }
        );
    }
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setHeaderArea();
        tbl_price = new TableView<>();
        tbl_price.setPrefWidth(Utility.STANDARD_TABLE_WIDTH);
        tbl_price.setPrefHeight(Utility.STANDARD_TABLE_HEIGHT);
        setSpacing(Utility.STANDARD_GAP);
        getChildren().addAll(area_header, tbl_price);
    }
    
    /* level 1 area */
    
    protected void setHeaderArea() {
        setUpperHeaderArea();
        area_header = new StandardViewHeaderArea();
        area_header.setContent(area_upperHeader);
    }
    
    /* level 2 area */
    
    // setHeaderArea()
    protected void setUpperHeaderArea() {
        lbl_supplier = new Label("Supplier: ");
        lbl_item = new Label("Item: ");
        lbl_unit = new Label("Unit: ");
        lbl_price = new Label("Price: ");
        lbl_priceTo = new Label(" to ");
        ntxt_min = new NumberTextField();
        ntxt_max = new NumberTextField();
        cmb_supplier = new ComboBox();
        cmb_item = new ComboBox<>();
        cmb_unit = new ComboBox();
        area_upperHeader = new GridPane();
        
        ntxt_min.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        ntxt_max.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_supplier.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_item.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_unit.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_upperHeader.setHgap(Utility.SHORT_GAP);
        area_upperHeader.setVgap(Utility.SHORT_GAP);
        
        area_upperHeader.addColumn(0, lbl_supplier);
        area_upperHeader.addColumn(0, lbl_item);
        area_upperHeader.addColumn(0, lbl_unit);
        area_upperHeader.addColumn(0, lbl_price);
        area_upperHeader.add(lbl_priceTo, 2, 3);
        area_upperHeader.addColumn(1, cmb_supplier);
        area_upperHeader.addColumn(1, cmb_item);
        area_upperHeader.addColumn(1, cmb_unit);
        area_upperHeader.addColumn(1, ntxt_min);
        area_upperHeader.add(ntxt_max, 3, 3);
    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */

    
    protected void configureItemTable() {
        TableColumn nameCol = new TableColumn("Supplier");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("supplier"));
        
        TableColumn countryCol = new TableColumn("Packed item");
        countryCol.setMinWidth(100);
        countryCol.setCellValueFactory(
                new PropertyValueFactory<>("packedItem"));
 
        TableColumn priceCol = new TableColumn("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(
                new PropertyValueFactory<>("price"));
 
        tbl_price.getColumns().addAll(nameCol, countryCol, priceCol);
    }
    
    protected void loadSupplierData() {
        cmb_supplier.setItems(Supplier.getAllSuppliers());
    }
    
    protected void loadItemData() {
        cmb_item.setItems(Item.getAllItems());
    }
}
