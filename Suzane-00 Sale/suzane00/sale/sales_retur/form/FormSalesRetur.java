/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_retur.form;

import suzane00.sale.delivery_order.form.*;
import suzane00.sale.stock_reservation.form.*;
import com.sun.jmx.remote.util.EnvHelp;
import java.sql.Time;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import javax.management.StringValueExp;
import suzane00.global.Environment;
import suzane00.global.FormConstant;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.transaction.SequenceGenerator;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardBigFormBottomArea;
import suzane00.inventory.Area;
import suzane00.inventory.Cost;
import suzane00.inventory.Item;
import suzane00.inventory.PackedItem;
import suzane00.inventory.PackedItemPK;
import suzane00.inventory.Unit;
import suzane00.inventory.form.FormPickItem;
import suzane00.inventory.form.FormArea;
import suzane00.inventory.ItemPrice;
import suzane00.inventory.SellPriceType;
import suzane00.transaction.custom_ui.IFRUnitComboBox;
import suzane00.inventory.custom_ui.NumberTextField;
import suzane00.inventory.stock.Stock;
import suzane00.sale.delivery_order.DOFile;
import suzane00.sale.delivery_order.DeliveryOrder;
import suzane00.sale.sales_order.SOFile;
import suzane00.sale.sales_order.SalesOrder;
import suzane00.sale.sales_order.form.FormPickSalesOrder;
import suzane00.sale.sales_order.form.FormSalesOrder;
import suzane00.sale.sales_retur.STFile;
import suzane00.sale.sales_retur.SalesRetur;
import suzane00.sale.stock_reservation.SRFile;
import suzane00.sale.stock_reservation.StockReservation;
import suzane00.source.Address;
import suzane00.source.Customer;
import suzane00.source.Supplier;
import suzane00.source.form.FormAddAddress;
import suzane00.source.form.FormSupplier;
import suzane00.transaction.Transaction;
import suzane00.transaction.custom_ui.MQIFRUnitComboBox;
import suzane00.transaction.form.FormAddCharges;
import suzane00.transaction.model.ItemFileRow;
import suzane00.transaction.model.MQItemFileRow;

/**
 *
 * @author Usere
 */
public class FormSalesRetur extends Stage {
    
    protected static final int COL_ID = 0 ;
    protected static final int COL_CODE = 1 ;
    protected static final int COL_NAME = 2 ;
    protected static final int COL_ORIGIN_QTY = 3 ;
    protected static final int COL_QTY = 4 ;
    protected static final int COL_UNIT = 5 ;
    
    protected SalesRetur v_salesRetur ;
    protected DeliveryOrder v_deliveryOrder ;
    protected Item v_item ;
    protected int v_mode = FormConstant.MODE_NEW;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    protected VBox area_main;
    
    /* level 1 area */
    
    protected BorderPane area_header;
    protected VBox area_content;
    protected BorderPane area_footer;
    protected StandardBigFormBottomArea area_bottom;   
    
    /* level 2 area */
    
    // area_header
    protected GridPane area_leftHeader;
    protected GridPane area_rightHeader;
    
    //area_content
    protected GridPane area_upperContent;    
    protected TableView<MQItemFileRow> tbl_item;
    
    // area_footer
    protected GridPane area_leftFooter;
    protected GridPane area_rightFooter;
    
    /* level 3 area */
    
    //area_header -> area_leftHeader
    protected Label lbl_returNumber;
    protected Label lbl_doNumber;
    protected Label lbl_area;
    protected TextField txt_returNumber;
    protected TextField txt_doNumber;
    protected ComboBox<Area> cmb_area;
    protected Button btn_pickDO;
    protected Button btn_viewDO;
    
    // area_header -> area_rightHeader
    protected Label lbl_issueDate;
    protected Label lbl_dueDate;
    DatePicker dtp_issueDate;
    DatePicker dtp_dueDate;
    
    // area_content -> area_upperContent
    protected Label lbl_itemCode;
    protected Label lbl_itemName;
    protected Label lbl_itemQty;
//    protected Label lbl_itemDiscount;
//    protected Label lbl_itemPrice;
    protected Label lbl_itemUnit;
    protected TextField txt_itemCode;
    protected TextField txt_itemName;
    protected NumberTextField ntxt_itemQty;
//    protected NumberTextField ntxt_itemDiscount;
//    protected NumberTextField ntxt_itemPrice;
    protected ComboBox cmb_itemUnit;
    protected Button btn_add;
    protected Button btn_item ;
    //protected Button btn_itemDisc;
    
    // area_footer -> area_leftFooter
    protected Label lbl_note;
    protected TextArea txa_note;
    
    // area_footer -> area_rightFooter
//    protected Label lbl_amount;
//    protected Label lbl_discount;
//    protected Label lbl_miscCharge;
    protected Label lbl_total;  
//    protected NumberTextField ntxt_amount;
//    protected NumberTextField ntxt_discount;
//    protected NumberTextField ntxt_miscCharge;
    protected NumberTextField ntxt_total;  
//    protected Button btn_disc;
//    protected Button btn_miscCharge;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    public Node getRoot() {
        return area_main ;
    }
    
    public FormSalesRetur() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        setScene(scene);
    }
    
    public FormSalesRetur(SalesRetur _st, int _mode) {
        this();
        setVariables(_st, _mode);
        disiplayData();
        if (v_mode == FormConstant.MODE_VIEW) {
            disableEditing(true);
            //setViewActionPerformed();
        }
    }
    
    protected void initComponents() {
        v_salesRetur = null;
        v_item = null;
        v_mode = FormConstant.MODE_NEW;;
    }
    
    protected void setAppearance() {
        setMainArea() ;
    }
    
    protected void setData() {
        configureItemTableByRow();
        loadAreaData();
    }
    
    protected void setActionPerformed() {
        setMainButtonsActionsPerformed();
        setButtonsActionsPerformed();
        setTableActionPerformed();
    }
    
    protected void setVariables(SalesRetur _st, int _mode) {
        v_salesRetur = _st;
        v_deliveryOrder = _st.getDeliveryOrder();
        v_mode = _mode;
    }
    
    protected void disiplayData() {
        Date issueDate = v_salesRetur.getIssueDate().getTime();
        LocalDate issueLocalDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date dueDate = v_salesRetur.getDueDate().getTime();
        LocalDate dueLocalDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        SalesOrder so = null;
        txt_returNumber.setText(v_salesRetur.getTransactionNumber());
        txt_doNumber.setText(v_salesRetur.getDeliveryOrder().getTransactionNumber());
        txa_note.setText(v_salesRetur.getNote());
        cmb_area.setValue(v_salesRetur.getArea());
        
        dtp_issueDate.setValue(issueLocalDate);
        dtp_dueDate.setValue(dueLocalDate);
        List<MQItemFileRow> stRows = STFile.convertFilesToRows(v_salesRetur.getFiles());
        List<MQItemFileRow> doRows = DOFile.convertFilesToRows(v_salesRetur.getDeliveryOrder()
                                         .getFiles());
        so = v_deliveryOrder.getStockreservation().getSalesOrder();
        for (MQItemFileRow stRow : stRows) {
            for (ItemFileRow doRow : doRows) {
                if(stRow.getId() == doRow.getId() && stRow.getUnit().equals(doRow.getUnit())) {
                    stRow.setOriginalQuantity(doRow.getQuantity());
                }
            }
            
            for (SOFile soFile : so.getFiles()) {
                if(soFile.getPackedItem().getItem().getId().equals(stRow.getId()) &&
                   soFile.getPackedItem().getUnit().equals(stRow.getUnit())) {
                        stRow.setPrice(soFile.getPrice());
                        break;
                }
            }
        }
        
        tbl_item.getItems().addAll(stRows);
    }
    
//    protected void setViewActionPerformed() {
//    }
        
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setHeaderArea();
        setContentArea();
        setFooterArea();
        setBottomArea();
        area_main = new VBox(area_header, area_content, area_footer, area_bottom);
        area_main.setPrefSize(Utility.STANDARD_FORM_WIDTH, Utility.STANDARD_FORM_HEIGHT);
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_SALE));
    }
    
    /* level 1 area */
    
    protected void setHeaderArea() {
        setLeftHeaderArea();
        setRightHeaderArea();
        
        area_header = new BorderPane();
                
        area_header.setPrefWidth(550);
        area_header.setLeft(area_leftHeader);
        area_header.setRight(area_rightHeader);
    }
    
    protected void setContentArea() {
        setUpperMidEArea();
        tbl_item = new TableView<>();
        tbl_item.setPrefWidth(Utility.STANDARD_TABLE_WIDTH);
        tbl_item.setPrefHeight(Utility.STANDARD_TABLE_HEIGHT);
        
        area_content = new VBox(area_upperContent, tbl_item);
        area_content.setSpacing(Utility.SHORT_GAP);
    }
       
    protected void setFooterArea() {
        setLeftFooterArea();
        setRightFooterArea();
        
        area_footer = new BorderPane();
        
        area_footer.setLeft(area_leftFooter);
        area_footer.setRight(area_rightFooter);
    }
    
    protected void setBottomArea() {
        area_bottom = new StandardBigFormBottomArea();
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_SALE));
    }
    
    /* level 2 area */
    
    // setHeaderArea()
    protected void setLeftHeaderArea() {
        lbl_returNumber = new Label("Retur Number; ");
        lbl_doNumber = new Label("DO Number: ");
        lbl_area = new Label("Area: ");
        txt_returNumber = new TextField();
        txt_doNumber = new TextField();
        cmb_area = new ComboBox(); 
        btn_pickDO = new Button(Utility.STANDARD_EXPAND_ICON);
        btn_viewDO = new Button("View DO");
        area_leftHeader = new GridPane();
        
        txt_returNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_doNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_area.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        
        area_leftHeader = new GridPane();
        area_leftHeader.setHgap(Utility.SHORT_GAP);
        area_leftHeader.setVgap(Utility.SHORT_GAP);
        area_leftHeader.addColumn(0, lbl_returNumber);
        area_leftHeader.addColumn(1, txt_returNumber);
        area_leftHeader.addColumn(0, lbl_doNumber);
        area_leftHeader.addColumn(1, txt_doNumber);
        area_leftHeader.addColumn(0, lbl_area);
        area_leftHeader.addColumn(1, cmb_area);
        area_leftHeader.add(btn_pickDO, 2, 1);
        area_leftHeader.add(btn_viewDO, 3, 1);
    }
    
    protected void setRightHeaderArea() {
        lbl_issueDate = new Label("Issue date: ");
        lbl_dueDate = new Label("Due date: ");
        dtp_issueDate = new DatePicker(LocalDate.now());
        dtp_dueDate = new DatePicker(LocalDate.now());
    
        dtp_issueDate.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        dtp_dueDate.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        
        area_rightHeader = new GridPane();
        
        area_rightHeader.setHgap(Utility.SHORT_GAP);
        area_rightHeader.setVgap(Utility.SHORT_GAP);
        area_rightHeader.add(lbl_issueDate, 0, 0);
        area_rightHeader.add(dtp_issueDate, 1, 0);
        area_rightHeader.add(lbl_dueDate, 0, 1);
        area_rightHeader.add(dtp_dueDate, 1, 1);
    }
    
    // setContentArea()
    protected void setUpperMidEArea() {
        lbl_itemCode = new Label("Code: ");
        lbl_itemName = new Label("Name: ");
        lbl_itemQty = new Label("Qty: ");
        lbl_itemUnit = new Label("Unit: ");
        txt_itemCode = new TextField();
        txt_itemName = new TextField();
        ntxt_itemQty = new NumberTextField(); 
        cmb_itemUnit = new ComboBox();
        btn_add = new Button("Add");
        btn_item = new Button(Utility.STANDARD_EXPAND_ICON);
        area_upperContent = new GridPane();
        
        txt_itemCode.setPrefWidth(Utility.SHORT_TEXT_WIDTH);
        txt_itemCode.setEditable(false);
        txt_itemName.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_itemName.setEditable(false);
        ntxt_itemQty.setPrefWidth(Utility.SHORT_TEXT_WIDTH);
        cmb_itemUnit.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_upperContent.setHgap(Utility.SHORT_GAP);
        area_upperContent.setVgap(Utility.SHORT_GAP);
        
        area_upperContent.add(lbl_itemCode, 0, 0);
        area_upperContent.add(txt_itemCode, 0, 1);
        area_upperContent.add(btn_item, 1, 1);
        area_upperContent.add(lbl_itemName, 2, 0);
        area_upperContent.add(txt_itemName, 2, 1);
        area_upperContent.add(lbl_itemQty, 3, 0);
        area_upperContent.add(ntxt_itemQty, 3, 1);
        area_upperContent.add(lbl_itemUnit, 4, 0);
        area_upperContent.add(cmb_itemUnit, 4, 1);
        area_upperContent.add(btn_add, 8, 1); 
        area_upperContent.setBackground(Transaction.getBackground(Transaction.TYPE_SALE));
    }
    
    // setFooterArea()
    protected void setLeftFooterArea()  {
        lbl_note = new Label("Notes: ");
        txa_note = new TextArea();
        area_leftFooter = new GridPane();
        
        txa_note.setPrefWidth(Utility.STANDARD_TXA_WIDTH);
        txa_note.setPrefHeight(Utility.STANDARD_TXA_HEIGHT);
        area_leftFooter.setHgap(Utility.SHORT_GAP);
        area_leftFooter.setVgap(Utility.SHORT_GAP);
        GridPane.setHgrow(txa_note, Priority.ALWAYS);
        GridPane.setVgrow(txa_note, Priority.ALWAYS);        
        area_leftFooter.add(lbl_note, 0, 0);
        area_leftFooter.add(txa_note, 0, 1);
    }
    
    protected void setRightFooterArea()  {
        
        lbl_total = new Label("Total: ");    
        ntxt_total = new NumberTextField();    
        area_rightFooter = new GridPane();
        
        ntxt_total.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        ntxt_total.setEditable(false);
       
        area_rightFooter.setHgap(Utility.SHORT_GAP);
        area_rightFooter.setVgap(Utility.SHORT_GAP);
        
        area_rightFooter.add(lbl_total, 1, 0);
        area_rightFooter.add(ntxt_total, 2, 0);
    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
    
    private void configureItemTableByRow() {
        TableColumn idCol = new TableColumn("Id");
        idCol.setPrefWidth(0);
        idCol.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        
        TableColumn codeCol = new TableColumn("Code");
        codeCol.setMinWidth(100);
        codeCol.setCellValueFactory(
                new PropertyValueFactory<>("code"));
        
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        
        TableColumn originalQtyCol = new TableColumn("Original Qty");
        originalQtyCol.setMinWidth(100);
        originalQtyCol.setCellValueFactory(new PropertyValueFactory<>("originalQuantity"));
        originalQtyCol.setId("originalQtyCol");
        
        
        TableColumn<MQItemFileRow, Number> qtyCol = new TableColumn("Quantity");
        qtyCol.setMinWidth(100);
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setCellFactory(TextFieldTableCell.<MQItemFileRow, Number>forTableColumn(new NumberStringConverter()));
        qtyCol.setOnEditCommit((TableColumn.CellEditEvent<MQItemFileRow, Number> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setQuantity(t.getNewValue().doubleValue());
            }
        );
        
        TableColumn<MQItemFileRow, Number> priceCol = new TableColumn("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        TableColumn<MQItemFileRow, Unit> unitCol = new TableColumn("Unit");
        unitCol.setMinWidth(100);
        unitCol.setCellValueFactory(
                new PropertyValueFactory<>("unit"));
        
        TableColumn<MQItemFileRow, Number> totalCol = new TableColumn("Total");
        totalCol.setMinWidth(100);
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        
        tbl_item.getColumns().addAll(idCol, codeCol, nameCol, originalQtyCol, 
                qtyCol, priceCol, unitCol, totalCol);
        tbl_item.getItems().addListener(
                (javafx.beans.Observable o) -> calculateTotal()
         );
        tbl_item.setEditable(true);
    }
    
    protected void loadAreaData() {
        cmb_area.setItems(Area.getAllAreas());
    }
    
    protected void calculateTotal() {
        double total = 0 ;
        
        if (tbl_item.getItems() != null)
            for (ItemFileRow row : tbl_item.getItems()) {
                total += row.getTotal();
            }
        
        ntxt_total.setValue(total);
    }
    
    protected void setMainButtonsActionsPerformed() {
        area_bottom.setButtonSaveActionPerformed(e -> {
                        if(!validateSave()) {
                            area_bottom.isCanceled(true);
                            return ;
                        }
                        if (v_mode == FormConstant.MODE_NEW) {
                            buildSalesRetur();
                            Environment.getEntityManager().getTransaction().begin();
                            SalesRetur.saveSalesReturByRow(v_salesRetur, tbl_item.getItems());
                            Environment.getEntityManager().getTransaction().commit();
                            disableEditing(true);
                        }
                        else {
                            buildSalesRetur();
                            Environment.getEntityManager().getTransaction().begin();
                            SalesRetur.editSalesReturByRow(v_salesRetur, tbl_item.getItems());
                            Environment.getEntityManager().getTransaction().commit();
                            disableEditing(true);
                        }
                }
        );
        
        area_bottom.setButtonNewActionPerformed(
                e -> { 
                    clearInputs();
                    disableEditing(false);
                }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());
    }
    
    protected void setButtonsActionsPerformed() {
        btn_item.setOnAction (
                e -> {
                    FormPickItem form = new FormPickItem();
                    form.showAndWait();
                    if(form.getReturnStatus() != FormConstant.RETURN_OK)
                        return;
                  
                    ObservableList<MQItemFileRow> insertedFiles = FXCollections.observableArrayList();
                    List<Item> items = form.getSelectedItems();
                    if (items.size() == 1) {
                        v_item = (Item) items.get(0);
                        txt_itemCode.setText(v_item.getCode());
                        txt_itemName.setText(v_item.getName());
                        cmb_itemUnit.setItems(FXCollections.observableArrayList(Unit.getUnitsByItemId(v_item)));
                    }
                    else if (items.size() > 1) {
                        for (Item _item : items) {
                            MQItemFileRow row = new MQItemFileRow();
                            row.setName(_item.getName());
                            row.setCode(_item.getCode());
                            insertedFiles.add(row);
                        }
                        tbl_item.getItems().addAll(insertedFiles);
                    }
               }
        );
        
        btn_add.setOnAction(
                e -> {
                    if (!validateAdd())
                        return;
                    
                    addByRow();
                    clearUpperMidInputs();
                }
        );
        
        btn_pickDO.setOnAction(e -> {
                    FormPickDeliveryOrder form = new FormPickDeliveryOrder();
                    SalesOrder so = null;
                    form.showAndWait();
                    
                    if (form.getReturnStatus() != FormConstant.RETURN_OK)
                            return;
                        
                        v_deliveryOrder = form.getSelectedItems().get(0);
                        so = v_deliveryOrder.getStockreservation().getSalesOrder();
                        txt_doNumber.setText(v_deliveryOrder.getTransactionNumber());
                        ObservableList<MQItemFileRow> rows = FXCollections.observableArrayList();
                        for (ItemFileRow r : DOFile.convertFilesToRows(v_deliveryOrder.getFiles())) {
                            MQItemFileRow row = new MQItemFileRow(r);
                            row.setOriginalQuantity(r.getQuantity());
                            row.setQuantity(0);
                            for (SOFile soFile : so.getFiles()) {
                                if(soFile.getPackedItem().getItem().getId().equals(r.getId()) &&
                                   soFile.getPackedItem().getUnit().equals(r.getUnit())) {
                                        row.setPrice(soFile.getPrice());
                                        break;
                                }
                            }
                            
                            row.quantityProperty().addListener(
                               (ObservableValue<? extends Number> 
                                       _value, Number _old, Number _new) -> {
                                    if (!validateQty(row.getOriginalQuantity(), _new.doubleValue()))
                                        row.setQuantity(_old.doubleValue());
                                }                            
                            );
                            row.totalProperty().addListener(o -> calculateTotal());
                            rows.add(row);
                        }
                        tbl_item.setItems(rows);
                }
        );
        
        btn_viewDO.setOnAction(e -> {
                    if(v_deliveryOrder == null)
                        return;
                    
                    FormDeliveryOrder form = new FormDeliveryOrder(v_deliveryOrder, FormConstant.MODE_VIEW);
                    form.setTitle("View Delivery Order");
                    form.showAndWait();
                }
        );
    }
    
    protected void setTableActionPerformed() {
        tbl_item.setOnKeyPressed(
                e -> {
                    if(e.getCode()== KeyCode.DELETE) {
                        int sel = tbl_item.getSelectionModel().getSelectedIndex() ;
                        tbl_item.getItems().remove(sel);
                    }
                }
        );
    }
    
    
    protected void addByRow() {
        MQItemFileRow row = new MQItemFileRow() ;
        double qty = ntxt_itemQty.getValue();
        row.setId(v_item.getId());
        row.setCode(txt_itemCode.getText());
        row.setName(txt_itemName.getText());
        row.setQuantity(qty);
        row.setUnit((Unit)cmb_itemUnit.getValue());
        row.totalProperty().addListener(
                o -> calculateTotal()
        );
        tbl_item.getItems().add(row);
    }

    protected void buildSalesRetur() {
        if(v_salesRetur == null)
            v_salesRetur = new SalesRetur();
        try
        {
            Calendar due = Calendar.getInstance();
            Calendar issue = Calendar.getInstance();
            
            LocalDate dueDate = dtp_dueDate.getValue();
            LocalDate issueDate = dtp_issueDate.getValue();

            if(txt_returNumber.getText() != null && txt_returNumber.getText().length() >= 0) {
                String sequenceKey = SequenceGenerator.convertLocalDateToSequenceString(issueDate);
                int sequenceValue = SequenceGenerator.getNextSequence(sequenceKey);
                txt_returNumber.setText("ST-" + sequenceKey + "-" + sequenceValue );
            }

            due.setTime(java.sql.Date.valueOf(dueDate));
            issue.setTime(java.sql.Date.valueOf(issueDate));
            
            v_salesRetur.setTransactionNumber(txt_returNumber.getText());
            v_salesRetur.setDeliveryOrder(v_deliveryOrder);
            v_salesRetur.setArea((Area) cmb_area.getValue());
            v_salesRetur.setDueDate(due);
            v_salesRetur.setIssueDate(issue);
            v_salesRetur.setNote(txa_note.getText());
        }
        catch(Exception _e ) {
            _e.printStackTrace();
        }
    }
    
    protected boolean validateSave() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving sales retur");
        
        if(v_deliveryOrder == null) {
            alert.setContentText("There is no delivery order for this sales retur");
            alert.showAndWait();
            return false ;
        }
        
        if(cmb_area.getValue() == null) {
            alert.setContentText("There is no return area for this sales retur");
            alert.showAndWait();
            return false ;
        }
        
        if (tbl_item.getItems() == null || tbl_item.getItems().size() <= 0) {
            alert.setContentText("There is no item to be saved");
            alert.showAndWait();
            return false ;
        }
        
        for(int i = 0 ; i < tbl_item.getItems().size(); i++) {
            ItemFileRow row = tbl_item.getItems().get(i);
            if(row.getQuantity() > Utility.DOUBLE_TOLERANCE)
                break;
            
            if(i == tbl_item.getItems().size() - 1) {
                alert.setContentText("There is no item to be saved");
                alert.showAndWait();
                return false;
            }
        }
        
        return true;
    }
    
    protected boolean validateAdd() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in adding item");
        
        if (v_item == null) {
            alert.setContentText("There is no item to be added");
            alert.showAndWait();
            return false ;
        }
        
        if (ntxt_itemQty.getValue() == null || ntxt_itemQty.getValue() <= 0) {
            alert.setContentText("The quantity of the item is not valid");
            alert.showAndWait();
            return false ;
        }
        
        
        if (cmb_itemUnit.getValue() == null) {
            alert.setContentText("Please chhose the unit in the item");
            alert.showAndWait();
            return false ;
        }
        
        return true;
    }
    
    protected boolean validateQty(double _oQty, double _qty) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in setting qty");
        
       
        if (_qty > _oQty ) {
            alert.setContentText("The qty set is more than delivered quantity");
            alert.showAndWait();
            return false ;
        }
        
        return true;
    }
    
    protected void disableEditing(boolean _disable) {
        txt_returNumber.setDisable(_disable);
        txt_returNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_doNumber.setDisable(_disable);
        txt_doNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txa_note.setDisable(_disable);
        txa_note.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_area.setDisable(_disable);
        cmb_area.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        dtp_issueDate.setDisable(_disable);
        dtp_issueDate.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        dtp_dueDate.setDisable(_disable);
        dtp_dueDate.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");

        txt_itemCode.setDisable(_disable);
        txt_itemCode.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_itemName.setDisable(_disable);
        txt_itemName.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        ntxt_itemQty.setDisable(_disable);
        ntxt_itemQty.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_itemUnit.setDisable(_disable); 
        cmb_itemUnit.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        
        btn_pickDO.setVisible(!_disable);
        
        tbl_item.setEditable(!_disable);
        area_upperContent.setVisible(!_disable);
        area_bottom.setViewMode(_disable);
        
        if(tbl_item.getColumns() != null) {
            for (TableColumn col : tbl_item.getColumns()) {
                if(col.getId() == null)
                    continue;                
                
                if(col.getId().equals("originalQtyCol"))
                    col.setVisible(false);
            }
        }
    }
    
    
    protected void clearVariables() {
        v_salesRetur = null;
        v_deliveryOrder = null;
        v_item = null ;
    }
    
    protected void clearInputs() {
        txt_returNumber.clear();
        txt_doNumber.clear();
        dtp_dueDate.setValue(LocalDate.now());
        dtp_issueDate.setValue(LocalDate.now());
        cmb_area.setValue(null);
        clearUpperMidInputs();
        clearVariables();
    }
    
    protected void clearUpperMidInputs() {
        txt_itemCode.clear();
        txt_itemName.clear();
        ntxt_itemQty.clear();
        cmb_itemUnit.setValue(null);
        v_item = null ;
    }
}
    
   

