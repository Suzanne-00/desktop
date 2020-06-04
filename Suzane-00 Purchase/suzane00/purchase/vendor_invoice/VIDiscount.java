/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.vendor_invoice;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javax.persistence.JoinColumns;
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
@Table(name = "vi_discount")
@IdClass(VIDiscountPK.class)
public class VIDiscount implements Serializable, PropertyObject {
    
//    @Transient
//    public static final String TYPE_PERCENTAGE = "Percentage";
//    @Transient
//    public static final String TYPE_VALUE = "Value";           
    
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "vi_id")
    private VendorInvoice v_vendorInvoice; 
    @Transient
    private SimpleObjectProperty v_vendorInvoiceProperty = new SimpleObjectProperty();
    
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
    
    public VIDiscount() {
        
    }
    
    public VIDiscount(VendorInvoice _so, Cost _disc) {
        setVendorInvoice(_so);
        setName(_disc.getName());
        setType(_disc.getType());
        setValue(_disc.getValue());
    }
    
    public static ObservableList<Cost> convertDiscountsToCosts(List<VIDiscount> _discs) {
        ObservableList<Cost> costs = FXCollections.observableArrayList();
        for (VIDiscount _disc : _discs) {
            Cost charge = new Cost(_disc.getName(), _disc.getType(), _disc.getValue());
            costs.add(charge);
        }
        return costs;
    }

    public VendorInvoice getVendorInvoice() {
        return v_vendorInvoice;
    }

    public void setVendorInvoice(VendorInvoice _so) {
        this.v_vendorInvoice = _so;
        v_vendorInvoiceProperty.setValue(_so);
    }
    
    public SimpleObjectProperty vendorInvoiceProperty() {
        return v_vendorInvoiceProperty;
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
        v_vendorInvoiceProperty.setValue(v_vendorInvoice);
        v_nameProperty.setValue(v_name);
        v_typeProperty.setValue(v_type);
        v_valueProperty.setValue(v_value);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_vendorInvoice != null ? v_vendorInvoice.hashCode() : 0);
        hash += (v_name != null ? v_name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VIDiscount)) {
            return false;
        }
        VIDiscount other = (VIDiscount) object;
//        if ((this.v_purchaseOrder == null && other.v_purchaseOrder != null) || (this.v_purchaseOrder != null && !this.v_purchaseOrder.equals(other.v_purchaseOrder)) ||
//            (this.v_name == null && other.v_name != null) || (this.v_name != null && !this.v_name.equals(other.v_name)) 
//           ) {
//            return false;
//        }
        
        if( !(v_vendorInvoice.equals(other.v_vendorInvoice) && v_name.equals(other.v_name)))
            return false ;
        
        return true;
    }

    @Override
    public String toString() {
        return v_vendorInvoice.getTransactionNumber() + " " + v_name;
    }
    
}
