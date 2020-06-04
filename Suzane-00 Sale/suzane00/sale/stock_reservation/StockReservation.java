/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.stock_reservation;

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
import suzane00.transaction.model.ItemFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "stock_reservation")
public class StockReservation extends Transaction implements Serializable, PropertyObject{
    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy = "v_stockReservation", cascade = CascadeType.ALL)
    private List<SRFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    @ManyToOne
    @JoinColumn(name = "so_id")
    private SalesOrder v_salesOrder;
    @Transient
    SimpleObjectProperty v_salesOrderProperty = new SimpleObjectProperty();
    
    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area v_area;
    @Transient
    SimpleObjectProperty v_areaProperty = new SimpleObjectProperty();
    
    
    public static ObservableList<StockReservation> getRSSByAttributes(String _numb, SalesOrder _so, 
            Area _area, String _note, Calendar _issueDateFrom, Calendar _issueDateTo, 
            Calendar _dueDateFrom, Calendar _dueDateTo) {
        String query = "SELECT s FROM StockReservation s WHERE s.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND s.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_so != null) {
            query += " AND s.v_salesOrder = " + _so;
        }
        if (_area != null) {
            query += " AND s.v_area = " + "'" + _area + "'";
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
        ObservableList<StockReservation> vis = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, StockReservation.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(vis);
    }
    
    public static void saveStockReservation(StockReservation _si, ObservableList<SRFile> _files, ObservableList<Cost> _discs) {
        for (SRFile _file : _files) {
            _file.setStockReservation(_si);
        }
        _si.setFiles(_files);
        Environment.getEntityManager().persist(_si);
    }
    
    public static boolean saveStockReservationByRow(StockReservation _sr, ObservableList<? extends ItemFileRow> _rows ) {
        buildStockReservation(_sr, _rows);
        Environment.getEntityManager().persist(_sr);
        for (ItemFileRow _row : _rows) {
            if(_row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            
            boolean result = Stock.removeStock(PackedItem.getPackedItem(Item.getItemsBasedOnId(_row.getId()), _row.getUnit())
                                , _sr.getArea(), _row.getQuantity());
            
            if(!result)
                return false;
        }
        return true;
    }
    
    public static void editStockReservationByRow(StockReservation _or, ObservableList<? extends ItemFileRow> _rows) {
        for (SRFile _file : _or.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        _or.setFiles(null);
        Environment.getEntityManager().flush();
        buildStockReservation(_or, _rows);
    }
    
    public static void deleteStockReservation(StockReservation _sr) {
        Environment.getEntityManager().remove(_sr);
        Environment.getEntityManager().getTransaction().commit();
    }
    
    public SalesOrder getSalesOrder() {
        return v_salesOrder;
    }

    public void setSalesOrder(SalesOrder _so) {
        this.v_salesOrder = _so;
        v_salesOrderProperty.setValue(_so);
    }
    
    public SimpleObjectProperty salesOrderProperty() {
        return v_salesOrderProperty;
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
    
    public List<SRFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<SRFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (SRFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<SRFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> filesProperty() {
        return v_filesProperty;
    }
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_salesOrderProperty.setValue(v_salesOrder);
        v_areaProperty.setValue(v_area);
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
                for (SRFile file : v_files) {
                    v_filesProperty.add(new SimpleObjectProperty<SRFile>(file));
                }
        }
    }
    
    private static void buildStockReservation(StockReservation _or, ObservableList<? extends ItemFileRow> _rows) {
        setFiles(_or, _rows);
    }
    
    private static void setFiles(StockReservation _or, ObservableList<? extends ItemFileRow> _rows) {
        ObservableList<SRFile> files = FXCollections.observableArrayList();
        for (ItemFileRow row : _rows) {
            if(row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            SRFile file = new SRFile();
            file.setPackedItem(Environment.getEntityManager().find(PackedItem.class, new PackedItemPK(row.getId(), row.getUnit().getId())));
            file.setQuantity(row.getQuantity());
            file.setStockReservation(_or);
            files.add(file);
        }
        _or.setFiles(files);
    }
}
