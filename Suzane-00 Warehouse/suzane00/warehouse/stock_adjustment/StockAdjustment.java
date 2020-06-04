/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.warehouse.stock_adjustment;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
import suzane00.global.Utility;
import suzane00.inventory.Area;
import suzane00.inventory.Cost;
import suzane00.inventory.Item;
import suzane00.inventory.PackedItem;
import suzane00.inventory.model.StockFileRow;
import suzane00.inventory.stock.Stock;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "stock_adjustment")
public class StockAdjustment extends Transaction implements Serializable, PropertyObject {
    
    @ManyToOne
    @JoinColumn(name = "area_id") 
    private Area v_area;
    @Transient
    SimpleObjectProperty v_areaProperty = new SimpleObjectProperty();
    
    @OneToMany(mappedBy = "v_stockAdjustment", cascade = CascadeType.ALL)
    private List<SAFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    
    public static ObservableList<StockAdjustment> getSASByAttributes(String _numb, Area _area, 
        String _note, Calendar _issueDateFrom, Calendar _issueDateTo, Calendar _dueDateFrom, Calendar _dueDateTo) {
        String query = "SELECT s FROM StockAdjustment s WHERE s.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND s.v_transactionNumber = " + "'" + _numb + "'";
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
        ObservableList<StockAdjustment> sas = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, StockAdjustment.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(sas);
    }
    
    public static void saveStockAdjustment(StockAdjustment _sa, ObservableList<SAFile> _files) {
        for (SAFile _file : _files) {
            _file.setStockAdjustment(_sa);
        }
        _sa.setFiles(_files);
        Environment.getEntityManager().persist(_sa);
    }
    
//    public static void editSalesPaymentByRow(StockAdjustment _sp, ObservableList<SAFile> _files) {
//        for (SAFile _file : _sp.getFiles()) {
//            Environment.getEntityManager().remove(_file);
//        }
//        _sp.setFiles(null);
//        Environment.getEntityManager().flush();
//        buildSalesPayment(_sp, _rows);
//    }
    
    public static void saveStockAdjustmentByRow(StockAdjustment _sa, ObservableList<? extends StockFileRow> _rows) {
        buildStockAdjustment(_sa, _rows);
        Environment.getEntityManager().persist(_sa);
        for (StockFileRow _row : _rows) {
            if(_row.getAdjustedQuantity() < Utility.DOUBLE_TOLERANCE || 
               Math.abs(_row.getQuantity() - _row.getAdjustedQuantity()) < Utility.DOUBLE_TOLERANCE )
                continue;
            Stock stock = Stock.getStocksById(PackedItem.getPackedItem(_row.getItem(), _row.getUnit()), 
                    _sa.getArea());
            if(stock == null)
                continue;
            
            stock.setQuantity(_row.getAdjustedQuantity());
//            if(_row.getAdjustedQuantity() < _row.getQuantity()) {
//                Stock.removeStock(PackedItem.getPackedItem(_row.getItem(), _row.getUnit())
//                                    , _sa.getArea(), _row.getQuantity() - _row.getAdjustedQuantity());
//            }
//            else {
//                Stock.addStock(PackedItem.getPackedItem(_row.getItem(), _row.getUnit())
//                                    , _sa.getArea(), _row.getAdjustedQuantity() - _row.getQuantity());
//            }
        }
        //Environment.getEntityManager().flush();
        //Environment.getEntityManager().re
//        try {
//            Environment.getEntityManager().flush();
//        }
//        catch(SQLException e) {
//            e.printStackTrace();
//        }
    }
    
    public static void editStockAdjustmentByRow(StockAdjustment _sa, ObservableList<? extends StockFileRow> _rows) {
        for (SAFile _file : _sa.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        _sa.setFiles(null);
        Environment.getEntityManager().flush();
        buildStockAdjustment(_sa, _rows);
    }
    
    public static void deleteStockAdjustment(StockAdjustment _sa) {
        Environment.getEntityManager().remove(_sa);
        Environment.getEntityManager().getTransaction().commit();
    }
    
//    public static double getTotalPaidForInvoice(SalesInvoice _si) {
//        try {
//            Statement stmt = Environment.getConnection().createStatement();
//            String sql = "SELECT SUM(amount) FROm sp_file WHERE si_id = " + _si.getId();
//            ResultSet rs = null ;
//            double paid = 0;
//            rs = stmt.executeQuery(sql);
//            rs.next();
//            paid = rs.getDouble(1);
//            return paid;
//        }
//        catch(SQLException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
    
    public Area getArea() {
        return v_area;
    }

    public void setArea(Area _cust) {
        this.v_area = _cust;
        v_areaProperty.setValue(v_area);
    }
    
    public SimpleObjectProperty areaProperty() {
        return v_areaProperty;
    }
    
    public List<SAFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<SAFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (SAFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<SAFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> filesProperty() {
        return v_filesProperty;
    }
    
//    public double getAmount() {
//        return v_amount;
//    }
//
//    public void setAmount(double _amount) {
//        this.v_amount = _amount;
//        v_amountProperty.setValue(v_amount);
//    }
//    
//     public SimpleDoubleProperty amountProperty() {
//        return v_amountProperty;
//    }
    
   
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_areaProperty.setValue(v_area);
        
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
            for (SAFile file : v_files) {
                v_filesProperty.add(new SimpleObjectProperty<SAFile>(file));
            }
        }
    }
    
     private static void buildStockAdjustment(StockAdjustment _sp, ObservableList<? extends StockFileRow> _rows) {
        setFiles(_sp, _rows);
    }
     
     private static void setFiles(StockAdjustment _sp, ObservableList<? extends StockFileRow> _rows) {
        ObservableList<SAFile> files = FXCollections.observableArrayList();
        for (StockFileRow row : _rows) {
            if(row.getAdjustedQuantity() < Utility.DOUBLE_TOLERANCE || 
               Math.abs(row.getQuantity() - row.getAdjustedQuantity()) < Utility.DOUBLE_TOLERANCE )
                continue;
            SAFile file = new SAFile();
            file.setStockAdjustment(_sp);
            file.setPackedItem(PackedItem.getPackedItem(row.getItem(), row.getUnit()));
            file.setQuantity(row.getQuantity());
            file.setAdjustedQuantity(row.getAdjustedQuantity());
            files.add(file);
        }
        _sp.setFiles(files);
    }
}
