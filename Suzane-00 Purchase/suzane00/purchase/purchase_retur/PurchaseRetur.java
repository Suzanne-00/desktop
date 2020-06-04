/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_retur;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleLongProperty;
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
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
import suzane00.global.Utility;
import suzane00.transaction.Transaction;
import suzane00.inventory.Area;
import suzane00.inventory.Cost;
import suzane00.inventory.Item;
import suzane00.inventory.PackedItem;
import suzane00.inventory.PackedItemPK;
import suzane00.inventory.stock.Stock;
import suzane00.purchase.order_receival.OrderReceival;
import suzane00.source.Address;
import suzane00.source.Customer;
import suzane00.source.Supplier;
import suzane00.transaction.model.ItemFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "purchase_retur")
public class PurchaseRetur extends Transaction implements Serializable, PropertyObject{
    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy = "v_purchaseRetur", cascade = CascadeType.ALL)
    private List<PRFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    @ManyToOne
    @JoinColumn(name = "st_id")
    private OrderReceival v_orderReceival;
    @Transient
    SimpleObjectProperty v_deliveryOrderProperty = new SimpleObjectProperty();
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "city_id", referencedColumnName = "city_id"),
        @JoinColumn( name = "street_name", referencedColumnName = "street_name") })    
    private Address v_address;
    @Transient
    SimpleObjectProperty v_addressProperty = new SimpleObjectProperty();
    
    
    public static ObservableList<PurchaseRetur> getPRSByAttributes(String _numb, OrderReceival _or, 
            Supplier _supplier, Address _address, String _note, Calendar _issueDateFrom, Calendar _issueDateTo, 
            Calendar _dueDateFrom, Calendar _dueDateTo) {
        String query = "SELECT p FROM PurchaseRetur p WHERE p.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND p.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_or != null) {
            query += " AND p.v_orderReceival.v_id = " + _or.getId();
        }
        if (_supplier != null) {
            query += " AND p.v_orderReceival..v_purchaseOrder.v_supplier.v_id = " 
                    + _supplier.getId();
        }
        if (_address != null) {
            query += " AND p.v_address.v_city.v_id = " + _address.getCity().getId() +
                     " AND p.v_address.v_streetname = '" + _address.getStreetName() + "' ";
        }
        if (_note != null && _note.length() > 0) {
            query += " AND s.v_note = " + "'" + _note + "'";
        }
        if (_issueDateFrom != null) {
            Date issueDateFrom = new Date(_issueDateFrom.getTimeInMillis());
            query += " AND s.v_issueDate >= '" + sdf.format(issueDateFrom) + "'";
        }
        if (_issueDateTo != null) {
            Date issueDateTo = new Date(_issueDateTo.getTimeInMillis());
            query += " AND s.v_issueDate <= '" + sdf.format(issueDateTo) + "'";
        }
        if (_dueDateFrom != null) {
            Date dueDateFrom = new Date(_dueDateFrom.getTimeInMillis());
            query += " AND s.v_dueDate >= '" + sdf.format(dueDateFrom) + "'";
        }
        if (_dueDateTo != null) {
            Date dueDateTo = new Date(_dueDateTo.getTimeInMillis());
            query += " AND s.v_dueDate <= '" + sdf.format(dueDateTo) + "'";
        }
        ObservableList<PurchaseRetur> prs = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, PurchaseRetur.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(prs);
    }
    
    public static void savePurchaseRetur(PurchaseRetur _pr, ObservableList<PRFile> _files) {
        for (PRFile _file : _files) {
            _file.setPurchaseRetur(_pr);
        }
        _pr.setFiles(_files);
        Environment.getEntityManager().persist(_pr);
    }
    
    public static boolean savePurchaseReturByRow(PurchaseRetur _pr, ObservableList<? extends ItemFileRow> _rows ) {
        buildSalesRetur(_pr, _rows);
        Environment.getEntityManager().persist(_pr);
        for (ItemFileRow _row : _rows) {
            if(_row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            boolean result = Stock.removeStock(PackedItem.getPackedItem(Item.getItemsBasedOnId(_row.getId()), _row.getUnit())
                                , _pr.getOrderReceival().getArea(), _row.getQuantity());
            if(!result)
                return false;
        }
        return true;
    }
    
    public static void editPurchaseReturByRow(PurchaseRetur _st, ObservableList<? extends ItemFileRow> _rows) {
        for (PRFile _file : _st.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        _st.setFiles(null);
        Environment.getEntityManager().flush();
        buildSalesRetur(_st, _rows);
    }
    
    public static void deletePurchaseRetur(PurchaseRetur _st) {
        Environment.getEntityManager().remove(_st);
        Environment.getEntityManager().getTransaction().commit();
    }
    
    public OrderReceival getOrderReceival() {
        return v_orderReceival;
    }

    public void setOrderReceival(OrderReceival _or) {
        this.v_orderReceival = _or;
        v_deliveryOrderProperty.setValue(v_orderReceival);
    }
    
    public SimpleObjectProperty orderReceivalProperty() {
        return v_deliveryOrderProperty;
    }
    
    public Address getAddress() {
        return v_address;
    }

    public void setAddress(Address _address) {
        this.v_address = _address;
        v_addressProperty.setValue(v_address);
    }
    
    public SimpleObjectProperty addressProperty() {
        return v_addressProperty;
    }
    
    public List<PRFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<PRFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (PRFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<PRFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> filesProperty() {
        return v_filesProperty;
    }
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_deliveryOrderProperty.setValue(v_orderReceival);
        v_addressProperty.setValue(v_address);
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
                for (PRFile file : v_files) {
                    v_filesProperty.add(new SimpleObjectProperty<PRFile>(file));
                }
        }
    }
    
    private static void buildSalesRetur(PurchaseRetur _or, ObservableList<? extends ItemFileRow> _rows) {
        setFiles(_or, _rows);
    }
    
    private static void setFiles(PurchaseRetur _st, ObservableList<? extends ItemFileRow> _rows) {
        ObservableList<PRFile> files = FXCollections.observableArrayList();
        for (ItemFileRow row : _rows) {
            if(row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            PRFile file = new PRFile();
            file.setPackedItem(Environment.getEntityManager().find(PackedItem.class, new PackedItemPK(row.getId(), row.getUnit().getId())));
            file.setQuantity(row.getQuantity());
            file.setPurchaseRetur(_st);
            files.add(file);
        }
        _st.setFiles(files);
    }
}
