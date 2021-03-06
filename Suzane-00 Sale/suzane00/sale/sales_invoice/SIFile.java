/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_invoice;

import suzane00.sale.sales_order.*;
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
@Table(name = "si_file")
@IdClass(SIFilePK.class)
@NamedQueries (
    {
        @NamedQuery(name="SIFile.getExistingQuantity",
        query="SELECT s.v_quantity FROM SIFile s " +
              "WHERE s.v_salesInvoice.v_salesOrder = :salesOrder " +
              "AND s.v_packedItem = :packedItem")
    }
)
public class SIFile implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    
    @Id
    @ManyToOne
    @JoinColumn(name = "si_id")
    private SalesInvoice v_salesInvoice;
    @Transient
    private SimpleObjectProperty v_salesInvoicerProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "item_id", referencedColumnName = "item_id"),
        @JoinColumn( name = "unit_id", referencedColumnName = "unit_id") })
    private PackedItem v_packedItem;
    @Transient
    private SimpleObjectProperty v_packedItemProperty = new SimpleObjectProperty();
    
    @OneToMany(mappedBy = "v_file", cascade = CascadeType.ALL)
    private List<SIFileDiscount> v_discounts;
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
    
    public static double getExistingQuantityFor(SalesOrder _so, ItemFileRow _row) {
        PackedItem item = Transaction.getPackedItem(_row);
        double retval = 0;
        try {
            Statement stmt = Environment.getConnection().createStatement();
            String sql = " SELECT SUM(quantity) FROM si_file" +
                         " WHERE si_id IN " + 
                                "(SELECT id FROM sales_invoice WHERE so_id =" + _so.getId() + " ) "  + 
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
//        return (Double) Environment.getEntityManager().createNamedQuery("SIFile.getExistingQuantity")
//                                   .setParameter("salesOrder", _so)
//                                   .setParameter("packedItem", item)
//                                   .getSingleResult();
    }
    
    public static ObservableList<MQItemFileRow> convertSIFilesToRows(List<SIFile> _files) {
        ObservableList<MQItemFileRow> rows = FXCollections.observableArrayList();
        for (SIFile file : _files) {
            MQItemFileRow row = new MQItemFileRow();
            row.setId(file.getPackedItem().getItem().getId());
            row.setCode(file.getPackedItem().getItem().getCode());
            row.setName(file.getPackedItem().getItem().getName());
            row.setUnit(file.getPackedItem().getUnit());
            row.setQuantity(file.getQuantity());
            row.setPrice(file.getPrice());
            row.setDiscounts(SIFileDiscount.convertSOFileDiscountsToCosts(file.getDiscounts()));
            rows.add(row);
        }
        return rows;
    }
    
    public SalesInvoice getSalesInvoice() {
        return v_salesInvoice;
    }
    
    public void setSalesInvoice(SalesInvoice _si) {
        v_salesInvoice = _si;
        v_salesInvoicerProperty.setValue(_si);
    }
    
    public SimpleObjectProperty salesInvoiceProperty() {
        return v_salesInvoicerProperty;
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
    
    public List<SIFileDiscount> getDiscounts() {
        return v_discounts;
    }
    
    public void setDiscounts(ObservableList<SIFileDiscount> _discounts) {
        v_discounts = _discounts;
        for (SIFileDiscount _discount : _discounts) {
            v_discountsProperty.add(new SimpleObjectProperty<SIFileDiscount>(_discount));
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
        v_salesInvoicerProperty.setValue(v_salesInvoice);
        v_packedItemProperty.setValue(v_packedItem);
        v_priceProperty.setValue(v_price);
        v_quantityProperty.setValue(v_quantity);
        v_discountsProperty = FXCollections.observableArrayList();
        if(v_discounts != null) {
            for (SIFileDiscount disc : v_discounts) {
                v_discountsProperty.add(new SimpleObjectProperty<SIFileDiscount>(disc));
            }
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_salesInvoice != null ? v_salesInvoice.hashCode() : 0);
        hash += (v_packedItem != null ? v_packedItem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SIFile)) {
            return false;
        }
        SIFile other = (SIFile) object;
//        if (
//                (this.v_purchaseOrder == null && other.v_purchaseOrder != null) || (this.v_purchaseOrder != null && !this.v_purchaseOrder.equals(other.v_purchaseOrder)) ||
//                (this.v_packedItem == null && other.v_packedItem != null) || (this.v_packedItem != null && !this.v_packedItem.equals(other.v_packedItem))                
//            ) {
//            return false;
//        }
        if(!(v_salesInvoice.equals(other.v_salesInvoice) && v_packedItem.equals(other.v_packedItem)))
            return false;
        
        return true;
    }

    @Override
    public String toString() {
        return v_salesInvoice + " " + v_packedItem;
    }
    
}
