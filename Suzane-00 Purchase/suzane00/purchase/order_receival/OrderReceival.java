/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.order_receival;

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
import suzane00.purchase.purchase_order.PurchaseOrder;
import suzane00.transaction.model.ItemFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "order_receival")
public class OrderReceival extends Transaction implements Serializable, PropertyObject{
    private static final long serialVersionUID = 1L;
    
    @OneToMany(mappedBy = "v_orderReceival", cascade = CascadeType.ALL)
    private List<ORFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    @ManyToOne
    @JoinColumn(name = "po_id")
    private PurchaseOrder v_purchaseOrder;
    @Transient
    SimpleObjectProperty v_purchaseOrderProperty = new SimpleObjectProperty();
    
    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area v_area;
    @Transient
    SimpleObjectProperty v_areaProperty = new SimpleObjectProperty();
    
    
    public static ObservableList<OrderReceival> getORSByAttributes(String _numb, PurchaseOrder _po, 
            Area _area, String _note, Calendar _issueDateFrom, Calendar _issueDateTo, 
            Calendar _dueDateFrom, Calendar _dueDateTo) {
        String query = "SELECT o FROM OrderReceival o WHERE o.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND o.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_po != null) {
            query += " AND o.v_purchaseOrder = " + _po;
        }
        if (_area != null) {
            query += " AND o.v_area = " + "'" + _area + "'";
        }
        if (_note != null && _note.length() > 0) {
            query += " AND o.v_note = " + "'" + _note + "'";
        }
        if (_issueDateFrom != null) {
            Date issueDateFrom = new Date(_issueDateFrom.getTimeInMillis());
            query += " AND o.v_issueDate >= '" + sdf.format(issueDateFrom) + "'";
        }
        if (_issueDateTo != null) {
            Date issueDateTo = new Date(_issueDateTo.getTimeInMillis());
            query += " AND o.v_issueDate <= '" + sdf.format(issueDateTo) + "'";
        }
        if (_dueDateFrom != null) {
            Date dueDateFrom = new Date(_dueDateFrom.getTimeInMillis());
            query += " AND o.v_dueDate >= '" + sdf.format(dueDateFrom) + "'";
        }
        if (_dueDateTo != null) {
            Date dueDateTo = new Date(_dueDateTo.getTimeInMillis());
            query += " AND o.v_dueDate <= '" + sdf.format(dueDateTo) + "'";
        }
        ObservableList<OrderReceival> vis = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, OrderReceival.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(vis);
    }
    
    public static void saveOrderReceival(OrderReceival _si, ObservableList<ORFile> _files) {
        for (ORFile _file : _files) {
            _file.setOrderReceival(_si);
        }
        _si.setFiles(_files);
        Environment.getEntityManager().persist(_si);
    }
    
    public static void saveOrderReceivalByRow(OrderReceival _or, ObservableList<? extends ItemFileRow> _rows ) {
        buildOrderReceival(_or, _rows);
        Environment.getEntityManager().persist(_or);
        for (ItemFileRow _row : _rows) {
            if(_row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            Stock.addStock(PackedItem.getPackedItem(Item.getItemsBasedOnId(_row.getId()), _row.getUnit())
                    , _or.getArea(), _row.getQuantity());
        }
    }
    
    public static void editOrderReceivalByRow(OrderReceival _or, ObservableList<? extends ItemFileRow> _rows) {
        for (ORFile _file : _or.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        _or.setFiles(null);
        Environment.getEntityManager().flush();
        buildOrderReceival(_or, _rows);
    }
    
    public static void deleteOrderReceival(OrderReceival _or) {
        Environment.getEntityManager().remove(_or);
    }
    
    public PurchaseOrder getPurchaseOrder() {
        return v_purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder _so) {
        this.v_purchaseOrder = _so;
        v_purchaseOrderProperty.setValue(_so);
    }
    
    public SimpleObjectProperty purchaseOrderProperty() {
        return v_purchaseOrderProperty;
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
    
    public List<ORFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<ORFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (ORFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<ORFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> filesProperty() {
        return v_filesProperty;
    }
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_purchaseOrderProperty.setValue(v_purchaseOrder);
        v_areaProperty.setValue(v_area);
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
                for (ORFile file : v_files) {
                    v_filesProperty.add(new SimpleObjectProperty<ORFile>(file));
                }
        }
    }
    
    private static void buildOrderReceival(OrderReceival _or, ObservableList<? extends ItemFileRow> _rows) {
        setFiles(_or, _rows);
    }
    
    private static void setFiles(OrderReceival _or, ObservableList<? extends ItemFileRow> _rows) {
        ObservableList<ORFile> files = FXCollections.observableArrayList();
        for (ItemFileRow row : _rows) {
            if(row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            ORFile file = new ORFile();
            file.setPackedItem(Environment.getEntityManager().find(PackedItem.class, new PackedItemPK(row.getId(), row.getUnit().getId())));
            file.setQuantity(row.getQuantity());
            file.setOrderReceival(_or);
            files.add(file);
        }
        _or.setFiles(files);
    }
    
    
}

