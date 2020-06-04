/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.warehouse.transfer_stock;

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
@Table(name = "transfer_stock")
public class TransferStock extends Transaction implements Serializable, PropertyObject {
    
    @ManyToOne
    @JoinColumn(name = "source_area_id")
    private Area v_sourceArea;
    @Transient
    SimpleObjectProperty v_sourceAreaProperty = new SimpleObjectProperty();
    
    @ManyToOne
    @JoinColumn(name = "destination_area_id")
    private Area v_destinationArea;
    @Transient
    SimpleObjectProperty v_destinationAreaProperty = new SimpleObjectProperty();
    
    @OneToMany(mappedBy = "v_transferStock", cascade = CascadeType.ALL)
    private List<TSFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    
    public static ObservableList<TransferStock> getTSSByAttributes(String _numb, Area _sArea, Area _dArea, 
        String _note, Calendar _issueDateFrom, Calendar _issueDateTo, Calendar _dueDateFrom, Calendar _dueDateTo) {
        
        String query = "SELECT t FROM TransferStock t WHERE t.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND t.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_sArea != null) {
            query += " AND t.v_sourceArea.v_id = " + _sArea.getId();
        }
        if (_dArea != null) {
            query += " AND t.v_destinationArea.v_id = " + _dArea.getId();
        }
        if (_note != null && _note.length() > 0) {
            query += " AND t.v_note = " + "'" + _note + "'";
        }
        if (_issueDateFrom != null) {
            Date issueDateFrom = new Date(_issueDateFrom.getTimeInMillis());
            query += " AND t.v_issueDate >= '" + sdf.format(issueDateFrom) + "'";
        }
        if (_issueDateTo != null) {
            Date issueDateTo = new Date(_issueDateTo.getTimeInMillis());
            query += " AND t.v_issueDate <= '" + sdf.format(issueDateTo) + "'";
        }
        if (_dueDateFrom != null) {
            Date dueDateFrom = new Date(_dueDateFrom.getTimeInMillis());
            query += " AND t.v_dueDate >= '" + sdf.format(dueDateFrom) + "'";
        }
        if (_dueDateTo != null) {
            Date dueDateTo = new Date(_dueDateTo.getTimeInMillis());
            query += " AND t.v_dueDate <= '" + sdf.format(dueDateTo) + "'";
        }
        ObservableList<TransferStock> tss = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, TransferStock.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(tss);
    }
    
//    public static void saveStockAdjustment(TransferStock _sa, ObservableList<TSFile> _files) {
//        for (TSFile _file : _files) {
//            _file.setStockAdjustment(_sa);
//        }
//        _sa.setFiles(_files);
//        Environment.getEntityManager().persist(_sa);
//    }
    
//    public static void editSalesPaymentByRow(StockAdjustment _sp, ObservableList<SAFile> _files) {
//        for (SAFile _file : _sp.getFiles()) {
//            Environment.getEntityManager().remove(_file);
//        }
//        _sp.setFiles(null);
//        Environment.getEntityManager().flush();
//        buildSalesPayment(_sp, _rows);
//    }
    
    public static boolean saveTransferStockByRow(TransferStock _ts, ObservableList<? extends StockFileRow> _rows) {
        boolean result = true;
        setFiles(_ts, _rows);
        Environment.getEntityManager().persist(_ts);
        for (StockFileRow _row : _rows) {
            if(_row.getQuantity() < Utility.DOUBLE_TOLERANCE )
                continue;
            Stock stock = Stock.getStocksById(PackedItem.getPackedItem(_row.getItem(), _row.getUnit()), 
                    _ts.getSourceArea());
            
            if(stock == null)
                continue;
            
            stock.setQuantity(stock.getQuantity() - _row.getQuantity());
         
            stock = Stock.getStocksById(PackedItem.getPackedItem(_row.getItem(), _row.getUnit()), 
                    _ts.getDestinationArea());
            
            if(stock == null) {
                Stock newStock = new Stock();
                newStock.setPackedItem(PackedItem.getPackedItem(_row.getItem(), _row.getUnit()));
                newStock.setArea(_ts.getDestinationArea());
                newStock.setQuantity(_row.getQuantity());
                Environment.getEntityManager().persist(newStock);
            }
            else {
                stock.setQuantity(stock.getQuantity() + _row.getQuantity());
            }
        }
        return true;
    }
    
    public static void editTransferStockByRow(TransferStock _sa, ObservableList<? extends StockFileRow> _rows) {
        for (TSFile _file : _sa.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        _sa.setFiles(null);
        Environment.getEntityManager().flush();
        saveTransferStock(_sa, _rows);
    }
    
    public static void deleteTransferStock(TransferStock _sa) {
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
    
    public Area getSourceArea() {
        return v_sourceArea;
    }

    public void setSourceArea(Area _cust) {
        this.v_sourceArea = _cust;
        v_sourceAreaProperty.setValue(v_sourceArea);
    }
    
    public SimpleObjectProperty sourceAreaProperty() {
        return v_sourceAreaProperty;
    }
    
    public Area getDestinationArea() {
        return v_destinationArea;
    }

    public void setDestinationArea(Area _cust) {
        this.v_destinationArea = _cust;
        v_destinationAreaProperty.setValue(v_destinationArea);
    }
    
    public SimpleObjectProperty destinationAreaProperty() {
        return v_destinationAreaProperty;
    }
    
    public List<TSFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<TSFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (TSFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<TSFile>(_file));
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
        v_sourceAreaProperty.setValue(v_sourceArea);
        v_destinationAreaProperty.setValue(v_destinationArea);
        
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
            for (TSFile file : v_files) {
                v_filesProperty.add(new SimpleObjectProperty<TSFile>(file));
            }
        }
    }
    
     private static void saveTransferStock(TransferStock _sp, ObservableList<? extends StockFileRow> _rows) {
        setFiles(_sp, _rows);
    }
     
     private static void setFiles(TransferStock _sp, ObservableList<? extends StockFileRow> _rows) {
        ObservableList<TSFile> files = FXCollections.observableArrayList();
        for (StockFileRow row : _rows) {
            if(row.getQuantity()< Utility.DOUBLE_TOLERANCE)
                continue ;
            TSFile file = new TSFile();
            file.setTransferStock(_sp);
            file.setPackedItem(PackedItem.getPackedItem(row.getItem(), row.getUnit()));
            file.setQuantity(row.getQuantity());
            files.add(file);
        }
        _sp.setFiles(files);
    }
}
