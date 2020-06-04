/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order;

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
@Table(name = "po_file_discount")
@IdClass(POFileDiscountPK.class)
public class POFileDiscount implements Serializable {
    
//    @Transient
//    public static final String TYPE_PERCENTAGE = "Percentage";
//    @Transient
//    public static final String TYPE_VALUE = "Value";           
    
    private static final long serialVersionUID = 1L;

    public static ObservableList<Cost> convertPOFileDiscountsToCosts(List<POFileDiscount> _discs) {
        ObservableList<Cost> costs = FXCollections.observableArrayList();
        for (POFileDiscount _disc : _discs) {
            Cost charge = new Cost(_disc.getName(), _disc.getType(), _disc.getValue());
            costs.add(charge);
        }
        return costs;
    }
    
    
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "po_id", referencedColumnName = "po_id"),
        @JoinColumn(name = "item_id", referencedColumnName = "item_id"),
        @JoinColumn(name = "unit_id", referencedColumnName = "unit_id") })
    private POFile v_poFile; 
    @Transient
    private SimpleObjectProperty v_poFileProperty = new SimpleObjectProperty();
    
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
    
    public POFileDiscount() {
        
    }
    
    public POFileDiscount(POFile _file, Cost _disc) {
        setPOFile(_file);
        setName(_disc.getName());
        setType(_disc.getType());
        setValue(_disc.getValue());
    }
    

    public POFile getPoFile() {
        return v_poFile;
    }

    public void setPOFile(POFile _file) {
        this.v_poFile = _file;
        v_poFileProperty.setValue(v_poFileProperty);
    }
    
    public SimpleObjectProperty poFileProperty() {
        return v_poFileProperty;
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
        hash += (v_poFile != null ? v_poFile.hashCode() : 0);
        hash += (v_name != null ? v_name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof POFileDiscount)) {
            return false;
        }
        POFileDiscount other = (POFileDiscount) object;
//        if ((this.v_poFile == null && other.v_poFile != null) || (this.v_poFile != null && !this.v_poFile.equals(other.v_poFile)) ||
//            (this.v_name == null && other.v_name != null) || (this.v_name != null && !this.v_name.equals(other.v_name)) 
//           ) {
//            return false;
//        }
        if(!(v_poFile.equals(other.v_poFile) && v_name.equals(other.v_name)))
            return false ;
        
        return true;
    }

    @Override
    public String toString() {
        return v_poFile.getPackedItem().getItem().getName() + " " + v_name;
    }
    
}
