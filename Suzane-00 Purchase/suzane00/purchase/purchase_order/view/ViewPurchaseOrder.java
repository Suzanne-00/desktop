/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order.view;

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
import suzane00.source.Supplier;
import suzane00.purchase.purchase_order.PurchaseOrder;

/**
 *
 * @author Usere
 */
public class ViewPurchaseOrder extends VBox implements Observable {

    protected LinkedList<Observer> m_observers;
    
    /*----------------------------------------------- GUI COMPONENTS ---------------------------------------------- */

    
    /* level 1 area */
    
    StandardViewHeaderArea area_header ;
    protected TableView<PurchaseOrder> tbl_po;
    
    /*level 2 area */
    
    // area_header
    protected BorderPane area_upperHeader;
    
    /* level 3 area */
    
    // area_header -> area_upperHeader
    protected GridPane area_leftHeader;
    protected GridPane area_rightHeader;
    
    /* level 4 area */
    
    // area_header -> area_upperHeader ->  area_leftHeader
    protected Label lbl_poNumber = new Label("PO number: ");
    protected Label lbl_supplier = new Label("Supplier: ");
    protected Label lbl_area = new Label("Area: ");
    protected Label lbl_note = new Label("Note: ");
    protected Label lbl_issueDate = new Label("Issue date: ");
    protected Label lbl_dueDate = new Label("Due date: ");
    protected Label lbl_issueDateTo = new Label(" to ");
    protected Label lbl_dueDateTo = new Label(" to ");
    protected TextField txt_poNumber = new TextField();
    protected TextField txt_note = new TextField();
    protected ComboBox cmb_supplier = new ComboBox();
    protected ComboBox cmb_area = new ComboBox();
        
    // area_header -> area_upperHeader -> area_rightHeader
    protected DatePicker dtp_dueDateFrom = new DatePicker();
    protected DatePicker dtp_issueDateFrom = new DatePicker();
    protected DatePicker dtp_dueDateTo = new DatePicker();
    protected DatePicker dtp_issueDateTo = new DatePicker();
        
    /*----------------------------------------------- END GUI COMPONENTS ---------------------------------------------- */
    
    
    public ViewPurchaseOrder() {
        setAppearance();
        setData();
        setActionPerformed();
    }
    
    public TableView getTable() {
        return tbl_po;
    }
    
    public ObservableList<PurchaseOrder> getSelectedItems() {
        return tbl_po.getSelectionModel().getSelectedItems();
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
            observer.update(this, tbl_po.getSelectionModel().getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        configurePOTable();
        loadComboSupplier();
        loadComboArea();
    }
    
    protected  void setActionPerformed() {
        area_header.setButtonSearchActionPerformed(
                e -> {
                    String num = txt_poNumber.getText();
                    Supplier supplier = (Supplier) cmb_supplier.getValue();
                    Area area = (Area) cmb_area.getValue();
                    String note = txt_note.getText();
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
                    
                    tbl_po.setItems(
                            FXCollections.observableArrayList(PurchaseOrder.getPOSByAttributes(
                                    num, supplier, area, note, issueFrom, issueTo, dueFrom, dueTo
                                )
                            )
                    );
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setHeaderArea();
        setBackground();
        tbl_po = new TableView<>();
        tbl_po.setPrefWidth(Utility.STANDARD_TABLE_WIDTH);
        tbl_po.setPrefHeight(Utility.STANDARD_TABLE_HEIGHT);
        setVgrow(tbl_po, Priority.ALWAYS);
        setSpacing(Utility.STANDARD_GAP);
        getChildren().addAll(area_header, tbl_po);
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
        lbl_poNumber = new Label("PO number: ");
        lbl_supplier = new Label("Supplier: ");
        lbl_area = new Label("Area: ");
        lbl_note = new Label("Note: ");
        lbl_issueDate = new Label("Issue date: ");
        lbl_dueDate = new Label("Due date: ");
        lbl_issueDateTo = new Label(" to ");
        lbl_dueDateTo = new Label(" to ");
        txt_poNumber = new TextField();
        txt_note = new TextField();
        cmb_supplier = new ComboBox();
        cmb_area = new ComboBox();
        
        area_leftHeader = new GridPane();
        
        txt_poNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_note.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_supplier.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_area.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_leftHeader.setHgap(Utility.SHORT_GAP);
        area_leftHeader.setVgap(Utility.SHORT_GAP);
        
        area_leftHeader.add(lbl_poNumber, 0, 0);
        area_leftHeader.add(txt_poNumber, 1, 0);
        area_leftHeader.add(lbl_supplier, 0, 1);
        area_leftHeader.add(cmb_supplier, 1, 1);
        area_leftHeader.add(lbl_area, 0, 2);
        area_leftHeader.add(cmb_area, 1, 2);
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
    
    protected void setBackground() {
        area_leftHeader.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        area_rightHeader.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        area_upperHeader.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
        area_header.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
    protected void configurePOTable() {
        TableColumn poNumbCol = new TableColumn("PO Number");
        poNumbCol.setMinWidth(100);
        poNumbCol.setCellValueFactory(
                new PropertyValueFactory<>("transactionNumber"));
 
        TableColumn supplierCol = new TableColumn("Supplier");
        supplierCol.setMinWidth(100);
        supplierCol.setCellValueFactory(
                new PropertyValueFactory<>("supplier"));
        
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
 
        tbl_po.getColumns().addAll(poNumbCol, supplierCol, areaCol, noteCol, 
                                      issueDateCol, dueDateCol);
    }
    
    protected void loadComboSupplier() {
        cmb_supplier.setItems(Supplier.getAllSuppliers());
    }
    
    protected void loadComboArea() {
        cmb_area.setItems(Area.getAllAreas());
    }
}
