/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.delivery_order;

import suzane00.sale.stock_reservation.*;
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
import suzane00.sale.sales_order.SalesOrder;
import suzane00.source.Address;
import suzane00.source.Customer;
import suzane00.transaction.model.ItemFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "delivery_order")
public class DeliveryOrder extends Transaction implements Serializable, PropertyObject{
    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy = "v_deliveryOrder", cascade = CascadeType.ALL)
    private List<DOFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    @ManyToOne
    @JoinColumn(name = "sr_id")
    private suzane00.sale.stock_reservation.StockReservation v_stockReservation;
    @Transient
    SimpleObjectProperty v_stockReservationProperty = new SimpleObjectProperty();
    
    @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "city_id", referencedColumnName = "city_id"),
        @JoinColumn( name = "street_name", referencedColumnName = "street_name") })    
    private Address v_address;
    @Transient
    SimpleObjectProperty v_addressProperty = new SimpleObjectProperty();
    
    
    public static ObservableList<DeliveryOrder> getDOSByAttributes(String _numb, StockReservation _sr, 
            Customer _cust, Address _address, String _note, Calendar _issueDateFrom, Calendar _issueDateTo, 
            Calendar _dueDateFrom, Calendar _dueDateTo) {
        String query = "SELECT d FROM DeliveryOrder d WHERE d.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND d.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_sr != null) {
            query += " AND d.v_stockReservation = " + _sr;
        }
        if (_cust != null) {
            query += " AND d.v_stockReservation.v_salesOrder.v_customer = " + "'" + _cust + "'";
        }
        if (_address != null) {
            query += " AND d.v_address.v_city.v_id = " + _address.getCity().getId() +
                     " AND d.v_address.v_streetname = '" + _address.getStreetName() + "' ";
        }
        if (_note != null && _note.length() > 0) {
            query += " AND d.v_note = " + "'" + _note + "'";
        }
        if (_issueDateFrom != null) {
            Date issueDateFrom = new Date(_issueDateFrom.getTimeInMillis());
            query += " AND d.v_issueDate >= '" + sdf.format(issueDateFrom) + "'";
        }
        if (_issueDateTo != null) {
            Date issueDateTo = new Date(_issueDateTo.getTimeInMillis());
            query += " AND d.v_issueDate <= '" + sdf.format(issueDateTo) + "'";
        }
        if (_dueDateFrom != null) {
            Date dueDateFrom = new Date(_dueDateFrom.getTimeInMillis());
            query += " AND d.v_dueDate >= '" + sdf.format(dueDateFrom) + "'";
        }
        if (_dueDateTo != null) {
            Date dueDateTo = new Date(_dueDateTo.getTimeInMillis());
            query += " AND d.v_dueDate <= '" + sdf.format(dueDateTo) + "'";
        }
        ObservableList<DeliveryOrder> dos = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, DeliveryOrder.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(dos);
    }
    
    public static void saveStockReservation(DeliveryOrder _do, ObservableList<DOFile> _files) {
        for (DOFile _file : _files) {
            _file.setDeliveryOrder(_do);
        }
        _do.setFiles(_files);
        Environment.getEntityManager().persist(_do);
    }
    
    public static void saveDeliveryOrderByRow(DeliveryOrder _do, ObservableList<? extends ItemFileRow> _rows ) {
        buildDeliveryOrder(_do, _rows);
        Environment.getEntityManager().persist(_do);
//        for (ItemFileRow _row : _rows) {
//            boolean result = Stock.removeStock(PackedItem.getPackedItem(Item.getItemsBasedOnId(_row.getId()), _row.getUnit())
//                                , _do.getAddress(), _row.getQuantity());
//            
//            if(!result)
//                return false;
//        }
    }
    
    public static void editDeliveryOrderByRow(DeliveryOrder _do, ObservableList<? extends ItemFileRow> _rows) {
        for (DOFile _file : _do.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        _do.setFiles(null);
        Environment.getEntityManager().flush();
        buildDeliveryOrder(_do, _rows);
    }
    
    public static void deleteDeliveryOrder(DeliveryOrder _do) {
        Environment.getEntityManager().remove(_do);
        Environment.getEntityManager().getTransaction().commit();
    }
    
    public suzane00.sale.stock_reservation.StockReservation getStockreservation() {
        return v_stockReservation;
    }

    public void setStockReservation(suzane00.sale.stock_reservation.StockReservation _sr) {
        this.v_stockReservation = _sr;
        v_stockReservationProperty.setValue(_sr);
    }
    
    public SimpleObjectProperty stockReservationProperty() {
        return v_stockReservationProperty;
    }
    
    public Address getAddress() {
        return v_address;
    }

    public void setAddress(Address _addr) {
        this.v_address = _addr;
        v_addressProperty.setValue(v_address);
    }
    
    public SimpleObjectProperty addressProperty() {
        return v_addressProperty;
    }
    
    public List<DOFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<DOFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (DOFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<DOFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> filesProperty() {
        return v_filesProperty;
    }
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_stockReservationProperty.setValue(v_stockReservation);
        v_addressProperty.setValue(v_address);
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
                for (DOFile file : v_files) {
                    v_filesProperty.add(new SimpleObjectProperty<DOFile>(file));
                }
        }
    }
    
    private static void buildDeliveryOrder(DeliveryOrder _or, ObservableList<? extends ItemFileRow> _rows) {
        setFiles(_or, _rows);
    }
    
    private static void setFiles(DeliveryOrder _do, ObservableList<? extends ItemFileRow> _rows) {
        ObservableList<DOFile> files = FXCollections.observableArrayList();
        for (ItemFileRow row : _rows) {
            if(row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            DOFile file = new DOFile();
            file.setPackedItem(Environment.getEntityManager().find(PackedItem.class, new PackedItemPK(row.getId(), row.getUnit().getId())));
            file.setQuantity(row.getQuantity());
            file.setDeliveryOrder(_do);
            files.add(file);
        }
        _do.setFiles(files);
    }
}
