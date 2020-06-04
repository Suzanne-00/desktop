/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.warehouse.stock_adjustment;

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
@Table(name = "sa_file")
@IdClass(SAFilePK.class)
public class SAFile implements Serializable, PropertyObject {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "sa_id")
    private StockAdjustment v_stockAdjustment;
    @Transient
    SimpleObjectProperty v_stockAdjustmentProperty = new SimpleObjectProperty();
    
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
    
    @Column(name = "adjusted_quantity")
    double v_adjustedQuantity;
    @Transient
    SimpleDoubleProperty v_adjustedQuantityProperty = new SimpleDoubleProperty();
    
    
    public static ObservableList<StockFileRow> convertFilesToRows(List<SAFile> _files) {
        ObservableList<StockFileRow> rows = FXCollections.observableArrayList();
        for (SAFile file : _files) {
            StockFileRow row = new StockFileRow();
            row.setArea(file.getStockAdjustment().getArea());
            row.setItem(file.getPackedItem().getItem());
            row.setUnit(file.getPackedItem().getUnit());
            row.setQuantity(file.getQuantity());
            row.setAdjustedQuantity(file.getAdjustedQuantity());
            rows.add(row);
        }
        return rows;
    }
    
    public StockAdjustment getStockAdjustment() {
        return v_stockAdjustment;
    }

    public void setStockAdjustment(StockAdjustment _sp) {
        this.v_stockAdjustment = _sp;
        v_stockAdjustmentProperty.setValue(v_stockAdjustment);
    }
    
    public SimpleObjectProperty stockAdjustmentProperty() {
        return v_stockAdjustmentProperty;
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
    
    public double getAdjustedQuantity() {
        return v_adjustedQuantity;
    }

    public void setAdjustedQuantity(double _qty) {
        this.v_adjustedQuantity = _qty;
        v_adjustedQuantityProperty.setValue(v_adjustedQuantity);
    }
    
    public SimpleDoubleProperty adjustedQuantityProperty() {
        return v_adjustedQuantityProperty;
    }
        

    @Override
    public void refreshProperty() {
        v_stockAdjustmentProperty.setValue(v_stockAdjustment);
        v_packedItemProperty.setValue(v_packedItem);
        v_quantityProperty.setValue(v_quantity);
        v_adjustedQuantityProperty.setValue(v_adjustedQuantity);
    }
}
