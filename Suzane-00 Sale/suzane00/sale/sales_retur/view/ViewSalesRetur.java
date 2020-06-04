/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_retur.view;

import suzane00.sale.delivery_order.view.*;
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
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardViewHeaderArea;
import suzane00.inventory.Area;
import suzane00.inventory.Item;
import suzane00.inventory.ProductType;
import suzane00.inventory.Unit;
import suzane00.sale.delivery_order.DeliveryOrder;
import suzane00.sale.sales_order.SalesOrder;
import suzane00.sale.sales_retur.SalesRetur;
import suzane00.sale.stock_reservation.StockReservation;
import suzane00.source.Address;
import suzane00.source.Customer;
import suzane00.source.Source;
import suzane00.source.Supplier;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
public class ViewSalesRetur extends VBox implements Observable {

    protected LinkedList<Observer> m_observers;
    protected SalesRetur v_salesRetur;
    protected DeliveryOrder v_deliveryOrder;
    
    /*----------------------------------------------- GUI COMPONENTS ---------------------------------------------- */

    
    /* level 1 area */
    
    protected StandardViewHeaderArea area_header ;
    protected TableView<SalesRetur> tbl_st;
    
    /*level 2 area */
    
    // area_header
    protected BorderPane area_upperHeader;
    
    /* level 3 area */
    
    // area_header -> area_upperHeader
    protected GridPane area_leftHeader;
    protected GridPane area_rightHeader;
    
    /* level 4 area */
    
    // area_header -> area_upperHeader ->  area_leftHeader
    protected Label lbl_stNumber;
    protected Label lbl_doNumber;
    protected Label lbl_customer;
    protected Label lbl_area;
    protected Label lbl_note;
    protected TextField txt_stNumber;
    protected TextField txt_doNumber;
    protected TextField txt_note;
    protected ComboBox<Customer> cmb_customer;
    protected ComboBox<Area> cmb_area;
    protected Button btn_pickDO;
    protected Button btn_viewDO;
        
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
    
    
    public ViewSalesRetur() {
        setAppearance();
        setData();
        setActionPerformed();
    }
    
    public TableView getTable() {
        return tbl_st;
    }
    
    public ObservableList<SalesRetur> getSelectedItems() {
        return tbl_st.getSelectionModel().getSelectedItems();
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
            observer.update(this, tbl_st.getSelectionModel().getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        configureTable();
        loadCustomerData();
        loadAreaData();
    }
    
    protected  void setActionPerformed() {
        area_header.setButtonSearchActionPerformed(e -> {
                    String num = txt_stNumber.getText();
                    String note = txt_note.getText();
                    Customer cust = (Customer) cmb_customer.getValue();
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
                    
                    tbl_st.setItems(SalesRetur.getSTSByAttributes(num, v_deliveryOrder, cust, area, 
                            note, issueFrom, issueTo, dueFrom, dueTo)
                    );
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setHeaderArea();
        tbl_st = new TableView<>();
        tbl_st.setPrefWidth(Utility.STANDARD_TABLE_WIDTH);
        tbl_st.setPrefHeight(Utility.STANDARD_TABLE_HEIGHT);
        VBox.setVgrow(tbl_st, Priority.ALWAYS);
        setSpacing(Utility.STANDARD_GAP);
        getChildren().addAll(area_header, tbl_st);
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
        lbl_stNumber = new Label("Retur number: ");
        lbl_doNumber = new Label("DO number: ");
        lbl_customer = new Label("Customer: ");
        lbl_area = new Label("Address: ");
        lbl_note = new Label("Note: ");
        lbl_issueDate = new Label("Issue date: ");
        lbl_dueDate = new Label("Due date: ");
        lbl_issueDateTo = new Label(" to ");
        lbl_dueDateTo = new Label(" to ");
        txt_stNumber = new TextField();
        txt_doNumber = new TextField();
        txt_note = new TextField();
        cmb_customer = new ComboBox();
        cmb_area = new ComboBox();
        btn_pickDO = new Button(Utility.STANDARD_EXPAND_ICON);
        btn_viewDO = new Button("View DO");
        area_leftHeader = new GridPane();
        
        txt_stNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_note.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_doNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_leftHeader.setHgap(Utility.SHORT_GAP);
        area_leftHeader.setVgap(Utility.SHORT_GAP);
        
        area_leftHeader.add(lbl_stNumber, 0, 0);
        area_leftHeader.add(txt_stNumber, 1, 0);
        area_leftHeader.add(lbl_doNumber, 0, 1);
        area_leftHeader.add(txt_doNumber, 1, 1);
        area_leftHeader.add(lbl_customer, 0, 2);
        area_leftHeader.add(cmb_customer, 1, 2);
        area_leftHeader.add(lbl_area, 0, 3);
        area_leftHeader.add(cmb_area, 1, 3);
        area_leftHeader.add(btn_pickDO, 2, 1);
        area_leftHeader.add(btn_viewDO, 3, 1);
        area_leftHeader.add(lbl_note, 0, 4);
        area_leftHeader.add(txt_note, 1, 4);
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
        TableColumn stNumbCol = new TableColumn("Retur Number");
        stNumbCol.setMinWidth(100);
        stNumbCol.setCellValueFactory(
                new PropertyValueFactory<>("transactionNumber"));
 
        TableColumn reservationNumbCol = new TableColumn("DO Number");
        reservationNumbCol.setMinWidth(100);
        reservationNumbCol.setCellValueFactory(
                new PropertyValueFactory<>("deliveryOrder"));
        
        TableColumn areaCol = new TableColumn("Area");
        areaCol.setMinWidth(100);
        areaCol.setCellValueFactory(
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
 
        tbl_st.getColumns().addAll(stNumbCol, reservationNumbCol, noteCol, 
                                      issueDateCol, dueDateCol);
    }
    
    protected void loadCustomerData() {
        cmb_customer.setItems(Customer.getAllCustomers());
    }
    
    protected void loadAreaData() {
        cmb_area.setItems(Area.getAllAreas());
    }
}
