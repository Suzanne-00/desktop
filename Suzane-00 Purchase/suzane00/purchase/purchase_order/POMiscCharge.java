/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.PropertyObject;
import suzane00.global.Utility;
import suzane00.inventory.Cost;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "po_misc_charge")
@IdClass(POMiscChargePK.class)
public class POMiscCharge implements Serializable, PropertyObject{
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "po_id")
    private PurchaseOrder v_purchaseOrder; 
    @Transient
    private SimpleObjectProperty v_purchaseOrderProperty = new SimpleObjectProperty();
    
    @Id
    @Column(name = "name")
    private String v_name;
    @Transient
    private SimpleStringProperty v_nameProperty = new SimpleStringProperty();
    
    @Column(name = "type")
    private String v_type = Utility.TYPE_PERCENTAGE;
    @Transient 
    private SimpleStringProperty v_typeProperty = new SimpleStringProperty(Utility.TYPE_PERCENTAGE) ;
    
    @Column(name = "value")
    private double v_value ;
    @Transient 
    private SimpleDoubleProperty v_valueProperty = new SimpleDoubleProperty() ;
    
    public POMiscCharge() {
        
    }
    
    public POMiscCharge(PurchaseOrder _po, Cost _disc) {
        setPurchaseOrder(_po);
        setName(_disc.getName());
        setType(_disc.getType());
        setValue( _disc.getValue());
    }
    
    public static ObservableList<Cost> convertPOMiscChargesToCosts(List<POMiscCharge> _miscs) {
        ObservableList<Cost> costs = FXCollections.observableArrayList();
        for (POMiscCharge _misc : _miscs) {
            Cost charge = new Cost(_misc.getName(), _misc.getType(), _misc.getValue());
            costs.add(charge);
        }
        return costs;
    }

    public PurchaseOrder getPurchaseOrder() {
        return v_purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder _po) {
        this.v_purchaseOrder = _po;
        v_purchaseOrderProperty.setValue(v_purchaseOrder);
    }
    
    public SimpleObjectProperty purchaseOrderProperty() {
        return v_purchaseOrderProperty;
    }
    
    public String getName() {
        return v_name;
    }
    
    public void setName(String _name) {
        this.v_name = _name;
        v_nameProperty.setValue(_name);
    }
    
    public SimpleStringProperty nameProperty() {
        return v_nameProperty;
    }
    
    public String getType() {
        return v_type;
    }
    
    public void setType(String _type) {
        this.v_type = _type;
        v_typeProperty.setValue(_type);
    }
    
    public SimpleStringProperty typeProperty() {
        return v_typeProperty;
    }
    
    public double getValue() {
        return v_value;
    }
    
    public void setValue(double _value) {
        this.v_value = _value;
        v_valueProperty.setValue(_value);
    }
    
    public SimpleDoubleProperty valueProperty() {
        return v_valueProperty;
    }
    
    @Override
    public void refreshProperty() {
        v_purchaseOrderProperty.setValue(v_purchaseOrder);
        v_nameProperty.setValue(v_name);
        v_typeProperty.setValue(v_type);
        v_valueProperty.setValue(v_value);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_purchaseOrder != null ? v_purchaseOrder.hashCode() : 0);
        hash += (v_name != null ? v_name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof POMiscCharge)) {
            return false;
        }
        POMiscCharge other = (POMiscCharge) object;
        if (v_purchaseOrder == null && other.v_purchaseOrder != null )
           return false;
        if( !(v_purchaseOrder.equals(other.v_purchaseOrder) && v_name.equals(other.v_name)))
            return false ;
        
        return true;
    }

    @Override
    public String toString() {
        return v_purchaseOrder.getTransactionNumber() + " " + v_name;
    }
}
