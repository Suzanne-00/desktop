/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_retur;

import suzane00.sale.delivery_order.*;
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
@Table(name = "sales_retur")
public class SalesRetur extends Transaction implements Serializable, PropertyObject{
    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy = "v_salesRetur", cascade = CascadeType.ALL)
    private List<STFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    @ManyToOne
    @JoinColumn(name = "st_id")
    private DeliveryOrder v_deliveryOrder;
    @Transient
    SimpleObjectProperty v_deliveryOrderProperty = new SimpleObjectProperty();
    
    @ManyToOne
    @JoinColumn(name = "area_id")    
    private Area v_area;
    @Transient
    SimpleObjectProperty v_areaProperty = new SimpleObjectProperty();
    
    
    public static ObservableList<SalesRetur> getSTSByAttributes(String _numb, DeliveryOrder _do, 
            Customer _cust, Area _area, String _note, Calendar _issueDateFrom, Calendar _issueDateTo, 
            Calendar _dueDateFrom, Calendar _dueDateTo) {
        String query = "SELECT s FROM SalesRetur s WHERE s.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND s.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_do != null) {
            query += " AND s.v_deliveryOrder.v_id = " + _do.getId();
        }
        if (_cust != null) {
            query += " AND s.v_deliveryOrder..v_stockReservation.v_salesOrder.v_customer.v_id = " 
                    + _cust.getId();
        }
        if (_area != null) {
            query += " AND s.v_area.v_id = " + _area.getId();
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
        ObservableList<SalesRetur> dos = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, SalesRetur.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(dos);
    }
    
    public static void saveSalesRetur(SalesRetur _st, ObservableList<STFile> _files) {
        for (STFile _file : _files) {
            _file.setSalesRetur(_st);
        }
        _st.setFiles(_files);
        Environment.getEntityManager().persist(_st);
    }
    
    public static void saveSalesReturByRow(SalesRetur _st, ObservableList<? extends ItemFileRow> _rows ) {
        buildSalesRetur(_st, _rows);
        Environment.getEntityManager().persist(_st);
        for (ItemFileRow _row : _rows) {
            if(_row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            Stock.addStock(PackedItem.getPackedItem(Item.getItemsBasedOnId(_row.getId()), _row.getUnit())
                    , _st.getArea(), _row.getQuantity());
        }
    }
    
    public static void editSalesReturByRow(SalesRetur _st, ObservableList<? extends ItemFileRow> _rows) {
        for (STFile _file : _st.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        _st.setFiles(null);
        Environment.getEntityManager().flush();
        buildSalesRetur(_st, _rows);
    }
    
    public static void deleteSalesRetur(SalesRetur _st) {
        Environment.getEntityManager().remove(_st);
        Environment.getEntityManager().getTransaction().commit();
    }
    
    public DeliveryOrder getDeliveryOrder() {
        return v_deliveryOrder;
    }

    public void setDeliveryOrder(DeliveryOrder _do) {
        this.v_deliveryOrder = _do;
        v_deliveryOrderProperty.setValue(v_deliveryOrder);
    }
    
    public SimpleObjectProperty deliveryOrderProperty() {
        return v_deliveryOrderProperty;
    }
    
    public Area getArea() {
        return v_area;
    }

    public void setArea(Area _area) {
        this.v_area = _area;
        v_areaProperty.setValue(v_area);
    }
    
    public SimpleObjectProperty areaProperty() {
        return v_areaProperty;
    }
    
    public List<STFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<STFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (STFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<STFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> filesProperty() {
        return v_filesProperty;
    }
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_deliveryOrderProperty.setValue(v_deliveryOrder);
        v_areaProperty.setValue(v_area);
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
                for (STFile file : v_files) {
                    v_filesProperty.add(new SimpleObjectProperty<STFile>(file));
                }
        }
    }
    
    private static void buildSalesRetur(SalesRetur _or, ObservableList<? extends ItemFileRow> _rows) {
        setFiles(_or, _rows);
    }
    
    private static void setFiles(SalesRetur _st, ObservableList<? extends ItemFileRow> _rows) {
        ObservableList<STFile> files = FXCollections.observableArrayList();
        for (ItemFileRow row : _rows) {
            if(row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            STFile file = new STFile();
            file.setPackedItem(Environment.getEntityManager().find(PackedItem.class, new PackedItemPK(row.getId(), row.getUnit().getId())));
            file.setQuantity(row.getQuantity());
            file.setSalesRetur(_st);
            files.add(file);
        }
        _st.setFiles(files);
    }
}
