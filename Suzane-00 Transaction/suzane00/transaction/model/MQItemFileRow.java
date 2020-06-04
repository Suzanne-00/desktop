/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.transaction.model;

import javafx.beans.property.SimpleDoubleProperty;
import suzane00.transaction.model.ItemFileRow;

/**
 *
 * @author Usere
 */
public class MQItemFileRow extends ItemFileRow {
    private SimpleDoubleProperty v_originalQuantityProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty v_existingQuantityProperty = new SimpleDoubleProperty();
    private SimpleDoubleProperty v_otherQuantityProperty = new SimpleDoubleProperty();
    
    public MQItemFileRow() {
        
    }
    
    public MQItemFileRow(ItemFileRow _row) {
        setItemFileRowData(_row);
    }
    
    public void setItemFileRowData(ItemFileRow _row) {
        setId(_row.getId());
        setUnit(_row.getUnit());
        setCode(_row.getCode());
        setDiscounts(_row.getDiscounts());
        setDiscount(_row.getDiscount());
        setQuantity(_row.getQuantity());
        setPrice(_row.getPrice());
        setTotal(_row.getTotal());
        setName(_row.getName());
    }
    
    public SimpleDoubleProperty originalQuantityProperty() {
        return v_originalQuantityProperty;
    }
    
    public double getOriginalQuantity() {
       return v_originalQuantityProperty.getValue();
    }
    
    public void setOriginalQuantity(double _qty) {
        v_originalQuantityProperty.setValue(_qty);
    }
    
    public SimpleDoubleProperty existingQuantityProperty() {
        return v_existingQuantityProperty;
    }
    
    public double getExistingQuantity() {
       return v_existingQuantityProperty.getValue();
    }
    
    public void setExistingQuantity(double _qty) {
        v_existingQuantityProperty.setValue(_qty);
    }
    
    public SimpleDoubleProperty otherQuantityProperty() {
        return v_otherQuantityProperty;
    }
    
    public double getOtherQuantity() {
       return v_otherQuantityProperty.getValue();
    }
    
    public void setOtherQuantity(double _qty) {
        v_otherQuantityProperty.setValue(_qty);
    }
}
