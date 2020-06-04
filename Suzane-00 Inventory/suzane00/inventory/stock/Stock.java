/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.stock;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
import suzane00.inventory.Area;
import suzane00.inventory.Item;
import suzane00.inventory.PackedItem;
import suzane00.inventory.Unit;
import suzane00.inventory.model.StockFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name ="stock")
@IdClass(StockPK.class)
@NamedQueries (
    {
        @NamedQuery(name="Stock.findAll",
        query="SELECT s FROM Stock s"),
        @NamedQuery(name="Stock.findById",
        query="SELECT s FROM Stock s WHERE s.v_packedItem = :packedItem"
                + " AND s.v_area = :area"),
        @NamedQuery(name="Stock.findByArea",
        query="SELECT s FROM Stock s WHERE s.v_area = :area")
    }
)
public class Stock implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    
    
    @Id
    @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "item_id", referencedColumnName = "item_id"),
        @JoinColumn( name = "unit_id", referencedColumnName = "unit_id") })
    private PackedItem v_packedItem;
    @Transient
    private SimpleObjectProperty v_packedItemProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area v_area;
    @Transient
    private SimpleObjectProperty v_areaProperty = new SimpleObjectProperty();
    
    @Column(name = "quantity")
    private double v_quantity;
    @Transient
    private SimpleDoubleProperty v_quantityProperty = new SimpleDoubleProperty();
    
    public static ObservableList<Stock> getStockByAttributes(Area _area, Item _item, Unit _unit, 
            Double _minQty, Double _maxQty) {
        String query = "SELECT s FROM Stock s WHERE s.v_area.v_id != 0";

        if (_area != null) {
            query += " AND s.v_area.v_id = " + "'" + _area.getId() + "'";
        }
        if (_item != null) {
            query += " AND s.v_packedItem.v_item.v_id = " + _item.getId();
        }
        if (_unit != null) {
            query += " AND s.v_packedItem.v_unit.v_id = " + "'" + _unit.getId() + "'";
        }
        if (_minQty != null) {
            query += " AND s.v_quantity >= " + "'" + _minQty + "'";
        }
        if (_maxQty != null) {
            query += " AND s.v_quantity <= " + "'" + _minQty + "'";
        }
        ObservableList<Stock> stocks = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, Stock.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(stocks);
    }
    
    public static ObservableList<StockFileRow> convertStocksToRows(List<Stock> _stock) {
        ObservableList<StockFileRow> rows = FXCollections.observableArrayList();
        for (Stock stock : _stock) {
            StockFileRow row = new StockFileRow();
            row.setArea(stock.getArea());
            row.setItem(stock.getPackedItem().getItem());
            row.setUnit(stock.getPackedItem().getUnit());
            row.setQuantity(stock.getQuantity());
            row.setAdjustedQuantity(0);
            rows.add(row);
        }
        return rows;
    }
    
    public static ObservableList<Stock> convertRowsRoStocks(List<StockFileRow> _rows) {
        ObservableList<Stock> stocks = FXCollections.observableArrayList();
        for (StockFileRow row : _rows) {
            Stock stock = Stock.getStocksById(PackedItem.getPackedItem(row.getItem(), row.getUnit()),
                                row.getArea());
            stocks.add(stock);
        }
        return stocks;
    }
    
    public static Stock getStocksById(PackedItem  _item, Area _area) {
        Stock stock = null;
        try {
            stock = Environment.getEntityManager()
                              .createNamedQuery("Stock.findById", Stock.class)
                              .setParameter("packedItem", _item)
                              .setParameter("area", _area)
                              .getSingleResult();
        }
        catch(NoResultException e) {
            stock = null ;
            e.printStackTrace();
        }
        return stock;
    }
    
    public static ObservableList<Stock> getStocksBasedOnArea(Area _area) {
        ObservableList<Stock> stocks = FXCollections.observableArrayList(Environment.getEntityManager()
                                                  .createNamedQuery("Stock.findByArea", Stock.class)
                                                  .setParameter("area", _area)
                                                  .getResultList());
        
        return PropertyObject.refreshProperies(stocks);
    }
    
    public static double getStockQuantity(PackedItem _item, Area _area) {
        try {
            Statement stmt = Environment.getConnection().createStatement();
            ResultSet rs = null;
            double qty = 0;
            String sql = " SELECT quantity FROM Stock ";
            sql += " WHERE item_id = " + _item.getItem().getId() ;
            sql += " AND unit_id = " + _item.getUnit().getId() ;
            sql += " AND area_id = " + _area.getId();
            rs = stmt.executeQuery(sql);
            if(!rs.next())
                return 0;
            qty = rs.getDouble(1);
            return qty;
        }
        catch(SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public static void addStock(PackedItem _item, Area _area, double _quantity) {
        try {
            Statement stmt = Environment.getConnection().createStatement();
            String sql = "SELECT add_stock(" + _item.getItem().getId() + ", " + _item.getUnit().getId() +
                         ", " + _area.getId() + ", " + _quantity + " )";
            stmt.executeQuery(sql);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
    }
    
    public static boolean removeStock(PackedItem _item, Area _area, double _quantity) {
        boolean result = true;
        try {
            Statement stmt = Environment.getConnection().createStatement();
            String sql = "SELECT remove_stock(" + _item.getItem().getId() + ", " + _item.getUnit().getId() +
                         ", " + _area.getId() + ", " + _quantity + " )";
            ResultSet rs = null ;
            rs = stmt.executeQuery(sql);
            rs.next();
            result = rs.getBoolean(1);
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public PackedItem getPackedItem() {
        return v_packedItem;
    }
    
    public void setPackedItem(PackedItem _item) {
        v_packedItem = _item;
        v_packedItemProperty.setValue(v_packedItem);
    }
    
    public SimpleObjectProperty packedItemProperty() {
        return v_packedItemProperty;
    }
    
    public Area getArea() {
        return v_area;
    }
    
    public void setArea(Area _area) {
        v_area = _area;
        v_areaProperty.setValue(v_area);
    }
    
    public SimpleObjectProperty areaProperty() {
        return v_areaProperty;
    }
    
    public double getQuantity() {
        return v_quantity;
    }
    
    public void setQuantity(double _qty) {
        v_quantity = _qty;
        v_quantityProperty.setValue(v_quantity);
    }
    
    public SimpleDoubleProperty quantityProperty() {
        return v_quantityProperty;
    }

    @Override
    public void refreshProperty() {
        v_packedItemProperty.setValue(v_packedItem);
        v_areaProperty.setValue(v_area);
        v_quantityProperty.setValue(v_quantity);
    }
    
}
