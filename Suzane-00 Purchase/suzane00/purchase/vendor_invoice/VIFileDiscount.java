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
import suzane00.global.Utility;
import suzane00.inventory.Cost;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "vi_file_discount")
@IdClass(VIFileDiscountPK.class)
public class VIFileDiscount implements Serializable {
    
//    @Transient
//    public static final String TYPE_PERCENTAGE = "Percentage";
//    @Transient
//    public static final String TYPE_VALUE = "Value";           
    
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "vi_id", referencedColumnName = "vi_id"),
        @JoinColumn(name = "item_id", referencedColumnName = "item_id"),
        @JoinColumn(name = "unit_id", referencedColumnName = "unit_id") })
    private VIFile v_file; 
    @Transient
    private SimpleObjectProperty v_fileProperty = new SimpleObjectProperty();
    
    @Id
    @Column(name = "name")
    private String v_name;
    @Transient
    private SimpleStringProperty v_nameProperty = new SimpleStringProperty();
    
    @Column(name = "type")
    private String v_type = Utility.TYPE_PERCENTAGE;
    @Transient 
    SimpleStringProperty v_typeProperty = new SimpleStringProperty(Utility.TYPE_PERCENTAGE) ;
    
    @Column(name = "value")
    private double v_value ;
    @Transient 
    SimpleDoubleProperty v_valueProperty = new SimpleDoubleProperty() ;
    
    public VIFileDiscount() {
        
    }
    
    public VIFileDiscount(VIFile _file, Cost _disc) {
        setFile(_file);
        setName(_disc.getName());
        setType(_disc.getType());
        setValue(_disc.getValue());
    }
    
    public static ObservableList<Cost> convertVIFileDiscountsToCosts(List<VIFileDiscount> _discs) {
        ObservableList<Cost> costs = FXCollections.observableArrayList();
        for (VIFileDiscount _disc : _discs) {
            Cost charge = new Cost(_disc.getName(), _disc.getType(), _disc.getValue());
            costs.add(charge);
        }
        return costs;
    }
    

    public VIFile getFile() {
        return v_file;
    }

    public void setFile(VIFile _file) {
        this.v_file = _file;
        v_fileProperty.setValue(v_file);
    }
    
    public String getName() {
        return v_name;
    }
    
    public SimpleObjectProperty fileProperty() {
        return v_fileProperty;
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
    
    public SimpleStringProperty discountTypeProperty() {
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
    public int hashCode() {
        int hash = 0;
        hash += (v_file != null ? v_file.hashCode() : 0);
        hash += (v_name != null ? v_name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VIFileDiscount)) {
            return false;
        }
        VIFileDiscount other = (VIFileDiscount) object;
//        if ((this.v_poFile == null && other.v_poFile != null) || (this.v_poFile != null && !this.v_poFile.equals(other.v_poFile)) ||
//            (this.v_name == null && other.v_name != null) || (this.v_name != null && !this.v_name.equals(other.v_name)) 
//           ) {
//            return false;
//        }
        if(!(v_file.equals(other.v_file) && v_name.equals(other.v_name)))
            return false ;
        
        return true;
    }

    @Override
    public String toString() {
        return v_file.getPackedItem().getItem().getName() + " " + v_name;
    }
    
}
