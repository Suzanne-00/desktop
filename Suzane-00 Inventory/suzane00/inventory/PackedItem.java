/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "packed_item")
@IdClass(PackedItemPK.class)
@NamedQueries (
    {
        @NamedQuery(name="PackedItem.findAll",
        query="SELECT p FROM PackedItem p"),
        @NamedQuery(name="PackedItem.findPackedItem",
        query="SELECT p FROM PackedItem p WHERE p.v_item = :item AND p.v_unit = :unit"),
        @NamedQuery(name="PackedItem.findById",
        query="SELECT p FROM PackedItem p WHERE p.v_item.v_id = :item_id AND p.v_unit.v_id = :unit_id"),
        @NamedQuery(name="PackedItem.findByItemId",
        query="SELECT p FROM PackedItem p WHERE p.v_item.v_id = :item_id"),
        @NamedQuery(name="PackedItem.findByUnitId",
        query="SELECT p FROM PackedItem p WHERE p.v_unit.v_id = :item_id")
    }
)
public class PackedItem implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    @Id
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item v_item;
    @Transient
    private SimpleObjectProperty v_itemProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumn(name = "unit_id")
    private Unit v_unit;
    @Transient
    private SimpleObjectProperty v_unitProperty = new SimpleObjectProperty();
    
    @OneToMany(mappedBy = "v_packedItem", cascade = CascadeType.ALL)
    private List<ItemPrice> v_itemPrices;
    @Transient
    private ObservableList<SimpleObjectProperty> v_itemPricesProperty = FXCollections.observableArrayList();
    
    public PackedItem() {
        
    }
    
    public PackedItem(Item _item, Unit _unit) {
        setItem(_item);
        setUnit(_unit);
    }
    
    public static ObservableList<PackedItem> getAllPackedItems() {
        ObservableList<PackedItem> items = FXCollections.observableArrayList(Environment.getEntityManager()
                                                        .createNamedQuery("PackedItem.findAll", PackedItem.class)
                                                        .getResultList());
        
        return PropertyObject.refreshProperies(items);
    }
    
    public static PackedItem getPackedItem(Item _item, Unit _unit) {
        PackedItem item =  Environment.getEntityManager().createNamedQuery("PackedItem.findPackedItem", PackedItem.class)
                                             .setParameter("item", _item)
                                             .setParameter("unit", _unit)
                                             .getSingleResult();
        
        item.refreshProperty();
        return item;
    }
    
    
    public static PackedItem getPackedItemsById(PackedItemPK _id) {
        PackedItem item = Environment.getEntityManager().createNamedQuery("PackedItem.findById", PackedItem.class).setParameter("item_id", _id.getItem())
                                             .setParameter("unit_id", _id.getUnit())
                                             .getSingleResult();
        
        item.refreshProperty();
        return item;
    }
    
    public static void deletePackedItem(PackedItem _item) {
        Environment.getEntityManager().remove(_item);
    }

    public Item getItem() {
        return v_item;
    }

    public void setItem(Item _item) {
        this.v_item = _item;
        v_itemProperty.setValue(v_item);
    }
    
    public Unit getUnit() {
        return v_unit;
    }

    public void setUnit(Unit _unit) {
        this.v_unit = _unit;
        v_unitProperty.setValue(v_unit);
    }
    
    public List<ItemPrice> getItemPrices() {
        return v_itemPrices;
    }

    public void setSellPrices(ObservableList<ItemPrice> _prices) {
        this.v_itemPrices = _prices;
        for (ItemPrice _price : _prices) {
            v_itemPricesProperty.add(new SimpleObjectProperty<ItemPrice>(_price));
        }
    }
    
    @Override
    public void refreshProperty() {
        v_itemProperty.setValue(v_item);
        v_itemPricesProperty = FXCollections.observableArrayList();
        for (ItemPrice price : v_itemPrices) {
            v_itemPricesProperty.add(new SimpleObjectProperty<ItemPrice>(price));
        }
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_item.getId() != null ? v_item.hashCode() : 0);
        hash += (v_unit.getId() != null ? v_unit.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PackedItem)) {
            return false;
        }
        PackedItem other = (PackedItem) object;
        if (
                !(v_item.equals(other.v_item) && v_unit.equals(other.v_unit))
//                (
//                    (v_item.getId() == null && other.getItem().getId() != null) || (v_unit.getId() == null && other.getUnit().getId()!= null)
//                ) ||
//                (
//                    (v_item.getId() != null && !v_item.getId().equals(other.getItem().getId())) || (v_unit.getId() != null && !v_unit.getId().equals(other.getUnit().getId())) 
//                )
            ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return v_item + " (" + v_unit + ")";
    }
    
}
