/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
//import suzane00.transaction.model.ItemFileRow;
import suzane00.inventory.model.ItemPriceRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "item")
@NamedQueries (
    {
        @NamedQuery(name="Item.findAll",
        query="SELECT i FROM Item i"),
        @NamedQuery(name="Item.findById",
        query="SELECT i FROM Item i WHERE i.v_id = :id"),
        @NamedQuery(name="Item.findByName",
        query="SELECT i FROM Item i WHERE i.v_name = :name"),
    }
)
public class Item extends Product implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private static Background v_masterBackground;
    static {
        try
        {
            InputStream is = Environment.class.getResource(
                    "/suzane00/global/resource/Blue Gradient 2.jpg").openStream();
            BackgroundImage img= new BackgroundImage(new Image(is,32,32,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0, 0, false, false, false, true));
            v_masterBackground = new Background(img);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    
    
    @Column(name = "code")
    private String v_code;
    @Transient
    private SimpleStringProperty v_codeProperty = new SimpleStringProperty();

    public static Background getMasterBackground() {
        return v_masterBackground;
    }
    
    public static ObservableList<Item> getAllItems() {
        ObservableList<Item> items = FXCollections.observableArrayList(Environment.getEntityManager()
                                                  .createNamedQuery("Item.findAll", Item.class)
                                                  .getResultList());
        
        return (ObservableList<Item>) PropertyObject.refreshProperies(items);
    }
    
    public static Item getItemsBasedOnId(long _id) {
       return Environment.getEntityManager()
                         .createNamedQuery("Item.findById", Item.class)
                         .setParameter("id", _id)
                         .getSingleResult();
        
    }
    
    
    public static ObservableList<Item> getItemsBasedOnName(String _name) {
        ObservableList<Item> items = FXCollections.observableArrayList(Environment.getEntityManager()
                                                  .createNamedQuery("Item.findByName", Item.class)
                                                  .setParameter("name", _name).getResultList());
        
        return (ObservableList<Item>) PropertyObject.refreshProperies(items);
    }
    
    public static ObservableList<Item> getItemsByAttributes(String _name, String _code, String _note, Brand _brand, Unit _unit, ProductType _type) {
        String query = "SELECT DISTINCT i FROM Item i LEFT JOIN PackedItem p WHERE i.v_id != 0";
        if (_name != null && _name.length() > 0) {
            query += " AND i.v_name = " + "'" + _name + "'";
        }
        if (_code != null && _name.length() > 0) {
            query += " AND i.v_code = " + "'" + _code + "'";
        }
        if (_note != null && _note.length() > 0) {
            query += " AND i.v_note = " + "'" + _note + "'";
        }
        if (_brand != null) {
            query += " AND i.v_brand .v_id= " + "'" + _brand.getId() + "'";
        }
        if (_unit != null) {
            query += " AND p.v_unit.v_id = " + _unit.getId();
        }
        if (_type != null) {
            query += " AND i.v_type.v_id = " + _type.getId();
        }
        
        ObservableList<Item> items =  FXCollections.observableArrayList(Environment.getEntityManager()
                                                   .createQuery(query, Item.class)
                                                   .getResultList());
        
        return PropertyObject.refreshProperies(items);
    }
    
    
    public static void saveItem(Item _item) {
        Environment.getEntityManager().persist(_item);
    }
    
    public static void saveItem(Item _item, List<ItemPriceRow> _prices) {
        List<PackedItem> packedItems = FXCollections.observableArrayList();
        List<ItemPrice> itemPrices = FXCollections.observableArrayList();
        for (int i = 0; i < _prices.size(); i++) {
            ItemPriceRow row = _prices.get(i);
            PackedItem item = new PackedItem(_item, (Unit) row.getUnit());
            if (!packedItems.contains(item)) {
                packedItems.add(item);
            } else {
                item = packedItems.get(packedItems.indexOf(item));
            }
            itemPrices.add(new ItemPrice(item, (SellPriceType) row.getSellPriceType(), row.getPrice()));
        }
        Environment.getEntityManager().persist(_item);
        Environment.getEntityManager().flush();
        for (PackedItem _i : packedItems) {
            ObservableList<ItemPrice> priceList = FXCollections.observableArrayList();
            for (ItemPrice _p : itemPrices) {
                if (!_i.equals(_p.getPackedItem())) {
                    priceList.add(_p);
                }
            }
            if (priceList.size() > 0) {
                _i.setSellPrices(priceList);
            }
            Environment.getEntityManager().persist(_i);
        }
    }
    
    public static void deleteItem(Item _item) {
        Environment.getEntityManager().remove(_item);
    }

    public String getCode() {
        return v_code;
    }

    public void setCode(String _c) {
        this.v_code = _c;
        v_codeProperty.setValue(_c);
    }
    
    public SimpleStringProperty codeProperty() {
        return v_codeProperty;
    }
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_codeProperty.setValue(v_code);
    }
}
