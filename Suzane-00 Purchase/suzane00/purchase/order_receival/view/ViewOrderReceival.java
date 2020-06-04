/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.order_receival.view;

import suzane00.purchase.vendor_invoice.view.*;
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
import suzane00.purchase.order_receival.OrderReceival;
import suzane00.purchase.purchase_order.PurchaseOrder;
import suzane00.purchase.vendor_invoice.VendorInvoice;
import suzane00.source.Customer;
import suzane00.source.Supplier;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
public class ViewOrderReceival extends VBox implements Observable {

    protected LinkedList<Observer> m_observers;
    protected OrderReceival v_orderReceival;
    protected PurchaseOrder v_purchaseOrder;
    
    /*----------------------------------------------- GUI COMPONENTS ---------------------------------------------- */

    
    /* level 1 area */
    
    protected StandardViewHeaderArea area_header ;
    protected TableView<OrderReceival> tbl_or;
    
    /*level 2 area */
    
    // area_header
    protected BorderPane area_upperHeader;
    
    /* level 3 area */
    
    // area_header -> area_upperHeader
    protected GridPane area_leftHeader;
    protected GridPane area_rightHeader;
    
    /* level 4 area */
    
    // area_header -> area_upperHeader ->  area_leftHeader
    protected Label lbl_supplierDO;
    protected Label lbl_poNumber;
    protected Label lbl_area;
    protected Label lbl_note;
    protected TextField txt_supplierDONumber;
    protected TextField txt_poNumber;
    protected TextField txt_note;
    protected ComboBox cmb_area;
    protected Button btn_pickPO;
    protected Button btn_viewPO;
        
    // area_header -> area_upperHeader -> area_rightHeader
    protected Label lbl_issueDate;
    protected Label lbl_dueDate;
    protected Label lbl_issueDateTo;
    protected Label lbl_dueDateTo;
    protected DatePicker dtp_dueDateFrom;
    protected DatePicker dtp_issueDateFrom;
    protected DatePicker dtp_dueDateTo;
    protected DatePicker dtp_issueDateTo;
        
    /*----------------------------------------------- END GUI COMPONENTS ---------------------------------------------- */
    
    
    public ViewOrderReceival() {
        setAppearance();
        setData();
        setActionPerformed();
    }
    
    public TableView getTable() {
        return tbl_or;
    }
    
    public ObservableList<OrderReceival> getSelectedItems() {
        return tbl_or.getSelectionModel().getSelectedItems();
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
            observer.update(this, tbl_or.getSelectionModel().getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        configureTable();
        loadComboAreaData();
    }
    
    protected  void setActionPerformed() {
        area_header.setButtonSearchActionPerformed(e -> {
                    String num = txt_supplierDONumber.getText();
                    String note = txt_note.getText();
                    Area area = (Area) cmb_area.getValue();
                    Calendar issueFrom = null;
                    Calendar issueTo = null;
                    Calendar dueFrom = null; 
                    Calendar dueTo = null;
                    
                    if (dtp_issueDateFrom.getValue() != null) {
                        issueFrom = Calendar.getInstance();
                        issueFrom.setTime(Date.valueOf(dtp_issueDateFrom.getValue()));
                    }
                    
                    if (dtp_issueDateTo.getValue() != null) {
                        issueTo = Calendar.getInstance();
                        issueTo.setTime(Date.valueOf(dtp_issueDateTo.getValue()));
                    }
                    
                    if (dtp_dueDateFrom.getValue() != null ) {
                        dueFrom = Calendar.getInstance();
                        dueFrom.setTime(Date.valueOf(dtp_dueDateFrom.getValue()));
                    }
                    
                    if (dtp_dueDateTo.getValue() != null) {
                        dueTo = Calendar.getInstance();
                        dueTo.setTime(Date.valueOf(dtp_dueDateTo.getValue()));
                    }
                    
//                    for (PurchaseOrder po : PurchaseOrder.getPOSByAttributes(
//                                    num, supplier, area, note, issueFrom, issueTo, dueFrom, dueTo)) {
//                    }
                    
                    tbl_or.setItems(OrderReceival.getORSByAttributes(num, v_purchaseOrder, area, 
                            note, issueFrom, issueTo, dueFrom, dueTo)
                    );
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setHeaderArea();
        tbl_or = new TableView<>();
        tbl_or.setPrefWidth(Utility.STANDARD_TABLE_WIDTH);
        tbl_or.setPrefHeight(Utility.STANDARD_TABLE_HEIGHT);
        setSpacing(Utility.STANDARD_GAP);
        getChildren().addAll(area_header, tbl_or);
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
        setRightHeaderArea();
        area_upperHeader = new BorderPane();
        area_upperHeader.setLeft(area_leftHeader);
        area_upperHeader.setRight(area_rightHeader);
    }
    
    /* level 3 area */
    
    // setHeaderArea() -> setUpperHeaderArea()
    protected void setLeftHeaderArea() {
        lbl_supplierDO = new Label("Supplier DO number: ");
        lbl_poNumber = new Label("PO number: ");
        lbl_area = new Label("Area: ");
        lbl_note = new Label("Note: ");
        lbl_issueDate = new Label("Issue date: ");
        lbl_dueDate = new Label("Due date: ");
        lbl_issueDateTo = new Label(" to ");
        lbl_dueDateTo = new Label(" to ");
        txt_supplierDONumber = new TextField();
        txt_poNumber = new TextField();
        txt_note = new TextField();
        cmb_area = new ComboBox();
        btn_pickPO = new Button(Utility.STANDARD_EXPAND_ICON);
        btn_viewPO = new Button("View PO");
        area_leftHeader = new GridPane();
        
        txt_supplierDONumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_note.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_poNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_leftHeader.setHgap(Utility.SHORT_GAP);
        area_leftHeader.setVgap(Utility.SHORT_GAP);
        
        area_leftHeader.add(lbl_supplierDO, 0, 0);
        area_leftHeader.add(txt_supplierDONumber, 1, 0);
        area_leftHeader.add(lbl_poNumber, 0, 1);
        area_leftHeader.add(txt_poNumber, 1, 1);
        area_leftHeader.add(lbl_area, 0, 2);
        area_leftHeader.add(cmb_area, 1, 2);
        area_leftHeader.add(btn_pickPO, 2, 1);
        area_leftHeader.add(btn_viewPO, 3, 1);
        area_leftHeader.add(lbl_note, 0, 3);
        area_leftHeader.add(txt_note, 1, 3);
    }
    
    protected void setRightHeaderArea() {
        dtp_dueDateFrom = new DatePicker();
        dtp_issueDateFrom = new DatePicker();
        dtp_dueDateTo = new DatePicker();
        dtp_issueDateTo = new DatePicker();
        area_rightHeader = new GridPane();
        
        dtp_issueDateFrom.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        dtp_issueDateTo.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        dtp_dueDateFrom.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        dtp_dueDateTo.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_rightHeader.setHgap(Utility.SHORT_GAP);
        area_rightHeader.setVgap(Utility.SHORT_GAP);
        
        area_rightHeader.add(lbl_issueDate, 0, 0);
        area_rightHeader.add(dtp_issueDateFrom, 1, 0);
        area_rightHeader.add(lbl_issueDateTo, 2, 0);
        area_rightHeader.add(dtp_issueDateTo, 3, 0);
        area_rightHeader.add(lbl_dueDate, 0, 1);
        area_rightHeader.add(dtp_dueDateFrom, 1, 1);
        area_rightHeader.add(lbl_dueDateTo, 2, 1);
        area_rightHeader.add(dtp_dueDateTo, 3, 1);
    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
    protected void configureTable() {
        TableColumn supplierDONumbCol = new TableColumn("Supplier DO Number");
        supplierDONumbCol.setMinWidth(100);
        supplierDONumbCol.setCellValueFactory(
                new PropertyValueFactory<>("transactionNumber"));
 
        TableColumn poNumbCol = new TableColumn("PO Number");
        poNumbCol.setMinWidth(100);
        poNumbCol.setCellValueFactory(
                new PropertyValueFactory<>("purchaseOrder"));
        
        TableColumn areaCol = new TableColumn("Area");
        poNumbCol.setMinWidth(100);
        poNumbCol.setCellValueFactory(
                new PropertyValueFactory<>("area"));
        
        TableColumn noteCol = new TableColumn("Note");
        noteCol.setMinWidth(100);
        noteCol.setCellValueFactory(
                new PropertyValueFactory<>("note"));
 
        TableColumn issueDateCol = new TableColumn("Issue Date");
        issueDateCol.setMinWidth(100);
        issueDateCol.setCellValueFactory(
                new PropertyValueFactory<>("issueDate"));
        
        TableColumn dueDateCol = new TableColumn("Due Date");
        dueDateCol.setMinWidth(100);
        dueDateCol.setCellValueFactory(
                new PropertyValueFactory<>("dueDate"));
 
        tbl_or.getColumns().addAll(supplierDONumbCol, poNumbCol, noteCol, 
                                      issueDateCol, dueDateCol);
    }
    
    protected void loadComboAreaData() {
        cmb_area.setItems(Area.getAllAreas());
    }
}
