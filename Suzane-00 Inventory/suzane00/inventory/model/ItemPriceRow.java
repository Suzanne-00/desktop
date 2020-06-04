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
import suzane00.inventory.Item;
import suzane00.inventory.ItemPrice;
import suzane00.inventory.SellPriceType;
import suzane00.inventory.Unit;

/**
 *
 * @author Usere
 */
public class ItemPriceRow {

    private SimpleObjectProperty v_unitProperty = new SimpleObjectProperty();
    private SimpleObjectProperty v_sellPriceProperty = new SimpleObjectProperty();
    private SimpleDoubleProperty v_priceProperty = new SimpleDoubleProperty();
    
    
    public ItemPriceRow() {        
       
    }
    
    public ItemPriceRow(Unit _unit, SellPriceType _type, double _price) {        
       v_unitProperty.setValue(_unit);
       v_sellPriceProperty.setValue(_type);
       v_priceProperty.setValue(_price);
    }
    
    public static ObservableList<ItemPriceRow> findByItem(Item _item) {
        ObservableList<ItemPriceRow> rows = FXCollections.observableArrayList();
        List<ItemPrice> prices = ItemPrice.getByItem(_item);
        for (ItemPrice price : prices) {
            ItemPriceRow row = new ItemPriceRow(price.getPackedItem().getUnit(), price.getSellPriceType(), price.getPrice());
            rows.add(row);
        }
        return rows;
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
    
    public SellPriceType getSellPriceType() {
       return (SellPriceType) v_sellPriceProperty.getValue();
    }
    
    public void setSellPriceType(SellPriceType _type) {
        v_sellPriceProperty.setValue(_type);
    }
    
    public SimpleObjectProperty sellPriceTypeProperty() {
        return v_sellPriceProperty;
    }
    
     public double getPrice() {
       return v_priceProperty.getValue();
    }
    
    public void setPrice(double _price) {
        v_priceProperty.setValue(_price);
    }
    
    public SimpleDoubleProperty priceProperty() {
        return v_priceProperty;
    }
    
}
