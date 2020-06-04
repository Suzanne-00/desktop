/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.transaction;

import java.io.IOException;

import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javax.persistence.EntityManagerFactory;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
import suzane00.inventory.Area;
import suzane00.inventory.Item;
import suzane00.inventory.ItemPrice;
import suzane00.inventory.PackedItem;
import suzane00.inventory.Unit;
import suzane00.transaction.model.ItemFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name ="transaction")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Transaction implements PropertyObject {
    private static final long serialVersionUID = 1L;
    
    public final static String TYPE_CASH = "Cash";
    public final static String TYPE_CREDIT = "Credit";
    public final static int TYPE_COUNT = 2;
    
    public final static int TYPE_PURCHASE = 1;
    public final static int TYPE_SALE = 2;
    public final static int TYPE_INVENTORY = 3;
    private static Background v_purchaseBackground;
    private static Background v_saleBackground;
    private static Background v_inventoryBackground;
    
    static {
        try
        {
            InputStream is = Environment.class.getResource(
                    "/suzane00/global/resource/White Green Gradient.jpg").openStream();
            BackgroundImage img= new BackgroundImage(new Image(is,32,32,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0, 0, false, false, false, true));
            v_purchaseBackground = new Background(img);
            
            is = Environment.class.getResource(
                    "/suzane00/global/resource/White Blue Gradient 2.jpg").openStream();
            img= new BackgroundImage(new Image(is,32,32,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0, 0, false, false, false, true));
            v_saleBackground = new Background(img); 
            
            is = Environment.class.getResource(
                    "/suzane00/global/resource/White Brown Gradient.jpg").openStream();
            img= new BackgroundImage(new Image(is,32,32,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0, 0, false, false, false, true));
            v_inventoryBackground = new Background(img);
           
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static Background getBackground(int _type) {
        switch(_type) {
            case TYPE_PURCHASE:  return v_purchaseBackground;
            case TYPE_SALE:      return v_saleBackground;
            case TYPE_INVENTORY:    return v_inventoryBackground;
            default:             return null;
        }
    }

    @TableGenerator(name = "tran_gen", allocationSize = 1)
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "tran_gen")
    private Long v_id;
    @Transient
    private SimpleLongProperty v_idProperty = new SimpleLongProperty();
   
    @Column(name = "transaction_number")
    private String v_transactionNumber;
    @Transient
    private SimpleStringProperty v_transactionNumberProperty = new SimpleStringProperty();
   
    @Column(name = "note")
    private String v_note;
    @Transient
    private SimpleStringProperty v_noteProperty = new SimpleStringProperty();
    
    @Column( name = "issue_date" )
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar v_issueDate;
    @Transient
    private SimpleStringProperty v_issueDateProperty = new SimpleStringProperty();
    
    @Column( name = "due_date" )
    @Temporal(javax.persistence.TemporalType.DATE)
    private Calendar v_dueDate;
    @Transient
    private SimpleStringProperty v_dueDateProperty = new SimpleStringProperty() ;
    
    public static Item getItem(ItemFileRow _row) {
        Item item = Environment.getEntityManager().createNamedQuery("Item.findById", Item.class).setParameter("id", _row.getId()).getSingleResult();
        item.refreshProperty();
        return item;
    }
    
    public static PackedItem getPackedItem(ItemFileRow _row) {
        Item item = Transaction.getItem(_row);
        Unit unit = _row.getUnit();
        PackedItem pItem = Environment.getEntityManager().createNamedQuery("PackedItem.findPackedItem", PackedItem.class).setParameter("item", item).setParameter("unit", unit).getSingleResult();
        pItem.refreshProperty();
        return pItem;
    }

    public static ObservableList<ItemPrice> getItemPricesByRow(ItemFileRow _row) {
        PackedItem item = getPackedItem(_row);
        ObservableList<ItemPrice> itemPrices = FXCollections.observableArrayList(Environment.getEntityManager().createNamedQuery("ItemPrice.findByPackedItem", ItemPrice.class).setParameter("packedItem", item).getResultList());
        return PropertyObject.refreshProperies(itemPrices);
    }

   
    public Long getId() {
        return v_id;
    }

    public void setId(Long _id) {
        this.v_id = _id;
        v_idProperty.setValue(_id);
    }
    
    public SimpleLongProperty idProperty() {
        return v_idProperty;
    }
    
    
    public String getTransactionNumber() {
        return v_transactionNumber;
    }

    public void setTransactionNumber(String _number) {
        this.v_transactionNumber = _number;
        v_transactionNumberProperty.setValue(_number);
    }
    
     public SimpleStringProperty transactionNumberProperty() {
       
        return v_transactionNumberProperty;
    }
          
    public String getNote() {
        return v_note;
    }

    public void setNote(String _note) {
        this.v_note = _note;
        v_noteProperty.setValue(_note);
    }
    
    public SimpleStringProperty noteProperty() {
      
        return v_noteProperty;
    }
     
    public Calendar getIssueDate() {
        return v_issueDate;
    }

    public void setIssueDate(Calendar _date) {
        
        v_issueDate = _date;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd"); 
        v_issueDateProperty.setValue(f.format(v_issueDate.getTime()));
    }
    
     public SimpleStringProperty issueDateProperty() {
        return v_issueDateProperty;
    }
     
    public Calendar getDueDate() {
        return v_dueDate;
    }

    public void setDueDate(Calendar _date) {
        v_dueDate = _date;
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd"); 
        v_dueDateProperty.setValue(f.format(v_dueDate.getTime()));
    }
    
    public SimpleStringProperty dueDateProperty() {
        return v_dueDateProperty;
    }
    
    @Override
    public void refreshProperty() {
        v_idProperty.setValue(v_id);
        v_transactionNumberProperty.setValue(v_transactionNumber);
        v_noteProperty.setValue(v_note);
        if(v_issueDate != null)
            v_issueDateProperty.setValue((new SimpleDateFormat("yyyy-MM-dd")).format(v_issueDate.getTime()));
        if(v_dueDate != null)
            v_dueDateProperty.setValue((new SimpleDateFormat("yyyy-MM-dd")).format(v_dueDate.getTime()));
    }
     
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_id != null ? v_id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the v_id fields are not set
        if (!(object instanceof Transaction)) {
            return false;
        }
        Transaction other = (Transaction) object;
        if ((this.v_id == null && other.v_id != null) || (this.v_id != null && !this.v_id.equals(other.v_id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return v_transactionNumber;
    }
    
}
