/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.eclipse.persistence.exceptions.DatabaseException;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
import suzane00.global.Utility;
import suzane00.inventory.Item;
import suzane00.inventory.Item;
import suzane00.inventory.ItemPrice;
import suzane00.inventory.ItemPrice;
import suzane00.inventory.PackedItem;
import suzane00.inventory.PackedItem;
import suzane00.transaction.model.ItemFileRow;
import suzane00.source.Supplier;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
@Entity
@IdClass(ItemPurchasePricePK.class)
@Table(name = "item_purchase_price")
@NamedQueries (
    {
        @NamedQuery(name="ItemPurchasePrice.findAll",
        query="SELECT i FROM ItemPurchasePrice i"),
        @NamedQuery(name="ItemPurchasePrice.findBySupplierPackedItem",
        query="SELECT i FROM ItemPurchasePrice i WHERE i.v_supplier = :supplier AND "
                + "i.v_packedItem = :packedItem"),
        @NamedQuery(name="ItemPurchasePrice.findByPackedItem",
        query="SELECT i FROM ItemPurchasePrice i WHERE i.v_packedItem = :packedItem"),
        @NamedQuery(name="ItemPurchasePrice.findByItem",
        query="SELECT i FROM ItemPurchasePrice i WHERE i.v_packedItem.v_item = :item")
    }
)
public class ItemPurchasePrice implements PropertyObject {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier v_supplier;
    @Transient
    private SimpleObjectProperty v_supplierProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "item_id", referencedColumnName = "item_id"),
        @JoinColumn( name = "unit_id", referencedColumnName = "unit_id") })
    private PackedItem v_packedItem;
    @Transient
    private SimpleObjectProperty v_packedItemProperty = new SimpleObjectProperty();
    
    @Id
    @Column(name = "price")
    private double v_price;
    @Transient
    SimpleDoubleProperty v_priceProperty = new SimpleDoubleProperty();
    
    ItemPurchasePrice() {
        
    }
    
    ItemPurchasePrice(Supplier _supplier, PackedItem _item, double _price) {
        setSupplier(_supplier);
        setPackedItem(_item);
        setPrice(_price);
    }
    
     public static ObservableList<ItemPurchasePrice> getIPPByAttributes(Supplier _supplier, PackedItem _item, double _min, double _max) {
        String query = "SELECT i FROM ItemPurchasePrice i WHERE i.v_supplier != NULL";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
       
        if (_supplier != null) {
            query += " AND p.v_supplier.v_id = " + _supplier.getId();
        }
        if (_item != null) {
            query += " AND p.v_packedItem.v_item.v_id = " + _item.getItem().getId();
            query += " AND p.v_packedItem.v_unit.v_id = " + _item.getUnit().getId();
        }
        if (_min >= 0 + Utility.DOUBLE_TOLERANCE) {
            query += " AND i.v_price >= " + _min;
        }
        if (_max != 0 + Utility.DOUBLE_TOLERANCE) {
            query += " AND i.v_price <= " + _max;
        }
        ObservableList<ItemPurchasePrice> itemPrices = FXCollections.observableArrayList(Environment.getEntityManager()
                            .createQuery(query, ItemPurchasePrice.class)
                            .getResultList());
        
        return PropertyObject.refreshProperies(itemPrices);
    }
    
    public static ObservableList<ItemPurchasePrice> getBySupplierPackedItems(Supplier _supplier, PackedItem _item) {
        ObservableList<ItemPurchasePrice> itemPrices =  FXCollections.observableArrayList(Environment.getEntityManager()
                                                             .createNamedQuery("ItemPurchasePrice.findBySupplierPackedItem", ItemPurchasePrice.class)
                                                             .setParameter("supplier", _supplier)
                                                             .setParameter("packedItem", _item)
                                                             .getResultList());
        
        return PropertyObject.refreshProperies(itemPrices);
    }
    
    public static ObservableList<ItemPurchasePrice> getByItem(Item _item) {
        ObservableList<ItemPurchasePrice> itemPrices = FXCollections.observableArrayList(Environment.getEntityManager()
                            .createNamedQuery("ItemPurchasePrice.findByItem", ItemPurchasePrice.class)
                            .setParameter("item", _item)
                            .getResultList());
        
        return PropertyObject.refreshProperies(itemPrices);
    }

    public static ObservableList<ItemPurchasePrice> getByPackedItem(PackedItem _item) {
        ObservableList<ItemPurchasePrice> itemPrices = FXCollections.observableArrayList(Environment.getEntityManager()
                            .createNamedQuery("ItemPurchasePrice.findByPackedItem", ItemPurchasePrice.class)
                            .setParameter("packedItem", _item)
                            .getResultList());
        
        return PropertyObject.refreshProperies(itemPrices);
    }

    public static ObservableList<ItemPurchasePrice> getByPORow(ItemFileRow _row) {
        PackedItem item = Transaction.getPackedItem(_row);
        ObservableList<ItemPurchasePrice> itemPrices = FXCollections.observableArrayList(Environment.getEntityManager()
                            .createNamedQuery("ItemPurchasePrice.findByPackedItem", ItemPurchasePrice.class)
                            .setParameter("packedItem", item)
                            .getResultList());
        
        return PropertyObject.refreshProperies(itemPrices);
    }
    
    public static void saveItemPurchasePrice(Supplier _supplier, ItemFileRow _row) {
        try {
            if(_supplier == null)
                return;
            Statement stmt = Environment.getConnection().createStatement();
            String sql = "SELECT is_item_purchase_price_exist( " + _supplier.getId() + ", " +
                    _row.getId() + ", " +  _row.getUnit().getId() + 
                    ", " + _row.getPrice() + " )";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            if(rs.getBoolean(1))
                return ;
            
            ItemPurchasePrice ipp = new ItemPurchasePrice(_supplier, Transaction.getPackedItem(_row), _row.getPrice());
            Environment.getEntityManager().persist(ipp);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteItemPrice(ItemPrice _price) {
        Environment.getEntityManager().remove(_price);
    }
    
    public Supplier getSupplier() {
        return v_supplier;
    }
    
    public void setSupplier(Supplier _sup) {
        v_supplier = _sup;
        v_supplierProperty.setValue(v_supplier);
    }
    
    public SimpleObjectProperty supplierProperty() {
        return v_supplierProperty;
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
        v_supplierProperty.setValue(v_supplier);
        v_packedItemProperty.setValue(v_packedItem);
        v_priceProperty.setValue(v_price);
    }
    
}
