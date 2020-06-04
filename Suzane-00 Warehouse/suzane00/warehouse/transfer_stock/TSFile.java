/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.warehouse.transfer_stock;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.PropertyObject;
import suzane00.inventory.PackedItem;
import suzane00.inventory.model.StockFileRow;
import suzane00.inventory.stock.Stock;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "ts_file")
@IdClass(TSFilePK.class)
public class TSFile implements Serializable, PropertyObject {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "ts_id")
    private TransferStock v_transferStock;
    @Transient
    SimpleObjectProperty v_transferStockProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "item_id", referencedColumnName = "item_id"),
        @JoinColumn( name = "unit_id", referencedColumnName = "unit_id") })
    private PackedItem v_packedItem;
    @Transient
    private SimpleObjectProperty v_packedItemProperty = new SimpleObjectProperty();
    
    @Column(name = "quantity")
    double v_quantity;
    @Transient
    SimpleDoubleProperty v_quantityProperty = new SimpleDoubleProperty();
   

    
    public static ObservableList<StockFileRow> convertFilesToRows(List<TSFile> _files) {
        ObservableList<StockFileRow> rows = FXCollections.observableArrayList();
        for (TSFile file : _files) {
            StockFileRow row = new StockFileRow();
            row.setArea(file.getTransferStock().getSourceArea());
            row.setItem(file.getPackedItem().getItem());
            row.setUnit(file.getPackedItem().getUnit());
            row.setAdjustedQuantity(file.getQuantity());
            rows.add(row);
        }
        return rows;
    }
    
    public TransferStock getTransferStock() {
        return v_transferStock;
    }

    public void setTransferStock(TransferStock _sp) {
        this.v_transferStock = _sp;
        v_transferStockProperty.setValue(v_transferStock);
    }
    
    public SimpleObjectProperty transferStockProperty() {
        return v_transferStockProperty;
    }
    
   public PackedItem getPackedItem() {
        return v_packedItem;
    }

    public void setPackedItem(PackedItem _item) {
        this.v_packedItem = _item;
        v_packedItemProperty.setValue(v_packedItem);
    }
    
    public SimpleObjectProperty packedItemProperty() {
        return v_packedItemProperty;
    }
    
    public double getQuantity() {
        return v_quantity;
    }

    public void setQuantity(double _qty) {
        this.v_quantity = _qty;
        v_quantityProperty.setValue(v_quantity);
    }
    
    public SimpleDoubleProperty quantityProperty() {
        return v_quantityProperty;
    }
    
    @Override
    public void refreshProperty() {
        v_transferStockProperty.setValue(v_transferStock);
        v_packedItemProperty.setValue(v_packedItem);
        v_quantityProperty.setValue(v_quantity);
    }
}
