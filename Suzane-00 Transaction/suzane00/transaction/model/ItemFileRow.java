/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.transaction.model;

import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import suzane00.global.Utility;
import suzane00.inventory.Cost;
import suzane00.inventory.Unit;

/**
 *
 * @author Usere
 */
public class ItemFileRow {

    private SimpleLongProperty v_idProperty = new SimpleLongProperty();
    private SimpleDoubleProperty v_quantityProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty v_priceProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty v_discountProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty v_totalProperty = new SimpleDoubleProperty();
    private SimpleStringProperty v_codeProperty = new SimpleStringProperty();
    private SimpleStringProperty v_nameProperty = new SimpleStringProperty();
    private SimpleObjectProperty v_unitProperty = new SimpleObjectProperty();
    private SimpleObjectProperty v_discountsProperty = new SimpleObjectProperty(); 
    
    public ItemFileRow() {        
        v_quantityProperty.addListener(
                (Observable o) -> calculateTotalAndDiscount()
        );
        
        v_priceProperty.addListener(
                (Observable o) -> calculateTotalAndDiscount()
        );
    }
    
    
    
    public long getId() {
      return v_idProperty.getValue();
    }
    
    public void setId(long _id) {
       v_idProperty.setValue(_id);
    }
    
    public SimpleLongProperty idProperty() {
        return v_idProperty;
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
    
    public double getPrice() {
       return v_priceProperty.getValue();
    }
    
    public void setPrice(double _price) {
        v_priceProperty.setValue(_price);
    }
    
    public SimpleDoubleProperty priceProperty() {
        return v_priceProperty;
    }
    
    public double getDiscount() {
        return v_discountProperty.getValue();
    }
    
    public void setDiscount(double _disc) {
       v_discountProperty.setValue(_disc);
    }
    
    public SimpleDoubleProperty discountProperty() {
        return v_discountProperty;
    }
    
    public double getTotal() {
        return v_totalProperty.getValue();
    }
    
    public void setTotal(double _total) {
       v_totalProperty.setValue(_total);
    }
    
    public SimpleDoubleProperty totalProperty() {
        return v_totalProperty;
    }
    
    public String getCode() {
        return v_codeProperty.getValue();
    }
    
    public void setCode(String _code) {
       v_codeProperty.setValue(_code);
    }
    
    public SimpleStringProperty codeProperty() {
        return v_codeProperty;
    }
    
    public String getName() {
       return v_nameProperty.getValue();
    }
    
    public void setName(String _name) {
        v_nameProperty.setValue(_name);
    }
    
    public SimpleStringProperty nameProperty() {
        return v_nameProperty;
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
    
    public ObservableList<Cost> getDiscounts() {
       return (ObservableList<Cost>) v_discountsProperty.getValue();
    }
    
    public void setDiscounts(ObservableList<Cost> _discounts) {
        v_discountsProperty.setValue(_discounts);
        calculateTotalAndDiscount();
        
        if (_discounts == null)
            return ;
        
        _discounts.addListener(
                (Observable obs) -> {
                    calculateTotalAndDiscount();
                }
        );
    }
    
    public SimpleObjectProperty discountsProperty() {
        return v_discountsProperty;
    }
    
    private void calculateTotalAndDiscount() {
        double discount = 0;
        double total = v_quantityProperty.getValue() * v_priceProperty.getValue();
        if ( v_discountsProperty.getValue() != null) {
            for (Cost disc : (ObservableList<Cost>)v_discountsProperty.getValue()) {
                if(disc.getType().equals(Utility.TYPE_PERCENTAGE))
                    discount += disc.getValue() * 0.01 * total;
                else discount += disc.getValue();
            }
        }
        v_discountProperty.setValue(discount);
        v_totalProperty.setValue(total - discount);
    } 
}
