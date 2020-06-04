/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.model;

import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import suzane00.global.Utility;
import suzane00.inventory.Area;
import suzane00.inventory.Cost;
import suzane00.inventory.Item;
import suzane00.inventory.Unit;
import suzane00.inventory.stock.Stock;

/**
 *
 * @author Usere
 */
public class StockFileRow {

    

    private SimpleObjectProperty v_areaProperty = new SimpleObjectProperty();
    private SimpleObjectProperty v_itemProperty = new SimpleObjectProperty();
    private SimpleObjectProperty v_unitProperty = new SimpleObjectProperty();
    private SimpleDoubleProperty v_quantityProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty v_adjustedQuantityProperty = new SimpleDoubleProperty();
    
    public StockFileRow() {
        
    }
    
    public StockFileRow(Area _area, Item _item, Unit _unit, double _qty) {        
        v_areaProperty.setValue(_area);
        v_itemProperty.setValue(_item);
        v_unitProperty.setValue(_unit);
        v_quantityProperty.setValue(_qty);
    }
    
    
    public Area getArea() {
        return (Area) v_areaProperty.getValue();
    }
    
    public void setArea(Area _area) {
        v_areaProperty.setValue(_area);
    }
    
    public SimpleObjectProperty areaProperty() {
        return v_areaProperty;
    }
    
    public Item getItem() {
        return (Item) v_itemProperty.getValue();
    }
    
    public void setItem(Item _item) {
        v_itemProperty.setValue(_item);
    }
    
    public SimpleObjectProperty itemProperty() {
        return v_itemProperty;
    }
    
    public Unit getUnit() {
        return (Unit) v_unitProperty.getValue();
    }
    
    public void setUnit(Unit _unit) {
        v_unitProperty.setValue(_unit);
    }
    
    public SimpleObjectProperty unitProperty() {
        return v_unitProperty;
    }
    
    public double getQuantity() {
        return v_quantityProperty.getValue();
    }
    
    public void setQuantity(double _qty) {
        v_quantityProperty.setValue(_qty);
    }
    
    public SimpleDoubleProperty quantityProperty() {
        return v_quantityProperty;
    }
    
    public double getAdjustedQuantity() {
        return v_adjustedQuantityProperty.getValue();
    }
    
    public void setAdjustedQuantity(double _qty) {
        v_adjustedQuantityProperty.setValue(_qty);
    }
    
    public SimpleDoubleProperty adjustedQuantityProperty() {
        return v_adjustedQuantityProperty;
    }
}
