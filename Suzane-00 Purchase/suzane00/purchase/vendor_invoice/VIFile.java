/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.vendor_invoice;

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
@Table(name = "vi_file")
@IdClass(VIFilePK.class)
@NamedQueries (
    {
        @NamedQuery(name="VIFile.getExistingQuantity",
        query="SELECT v.v_quantity FROM VIFile v " +
              "WHERE v.v_vendorInvoice.v_purchaseOrder = :purchaseOrder " +
              "AND v.v_packedItem = :packedItem")
    }
)
public class VIFile implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    
    @Id
    @ManyToOne
    @JoinColumn(name = "vi_id")
    private VendorInvoice v_vendorInvoice;
    @Transient
    private SimpleObjectProperty v_vendorInvoicerProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "item_id", referencedColumnName = "item_id"),
        @JoinColumn( name = "unit_id", referencedColumnName = "unit_id") })
    private PackedItem v_packedItem;
    @Transient
    private SimpleObjectProperty v_packedItemProperty = new SimpleObjectProperty();
    
    @OneToMany(mappedBy = "v_file", cascade = CascadeType.ALL)
    private List<VIFileDiscount> v_discounts;
    @Transient
    private ObservableList<SimpleObjectProperty> v_discountsProperty = FXCollections.observableArrayList();
    
    
    @Column(name = "quantity")
    double v_quantity;
    @Transient
    SimpleDoubleProperty v_quantityProperty = new SimpleDoubleProperty();

    @Column(name = "price")
    double v_price;
    @Transient
    SimpleDoubleProperty v_priceProperty = new SimpleDoubleProperty();
    
    public static double getExistingQuantityFor(PurchaseOrder _po, ItemFileRow _row) {
        PackedItem item = Transaction.getPackedItem(_row);
        double retval = 0;
        try {
            Statement stmt = Environment.getConnection().createStatement();
            String sql = " SELECT SUM(quantity) FROM vi_file" +
                         " WHERE vi_id IN " + 
                                "(SELECT id FROM vendor_invoice WHERE po_id =" + _po.getId() + " ) "  + 
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
    
    public static ObservableList<MQItemFileRow> convertFilesToRows(List<VIFile> _files) {
        ObservableList<MQItemFileRow> rows = FXCollections.observableArrayList();
        for (VIFile file : _files) {
            MQItemFileRow row = new MQItemFileRow();
            row.setId(file.getPackedItem().getItem().getId());
            row.setCode(file.getPackedItem().getItem().getCode());
            row.setName(file.getPackedItem().getItem().getName());
            row.setUnit(file.getPackedItem().getUnit());
            row.setQuantity(file.getQuantity());
            row.setPrice(file.getPrice());
            row.setDiscounts(VIFileDiscount.convertVIFileDiscountsToCosts(file.getDiscounts()));
            rows.add(row);
        }
        return rows;
    }
    
    public VendorInvoice getVendorInvoice() {
        return v_vendorInvoice;
    }
    
    public void setVendorInvoice(VendorInvoice _si) {
        v_vendorInvoice = _si;
        v_vendorInvoicerProperty.setValue(_si);
    }
    
    public SimpleObjectProperty vendorInvoiceProperty() {
        return v_vendorInvoicerProperty;
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
    
    public List<VIFileDiscount> getDiscounts() {
        return v_discounts;
    }
    
    public void setDiscounts(ObservableList<VIFileDiscount> _discounts) {
        v_discounts = _discounts;
        for (VIFileDiscount _discount : _discounts) {
            v_discountsProperty.add(new SimpleObjectProperty<VIFileDiscount>(_discount));
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
        v_vendorInvoicerProperty.setValue(v_vendorInvoice);
        v_packedItemProperty.setValue(v_packedItem);
        v_priceProperty.setValue(v_price);
        v_quantityProperty.setValue(v_quantity);
        v_discountsProperty = FXCollections.observableArrayList();
        if(v_discounts != null) {
            for (VIFileDiscount disc : v_discounts) {
                v_discountsProperty.add(new SimpleObjectProperty<VIFileDiscount>(disc));
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_vendorInvoice != null ? v_vendorInvoice.hashCode() : 0);
        hash += (v_packedItem != null ? v_packedItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VIFile)) {
            return false;
        }
        VIFile other = (VIFile) object;
//        if (
//                (this.v_purchaseOrder == null && other.v_purchaseOrder != null) || (this.v_purchaseOrder != null && !this.v_purchaseOrder.equals(other.v_purchaseOrder)) ||
//                (this.v_packedItem == null && other.v_packedItem != null) || (this.v_packedItem != null && !this.v_packedItem.equals(other.v_packedItem))                
//            ) {
//            return false;
//        }
        if(!(v_vendorInvoice.equals(other.v_vendorInvoice) && v_packedItem.equals(other.v_packedItem)))
            return false;
        
        return true;
    }

    @Override
    public String toString() {
        return v_vendorInvoice + " " + v_packedItem;
    }
    
}
