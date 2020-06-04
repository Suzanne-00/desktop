/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_order;

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
@Table(name = "so_file")
@IdClass(SOFilePK.class)
public class SOFile implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    
    @Id
    @ManyToOne
    @JoinColumn(name = "so_id")
    private SalesOrder v_salesOrder;
    @Transient
    private SimpleObjectProperty v_salesOrderProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "item_id", referencedColumnName = "item_id"),
        @JoinColumn( name = "unit_id", referencedColumnName = "unit_id") })
    private PackedItem v_packedItem;
    @Transient
    private SimpleObjectProperty v_packedItemProperty = new SimpleObjectProperty();
    
    @OneToMany(mappedBy = "v_file", cascade = CascadeType.ALL)
    private List<SOFileDiscount> v_discounts;
    @Transient
    private ObservableList<SimpleObjectProperty> v_discountsProperty = FXCollections.observableArrayList();
    
//    @ManyToOne
//    @JoinColumn(name = "sell_price_type_id")
//    private SellPriceType v_priceType;
    
    @Column(name = "quantity")
    double v_quantity;
    @Transient
    SimpleDoubleProperty v_quantityProperty = new SimpleDoubleProperty();

    @Column(name = "price")
    double v_price;
    @Transient
    SimpleDoubleProperty v_priceProperty = new SimpleDoubleProperty();
    
    public static ObservableList<ItemFileRow> convertSOFilesToRows(List<SOFile> _files) {
        ObservableList<ItemFileRow> rows = FXCollections.observableArrayList();
        for (SOFile file : _files) {
            ItemFileRow row = new ItemFileRow();
            row.setId(file.getPackedItem().getItem().getId());
            row.setCode(file.getPackedItem().getItem().getCode());
            row.setName(file.getPackedItem().getItem().getName());
            row.setUnit(file.getPackedItem().getUnit());
            row.setQuantity(file.getQuantity());
            row.setPrice(file.getPrice());
            row.setDiscounts(SOFileDiscount.convertSOFileDiscountsToCosts(file.getDiscounts()));
            rows.add(row);
        }
        return rows;
    }
    
    public SalesOrder getSalesOrder() {
        return v_salesOrder;
    }
    
    public void setSalesOrder(SalesOrder _so) {
        v_salesOrder = _so;
        v_salesOrderProperty.setValue(_so);
    }
    
    public SimpleObjectProperty salesOrderProperty() {
        return v_salesOrderProperty;
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
    
    public List<SOFileDiscount> getDiscounts() {
        return v_discounts;
    }
    
    public void setDiscounts(ObservableList<SOFileDiscount> _discounts) {
        v_discounts = _discounts;
        for (SOFileDiscount _discount : _discounts) {
            v_discountsProperty.add(new SimpleObjectProperty<SOFileDiscount>(_discount));
        }
    }
    
    public ObservableList<SimpleObjectProperty> discountsProperty() {
        return v_discountsProperty;
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
        v_salesOrderProperty.setValue(v_salesOrder);
        v_packedItemProperty.setValue(v_packedItem);
        v_priceProperty.setValue(v_price);
        v_quantityProperty.setValue(v_quantity);
        v_discountsProperty = FXCollections.observableArrayList();
        if(v_discounts != null) {
            for (SOFileDiscount disc : v_discounts) {
                v_discountsProperty.add(new SimpleObjectProperty<SOFileDiscount>(disc));
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_salesOrder != null ? v_salesOrder.hashCode() : 0);
        hash += (v_packedItem != null ? v_packedItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SOFile)) {
            return false;
        }
        SOFile other = (SOFile) object;
//        if (
//                (this.v_purchaseOrder == null && other.v_purchaseOrder != null) || (this.v_purchaseOrder != null && !this.v_purchaseOrder.equals(other.v_purchaseOrder)) ||
//                (this.v_packedItem == null && other.v_packedItem != null) || (this.v_packedItem != null && !this.v_packedItem.equals(other.v_packedItem))                
//            ) {
//            return false;
//        }
        if(!(v_salesOrder.equals(other.v_salesOrder) && v_packedItem.equals(other.v_packedItem)))
            return false;
        
        return true;
    }

    @Override
    public String toString() {
        return v_salesOrder + " " + v_packedItem;
    }
    
}
