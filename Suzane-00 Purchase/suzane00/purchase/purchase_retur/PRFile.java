/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_retur;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
import suzane00.inventory.Cost;
import suzane00.inventory.PackedItem;
import suzane00.transaction.Transaction;
import suzane00.transaction.model.ItemFileRow;
import suzane00.transaction.model.MQItemFileRow;
/**
 *
 * @author Usere
 */
@Entity
@Table(name = "pr_file")
@IdClass(PRFilePK.class)

public class PRFile implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    
    @Id
    @ManyToOne
    @JoinColumn(name = "pr_id")
    private PurchaseRetur v_purchaseRetur;
    @Transient
    private SimpleObjectProperty v_purchaseReturProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "item_id", referencedColumnName = "item_id"),
        @JoinColumn( name = "unit_id", referencedColumnName = "unit_id") })
    private PackedItem v_packedItem;
    @Transient
    private SimpleObjectProperty v_packedItemProperty = new SimpleObjectProperty();
    @Column(name = "quantity")
    double v_quantity;
    @Transient
    SimpleDoubleProperty v_quantityProperty = new SimpleDoubleProperty();
    
    
    public static ObservableList<MQItemFileRow> convertFilesToRows(List<PRFile> _files) {
        ObservableList<MQItemFileRow> rows = FXCollections.observableArrayList();
        for (PRFile file : _files) {
            MQItemFileRow row = new MQItemFileRow();
            row.setId(file.getPackedItem().getItem().getId());
            row.setCode(file.getPackedItem().getItem().getCode());
            row.setName(file.getPackedItem().getItem().getName());
            row.setUnit(file.getPackedItem().getUnit());
            row.setQuantity(file.getQuantity());
            rows.add(row);
        }
        return rows;
    }
    
    public PurchaseRetur getPurchaseRetur() {
        return v_purchaseRetur;
    }
    
    public void setPurchaseRetur(PurchaseRetur _do) {
        v_purchaseRetur = _do;
        v_purchaseReturProperty.setValue(v_purchaseRetur);
    }
    
    public SimpleObjectProperty salesReturProperty() {
        return v_purchaseReturProperty;
    }
    
    public PackedItem getPackedItem() {
        return v_packedItem;
    }
    
    public void setPackedItem(PackedItem _item) {
        v_packedItem = _item;
        v_packedItemProperty.setValue(v_packedItem);
    }
    
    public SimpleObjectProperty packedItemProperty() {
        return v_packedItemProperty;
    }
    
    public double getQuantity() {
        return v_quantity;
    }
    
    public void setQuantity(double _qty) {
        this.v_quantity = _qty;
        v_quantityProperty.setValue(_qty);
    }
    
    public SimpleDoubleProperty quantityProperty() {
       return v_quantityProperty;
    }
    
    @Override
    public void refreshProperty() {
        v_purchaseReturProperty.setValue(v_purchaseRetur);
        v_packedItemProperty.setValue(v_packedItem);
        v_quantityProperty.setValue(v_quantity);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_purchaseRetur != null ? v_purchaseRetur.hashCode() : 0);
        hash += (v_packedItem != null ? v_packedItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PRFile)) {
            return false;
        }
        PRFile other = (PRFile) object;
//        if (
//                (this.v_purchaseOrder == null && other.v_purchaseOrder != null) || (this.v_purchaseOrder != null && !this.v_purchaseOrder.equals(other.v_purchaseOrder)) ||
//                (this.v_packedItem == null && other.v_packedItem != null) || (this.v_packedItem != null && !this.v_packedItem.equals(other.v_packedItem))                
//            ) {
//            return false;
//        }
        if(!(v_purchaseRetur.equals(other.v_purchaseRetur) && v_packedItem.equals(other.v_packedItem)))
            return false;
        
        return true;
    }

    @Override
    public String toString() {
        return v_purchaseRetur + " " + v_packedItem;
    }
    
}
