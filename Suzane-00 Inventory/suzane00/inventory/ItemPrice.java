/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory;

import suzane00.inventory.SellPriceType;
import java.io.Serializable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
import suzane00.inventory.Item;
import suzane00.inventory.Item;
import suzane00.inventory.PackedItem;
import suzane00.inventory.PackedItem;
//import suzane00.transaction.model.ItemFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "item_price")
@IdClass(ItemPricePK.class)
@NamedQueries (
    {
        @NamedQuery(name="ItemPrice.findAll",
        query="SELECT i FROM ItemPrice i"),
        @NamedQuery(name="ItemPrice.findItemPrice",
        query="SELECT i FROM ItemPrice i WHERE i.v_packedItem = :packedItem AND "
                + "i.v_sellPriceType = :sellPriceType"),
        @NamedQuery(name="ItemPrice.findByPackedItem",
        query="SELECT i FROM ItemPrice i WHERE i.v_packedItem = :packedItem"),
        @NamedQuery(name="ItemPrice.findByItem",
        query="SELECT i FROM ItemPrice i WHERE i.v_packedItem.v_item = :item"),
        @NamedQuery(name="ItemPrice.findByPriceType",
        query="SELECT i FROM ItemPrice i WHERE i.v_sellPriceType = :sellPriceType")
    }
)
public class ItemPrice implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "item_id", referencedColumnName = "item_id"),
        @JoinColumn( name = "unit_id", referencedColumnName = "unit_id") })
    private PackedItem v_packedItem;
    @Transient
    private SimpleObjectProperty v_packedItemProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumn(name = "type_id")
    private SellPriceType v_sellPriceType;
    @Transient
    private SimpleObjectProperty v_sellPriceTypeProperty = new SimpleObjectProperty();
    
    @Column(name = "price")
    private double v_price;
    @Transient
    private SimpleDoubleProperty v_priceProperty = new SimpleDoubleProperty();

    public ItemPrice() {
        
    }
    
    public ItemPrice(PackedItem _item, SellPriceType _type, double _price) {
        setPackedItem(_item);
        setSellPriceType(_type);
        setPrice(_price);
    }
    
    public static ObservableList<ItemPrice> getByItem(Item _item) {
        ObservableList<ItemPrice> itemPrices = FXCollections.observableArrayList(Environment.getEntityManager()
                                                            .createNamedQuery("ItemPrice.findByItem", ItemPrice.class)
                                                            .setParameter("item", _item).getResultList());
    
        return PropertyObject.refreshProperies(itemPrices);
    }

    public static ObservableList<ItemPrice> getByPackedItem(PackedItem _item) {
        ObservableList<ItemPrice> itemPrices = FXCollections.observableArrayList(Environment.getEntityManager()
                                                            .createNamedQuery("ItemPrice.findByPackedItem", ItemPrice.class)
                                                            .setParameter("packedItem", _item)
                                                            .getResultList());
        
        return PropertyObject.refreshProperies(itemPrices);
    }

    
    public static void deleteItemPrice(ItemPrice _price) {
        Environment.getEntityManager().remove(_price);
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
    
    public SellPriceType getSellPriceType() {
        return v_sellPriceType;
    }
    
    public void setSellPriceType(SellPriceType _type) {
        v_sellPriceType = _type;
        v_sellPriceTypeProperty.setValue(v_sellPriceType);
    }
    
    public SimpleObjectProperty sellPriceTypeProperty() {
        return v_sellPriceTypeProperty;
    }
    
    public double getPrice() {
        return v_price;
    }
    
    public void setPrice(double _price) {
        v_price = _price;
        v_priceProperty.setValue(v_price);
    }
    
    public SimpleDoubleProperty priceProperty() {
        return v_priceProperty;
    }
    
    @Override
    public void refreshProperty() {
        v_packedItemProperty.setValue(v_packedItem);
        v_sellPriceTypeProperty.setValue(v_sellPriceType);
        v_priceProperty.setValue(v_price);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_packedItem.getItem().getId() != null && v_packedItem.getUnit().getId() != null ? 
                v_packedItem.hashCode() : 0);
        hash += (v_sellPriceType.getId() != null ? v_sellPriceType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemPrice)) {
            return false;
        }
        ItemPrice other = (ItemPrice) object;
        if (
                !(v_packedItem.equals(other.v_packedItem) && v_sellPriceType.equals(other.v_sellPriceType))
//                (
//                    (v_packedItem.getItem().getId() == null && other.getPackedItem().getItem().getId()!= null) ||
//                    (v_packedItem.getUnit().getId() == null && other.getPackedItem().getUnit().getId()!= null) || 
//                    (v_sellPriceType.getId() == null && other.getSellPriceType().getId()!= null)
//                ) ||
//                (
//                    (v_packedItem.getItem().getId()!= null && !v_packedItem.getItem().getId().equals(other.getPackedItem().getItem().getId())) || 
//                    (v_packedItem.getUnit().getId()!= null && !v_packedItem.getUnit().getId().equals(other.getPackedItem().getUnit().getId())) || 
//                    (v_sellPriceType.getId() != null && !v_sellPriceType.getId().equals(other.getSellPriceType().getId())) 
//                )
            ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return v_packedItem + " " + v_sellPriceType;
    }
    
}
