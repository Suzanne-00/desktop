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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.PropertyObject;
import suzane00.inventory.Cost;
import suzane00.inventory.PackedItem;
import suzane00.transaction.model.ItemFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "po_file")
@IdClass(POFilePK.class)
public class POFile implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "po_id")
    private PurchaseOrder v_purchaseOrder;
    @Transient
    private SimpleObjectProperty  v_purchaseOrderProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "item_id", referencedColumnName = "item_id"),
        @JoinColumn( name = "unit_id", referencedColumnName = "unit_id") })
    private PackedItem v_packedItem;
    @Transient
    private SimpleObjectProperty v_packedItemProperty = new SimpleObjectProperty();
    
    @OneToMany(mappedBy = "v_poFile", cascade = CascadeType.ALL)
    private List<POFileDiscount> v_fileDiscounts;
    @Transient
    private ObservableList<SimpleObjectProperty> v_discounts = FXCollections.observableArrayList();
    
    @Column(name = "quantity")
    double v_quantity;
    @Transient
    SimpleDoubleProperty v_quantityProperty = new SimpleDoubleProperty();

    @Column(name = "price")
    double v_price;
    @Transient
    SimpleDoubleProperty v_priceProperty = new SimpleDoubleProperty();
    
    public static ObservableList<ItemFileRow> convertPOFilesToRows(List<POFile> _files) {
        ObservableList<ItemFileRow> rows = FXCollections.observableArrayList();
        for (POFile file : _files) {
            ItemFileRow row = new ItemFileRow();
            row.setId(file.getPackedItem().getItem().getId());
            row.setCode(file.getPackedItem().getItem().getCode());
            row.setName(file.getPackedItem().getItem().getName());
            row.setUnit(file.getPackedItem().getUnit());
            row.setQuantity(file.getQuantity());
            row.setPrice(file.getPrice());
            row.setDiscounts(POFileDiscount.convertPOFileDiscountsToCosts(file.getDiscounts()));
            rows.add(row);
        }
        return rows;
    }
    
    public PurchaseOrder getPurchaseOrder() {
        return v_purchaseOrder;
    }
    
    public void setPurchaseOrder(PurchaseOrder _po) {
        v_purchaseOrder = _po;
        v_purchaseOrderProperty.setValue(v_purchaseOrder);
    }
    
    public SimpleObjectProperty purchaseOrderProperty() {
        return v_purchaseOrderProperty;
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
    
    public List<POFileDiscount> getDiscounts() {
        return FXCollections.observableArrayList(v_fileDiscounts);
    }
    
    public void setDiscounts(ObservableList<POFileDiscount> _discounts) {
        v_fileDiscounts = _discounts;
        for (POFileDiscount _discount : _discounts) {
            v_discounts.add(new SimpleObjectProperty<POFileDiscount>(_discount));
        }
    }
    
    public ObservableList<SimpleObjectProperty> discountsProperty() {
        return v_discounts;
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
    
    public double getPrice() {
        return v_price;
    }
    
    public void setPrice(double _price) {
        this.v_price = _price;
        v_priceProperty.setValue(_price);
    }
    
    public SimpleDoubleProperty priceProperty() {
        return v_priceProperty;
    }
    
    @Override
    public void refreshProperty() {
        v_purchaseOrderProperty.setValue(v_purchaseOrder);
        v_packedItemProperty.setValue(v_packedItem);
        v_priceProperty.setValue(v_price);
        v_quantityProperty.setValue(v_quantity);
        v_discounts = FXCollections.observableArrayList();
        for (POFileDiscount disc : v_fileDiscounts) {
            v_discounts.add(new SimpleObjectProperty<POFileDiscount>(disc));
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_purchaseOrder != null ? v_purchaseOrder.hashCode() : 0);
        hash += (v_packedItem != null ? v_packedItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof POFile)) {
            return false;
        }
        POFile other = (POFile) object;
        if (v_purchaseOrder == null && other.v_purchaseOrder != null )
           return false;
        if(!(v_purchaseOrder.equals(other.v_purchaseOrder) && v_packedItem.equals(other.v_packedItem)))
            return false;
        
        return true;
    }

    @Override
    public String toString() {
        return v_purchaseOrder + " " + v_packedItem;
    }
    
}
