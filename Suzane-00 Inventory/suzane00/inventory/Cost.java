/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.PropertyObject;
import suzane00.global.Utility;
/**
 *
 * @author Usere
 */
public class Cost implements Serializable, PropertyObject {
    
    
    private static final long serialVersionUID = 1L;

    private String v_name;
    private SimpleStringProperty v_nameProperty = new SimpleStringProperty();
    
    private String v_type = Utility.TYPE_PERCENTAGE;
    private SimpleStringProperty v_typeProperty = new SimpleStringProperty();
    
    private Double v_value ;
    private SimpleDoubleProperty v_valueProperty = new SimpleDoubleProperty();
    
    public Cost() {
        
    }
    
    public Cost( String _name, String _type, double _value) {
        setName(_name);
        setType(_type);
        setValue(_value);
    }
    
    public static Double getTotalCostsValue(List<Cost> _discounts, Double _amount) {
        double totalDiscount = 0;
        
        if ( _discounts == null || _discounts.size() == 0)
            return new Double(0);
        
        for (Cost _disc : _discounts) {
            if (_disc.getType().equals(Utility.TYPE_PERCENTAGE)) {
                totalDiscount += _amount * _disc.getValue() * 0.01;
            } else {
                totalDiscount += _disc.getValue();
            }
        }
        return totalDiscount;
    }

    public static Double getTotalDiscountedPrice(List<Cost> _discounts, Double _amount) {
        return _amount - getTotalCostsValue(_discounts, _amount);
    }
    
    public static Double getTotalAddedPrice(List<Cost> _charges, Double _amount) {
        return _amount - getTotalCostsValue(_charges, _amount);
    }
        
    public String getName() {
        return v_name;
    }
    
    public void setName(String _name) {
        this.v_name = _name;
        v_nameProperty.setValue(v_name);
    }
    
    public SimpleStringProperty nameProperty() {
       return v_nameProperty;
    }
    
    
    public String getType() {
        return v_type;
    }
    
    public void setType(String _type) {
        this.v_type = _type;
        v_typeProperty.setValue(v_type);
    }
    
    public SimpleStringProperty typeProperty() {
        return v_typeProperty;
    }
    
    
    public Double getValue() {
        return v_value;
    }
    
    public void setValue(Double _value) {
        this.v_value = _value;
        v_valueProperty.setValue(v_value);
    }
    
    public SimpleDoubleProperty valueProperty() {
       return v_valueProperty;
    }
    
    @Override
    public void refreshProperty() {
        v_typeProperty.setValue(v_type);
        v_nameProperty.setValue(v_name);
        v_valueProperty.setValue(v_value);
    }
    
    @Override
    public String toString() {
        return v_name;
    }
}
