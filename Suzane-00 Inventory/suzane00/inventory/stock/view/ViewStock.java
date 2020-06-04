/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.stock.view;

import suzane00.inventory.view.*;
import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardViewHeaderArea;
import suzane00.inventory.Area;
import suzane00.inventory.Item;
import suzane00.inventory.ProductType;
import suzane00.inventory.Unit;
import suzane00.inventory.custom_ui.NumberTextField;
import suzane00.inventory.model.StockFileRow;
import suzane00.inventory.stock.Stock;

/**
 *
 * @author Usere
 */
public class ViewStock extends VBox implements Observable {

    protected LinkedList<Observer> m_observers;
    
    /*----------------------------------------------- GUI COMPONENTS ---------------------------------------------- */

    
    /* level 1 area */
    
    protected StandardViewHeaderArea area_header ;
    protected TableView<StockFileRow> tbl_stock;
    
     /*level 2 area */
    
    // area_header
    protected BorderPane area_upperHeader;
    
    /* level 3 area */
    
    // area_header -> area_upperHeader
    protected GridPane area_leftHeader;
    protected GridPane area_rightHeader;
    
    /* level 4 area */
    
    // area_header -> area_upperHeader ->  area_leftHeade
   
    protected Label lbl_area;
    protected Label lbl_minQty;
    protected Label lbl_maxQty;
    protected Label lbl_item;
    protected Label lbl_unit;
   
    protected ComboBox cmb_area;
    protected ComboBox cmb_item;
    protected ComboBox cmb_unit;
    protected NumberTextField ntxt_minQty;
    protected NumberTextField ntxt_maxQty;
    
    /*----------------------------------------------- END GUI COMPONENTS ---------------------------------------------- */
    
    
    public ViewStock() {
        setAppearance();
        setData();
        setActionPerformed();
    }
    
    public TableView getTable() {
        return tbl_stock;
    }
    
    public ObservableList<Stock> getSelectedItems() {
        ObservableList<Stock> stocks = Stock.convertRowsRoStocks(tbl_stock.getItems());
        return stocks;
    }
    
    public void searchStocksBasedOnArea(Area _area) {
        ObservableList<StockFileRow> stocks = Stock.convertStocksToRows(Stock.getStocksBasedOnArea(_area));
        tbl_stock.setItems(stocks);
    }
    
    @Override
    public void addObserver(Observer _o) {
        if( m_observers == null)
            m_observers = new LinkedList<>();
        
        m_observers.add(_o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : m_observers) {
            observer.update(this, tbl_stock.getSelectionModel().getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        configureTable();
        loadComboAreaData();
        loadComboItemData();
        loadColboUnitData();
    }
    
    protected  void setActionPerformed() {
        area_header.setButtonSearchActionPerformed(e -> {
                    Area area = (Area) cmb_area.getValue();
                    Item item = (Item) cmb_item.getValue();
                    Unit unit = (Unit) cmb_unit.getValue();
                    Double minQty = null;
                    Double maxQty = null;
                    Calendar dueFrom = null; 
                    Calendar dueTo = null;
                    ObservableList<StockFileRow> stocks = Stock.convertStocksToRows(
                            Stock.getStockByAttributes(area, item, unit, minQty, maxQty));
                    tbl_stock.setItems(stocks); 
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setHeaderArea();
        tbl_stock = new TableView<>();
        tbl_stock.setPrefWidth(Utility.STANDARD_TABLE_WIDTH);
        tbl_stock.setPrefHeight(Utility.STANDARD_TABLE_HEIGHT);
        setSpacing(Utility.STANDARD_GAP);
        getChildren().addAll(area_header, tbl_stock);
    }
    
    /* level 1 area */
    
    protected void setHeaderArea() {
        setUpperHeaderArea();
        area_header = new StandardViewHeaderArea();
        area_header.setContent(area_upperHeader);
    }
    
    /* level 2 area */
    
    //setUpperHeaderArea()
    protected void setUpperHeaderArea() {
        setLeftHeaderArea();
        area_upperHeader = new BorderPane();
        area_upperHeader.setLeft(area_leftHeader);
    }
    
    /* level 3 area */
    
    // setHeaderArea() -> setUpperHeaderArea()
    protected void setLeftHeaderArea() {
        lbl_area = new Label("Area: ");
        lbl_item = new Label("Item: ");
        lbl_unit = new Label("Unit: ");
        lbl_minQty = new Label("Qty: ");
        lbl_maxQty = new Label("To: ");
        cmb_area = new ComboBox();
        cmb_item = new ComboBox();
        cmb_unit = new ComboBox();
        ntxt_minQty = new NumberTextField();
        ntxt_maxQty = new NumberTextField();
        area_leftHeader = new GridPane();
        
        ntxt_minQty.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        ntxt_maxQty.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_area.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_item.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_unit.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_leftHeader.setHgap(Utility.SHORT_GAP);
        area_leftHeader.setVgap(Utility.SHORT_GAP);
        
        area_leftHeader.add(lbl_area, 0, 0);
        area_leftHeader.add(cmb_area, 1, 0);
        area_leftHeader.add(lbl_item, 0, 1);
        area_leftHeader.add(cmb_item, 1, 1);
        area_leftHeader.add(lbl_unit, 0, 2);
        area_leftHeader.add(cmb_unit, 1, 2);
        area_leftHeader.add(lbl_minQty, 0, 3);
        area_leftHeader.add(ntxt_minQty, 1, 3);
        area_leftHeader.add(lbl_maxQty, 2, 3);
        area_leftHeader.add(ntxt_maxQty, 3, 3);
    }
    
//    protected void setRightHeaderArea() {
//        dtp_dueDateFrom = new DatePicker();
//        dtp_issueDateFrom = new DatePicker();
//        dtp_dueDateTo = new DatePicker();
//        dtp_issueDateTo = new DatePicker();
//        area_rightHeader = new GridPane();
//        
//        dtp_issueDateFrom.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
//        dtp_issueDateTo.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
//        dtp_dueDateFrom.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
//        dtp_dueDateTo.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
//        area_rightHeader.setHgap(Utility.SHORT_GAP);
//        area_rightHeader.setVgap(Utility.SHORT_GAP);
//        
//        area_rightHeader.add(lbl_issueDate, 0, 0);
//        area_rightHeader.add(dtp_issueDateFrom, 1, 0);
//        area_rightHeader.add(lbl_issueDateTo, 2, 0);
//        area_rightHeader.add(dtp_issueDateTo, 3, 0);
//        area_rightHeader.add(lbl_dueDate, 0, 1);
//        area_rightHeader.add(dtp_dueDateFrom, 1, 1);
//        area_rightHeader.add(lbl_dueDateTo, 2, 1);
//        area_rightHeader.add(dtp_dueDateTo, 3, 1);
//    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
    protected void configureTable() {
        TableColumn areaCol = new TableColumn("Area");
        areaCol.setMinWidth(100);
        areaCol.setCellValueFactory(
                new PropertyValueFactory<>("area"));
 
        TableColumn itemCol = new TableColumn("Item");
        itemCol.setMinWidth(100);
        itemCol.setCellValueFactory(
                new PropertyValueFactory<>("item"));
        
        TableColumn unitCol = new TableColumn("Unit");
        unitCol.setMinWidth(100);
        unitCol.setCellValueFactory(
                new PropertyValueFactory<>("unit"));
        
        TableColumn qtyCol = new TableColumn("Quantity");
        qtyCol.setMinWidth(100);
        qtyCol.setCellValueFactory(
                new PropertyValueFactory<>("quantity"));
 
 
        tbl_stock.getColumns().addAll(areaCol, itemCol, unitCol, qtyCol);
        tbl_stock.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }
    
    protected void loadComboAreaData() {
        cmb_area.setItems(Area.getAllAreas());
    }
    
    protected void loadComboItemData() {
        cmb_item.setItems(Item.getAllItems());
    }
    
    protected void loadColboUnitData() {
        cmb_unit.setItems(Unit.getAllUnits());
    }
    
    
}
