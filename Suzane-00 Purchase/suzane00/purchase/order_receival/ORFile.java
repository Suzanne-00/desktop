/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.order_receival;

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
import suzane00.purchase.purchase_order.PurchaseOrder;
import suzane00.transaction.Transaction;
import suzane00.transaction.model.ItemFileRow;
import suzane00.transaction.model.MQItemFileRow;
/**
 *
 * @author Usere
 */
@Entity
@Table(name = "or_file")
@IdClass(ORFilePK.class)

public class ORFile implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    
    @Id
    @ManyToOne
    @JoinColumn(name = "or_id")
    private OrderReceival v_orderReceival;
    @Transient
    private SimpleObjectProperty v_orderReceivalProperty = new SimpleObjectProperty();
    
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
    
    public static double getExistingQuantityFor(PurchaseOrder _po, ItemFileRow _row) {
        PackedItem item = Transaction.getPackedItem(_row);
        double retval = 0;
        try {
            Statement stmt = Environment.getConnection().createStatement();
            String sql = " SELECT SUM(quantity) FROM or_file" +
                         " WHERE or_id IN " + 
                                "(SELECT id FROM order_receival WHERE po_id =" + _po.getId() + " ) "  + 
                         " AND item_id = " + item.getItem().getId() +
                         " AND unit_id = " + item.getUnit().getId();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            retval = rs.getDouble(1);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        
        return retval;
    }
    
    public static ObservableList<MQItemFileRow> convertFilesToRows(List<ORFile> _files) {
        ObservableList<MQItemFileRow> rows = FXCollections.observableArrayList();
        for (ORFile file : _files) {
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
    
    public OrderReceival getOrderReceival() {
        return v_orderReceival;
    }
    
    public void setOrderReceival(OrderReceival _or) {
        v_orderReceival = _or;
        v_orderReceivalProperty.setValue(v_orderReceival);
    }
    
    public SimpleObjectProperty orderReceivalProperty() {
        return v_orderReceivalProperty;
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
    
    public SimpleDoubleProperty nameProperty() {
       return v_quantityProperty;
    }
    
    @Override
    public void refreshProperty() {
        v_orderReceivalProperty.setValue(v_orderReceival);
        v_packedItemProperty.setValue(v_packedItem);
        v_quantityProperty.setValue(v_quantity);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_orderReceival != null ? v_orderReceival.hashCode() : 0);
        hash += (v_packedItem != null ? v_packedItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ORFile)) {
            return false;
        }
        ORFile other = (ORFile) object;
//        if (
//                (this.v_purchaseOrder == null && other.v_purchaseOrder != null) || (this.v_purchaseOrder != null && !this.v_purchaseOrder.equals(other.v_purchaseOrder)) ||
//                (this.v_packedItem == null && other.v_packedItem != null) || (this.v_packedItem != null && !this.v_packedItem.equals(other.v_packedItem))                
//            ) {
//            return false;
//        }
        if(!(v_orderReceival.equals(other.v_orderReceival) && v_packedItem.equals(other.v_packedItem)))
            return false;
        
        return true;
    }

    @Override
    public String toString() {
        return v_orderReceival + " " + v_packedItem;
    }
    
}
