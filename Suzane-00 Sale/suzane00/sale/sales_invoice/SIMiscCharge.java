/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_invoice;

import suzane00.sale.sales_order.*;
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
@Table(name = "si_misc_charge")
@IdClass(SIMiscChargePK.class)
public class SIMiscCharge implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "si_id")
    private SalesInvoice v_salesInvoice;
    @Transient
    private SimpleObjectProperty v_salesInvoiceProperty = new SimpleObjectProperty();
    
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
    
    public SIMiscCharge() {
        
    }
    
    public SIMiscCharge(SalesInvoice _so, Cost _disc) {
        setSalesInvoice(_so);
        setName(_disc.getName());
        setType(_disc.getType());
        setValue(_disc.getValue());
    }
    
    public static ObservableList<Cost> convertSIMiscChargesToCosts(List<SIMiscCharge> _miscs) {
        ObservableList<Cost> costs = FXCollections.observableArrayList();
        for (SIMiscCharge _misc : _miscs) {
            Cost charge = new Cost(_misc.getName(), _misc.getType(), _misc.getValue());
            costs.add(charge);
        }
        return costs;
    }

    public SalesInvoice getSalesInvoice() {
        return v_salesInvoice;
    }

    public void setSalesInvoice(SalesInvoice _so) {
        this.v_salesInvoice = _so;
        v_salesInvoiceProperty.setValue(_so);
    }
    
    public SimpleObjectProperty salesInvoiceProperty() {
        return v_salesInvoiceProperty;
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
        v_salesInvoiceProperty.setValue(v_salesInvoice);
        v_nameProperty.setValue(v_name);
        v_typeProperty.setValue(v_type);
        v_valueProperty.setValue(v_value);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_salesInvoice != null ? v_salesInvoice.hashCode() : 0);
        hash += (v_name != null ? v_name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SIMiscCharge)) {
            return false;
        }
        SIMiscCharge other = (SIMiscCharge) object;
//        if ((this.v_purchaseOrder == null && other.v_purchaseOrder != null) || (this.v_purchaseOrder != null && !this.v_purchaseOrder.equals(other.v_purchaseOrder)) ||
//            (this.v_name == null && other.v_name != null) || (this.v_name != null && !this.v_name.equals(other.v_name)) 
//           ) {
//            return false;
//        }
        if( !(v_salesInvoice.equals(other.v_salesInvoice) && v_name.equals(other.v_name)))
            return false ;
        
        return true;
    }

    @Override
    public String toString() {
        return v_salesInvoice.getTransactionNumber() + " " + v_name;
    }
}
