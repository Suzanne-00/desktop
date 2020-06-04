/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.stock_reservation.form;

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
import suzane00.sale.sales_order.SOFile;
import suzane00.sale.sales_order.SalesOrder;
import suzane00.sale.sales_order.form.FormPickSalesOrder;
import suzane00.sale.sales_order.form.FormSalesOrder;
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
public class FormStockReservation extends Stage {
    
    protected static final int COL_ID = 0 ;
    protected static final int COL_CODE = 1 ;
    protected static final int COL_NAME = 2 ;
    protected static final int COL_ORIGIN_QTY = 3 ;
    protected static final int COL_DELIVERED_QTY = 4 ;
    protected static final int COL_EXISTING_QTY = 5 ;
    protected static final int COL_QTY = 6 ;
    protected static final int COL_UNIT = 7 ;
    
    protected StockReservation v_stockReservation ;
    protected SalesOrder v_salesOrder ;
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
    protected Label lbl_reservationNumber;
    protected Label lbl_soNumber;
    protected Label lbl_area;
    protected TextField txt_reservationNumber;
    protected TextField txt_soNumber;
    protected ComboBox<Area> cmb_area;
    protected Button btn_pickSO;
    protected Button btn_viewSO;
    
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
//    protected Label lbl_total;  
//    protected NumberTextField ntxt_amount;
//    protected NumberTextField ntxt_discount;
//    protected NumberTextField ntxt_miscCharge;
//    protected NumberTextField ntxt_total;  
//    protected Button btn_disc;
//    protected Button btn_miscCharge;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    public Node getRoot() {
        return area_main ;
    }
    
    public FormStockReservation() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        setScene(scene);
    }
    
    public FormStockReservation(StockReservation _sr, int _mode) {
        this();
        setVariables(_sr, _mode);
        disiplayData();
        if (v_mode == FormConstant.MODE_VIEW) {
            disableEditing(true);
            //setViewActionPerformed();
        }
    }
    
    protected void initComponents() {
        v_stockReservation = null;
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
        setOthersActionsPerformed();
    }
    
    protected void setVariables(StockReservation _sr, int _mode) {
        v_stockReservation = _sr;
        v_salesOrder = _sr.getSalesOrder();
        v_mode = _mode;
    }
    
    protected void disiplayData() {
        Date issueDate = v_stockReservation.getIssueDate().getTime();
        LocalDate issueLocalDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date dueDate = v_stockReservation.getDueDate().getTime();
        LocalDate dueLocalDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        txt_reservationNumber.setText(v_stockReservation.getTransactionNumber());
        txt_soNumber.setText(v_stockReservation.getSalesOrder().getTransactionNumber());
        txa_note.setText(v_stockReservation.getNote());
        cmb_area.setValue(v_stockReservation.getArea());
        
        dtp_issueDate.setValue(issueLocalDate);
        dtp_dueDate.setValue(dueLocalDate);
        List<MQItemFileRow> srRows = SRFile.convertFilesToRows(v_stockReservation.getFiles());
        List<ItemFileRow> soRows = SOFile.convertSOFilesToRows(v_stockReservation.getSalesOrder()
                                         .getFiles());
        for (MQItemFileRow srRow : srRows) {
            for (ItemFileRow soRow : soRows) {
                if(srRow.getId() == soRow.getId() && srRow.getUnit().equals(soRow.getUnit())) {
                    srRow.setOriginalQuantity(soRow.getQuantity());
//                    orRow.setExistingQuantity(SRFile.getExistingQuantityFor(v_stockReservation.getSalesOrder(),
//                            orRow));
                }
            }
        }
        
        tbl_item.getItems().addAll(srRows);
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
        //setRightFooterArea();
        
        area_footer = new BorderPane();
        
        area_footer.setLeft(area_leftFooter);
        //area_footer.setRight(area_rightFooter);
    }
    
    protected void setBottomArea() {
        area_bottom = new StandardBigFormBottomArea();
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_SALE));
    }
    
    /* level 2 area */
    
    // setHeaderArea()
    protected void setLeftHeaderArea() {
        lbl_reservationNumber = new Label("Reservation Number; ");
        lbl_soNumber = new Label("SO Number: ");
        lbl_area = new Label("Area: ");
        txt_reservationNumber = new TextField();
        txt_soNumber = new TextField();
        cmb_area = new ComboBox(); 
        btn_pickSO = new Button(Utility.STANDARD_EXPAND_ICON);
        btn_viewSO = new Button("View SO");
        area_leftHeader = new GridPane();
        
        txt_reservationNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_soNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_area.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        
        area_leftHeader = new GridPane();
        area_leftHeader.setHgap(Utility.SHORT_GAP);
        area_leftHeader.setVgap(Utility.SHORT_GAP);
        area_leftHeader.addColumn(0, lbl_reservationNumber);
        area_leftHeader.addColumn(1, txt_reservationNumber);
        area_leftHeader.addColumn(0, lbl_soNumber);
        area_leftHeader.addColumn(1, txt_soNumber);
        area_leftHeader.addColumn(0, lbl_area);
        area_leftHeader.addColumn(1, cmb_area);
        area_leftHeader.add(btn_pickSO, 2, 1);
        area_leftHeader.add(btn_viewSO, 3, 1);
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
        
        TableColumn existingQtyCol = new TableColumn("Existing Qty");
        existingQtyCol.setMinWidth(100);
        existingQtyCol.setCellValueFactory(new PropertyValueFactory<>("existingQuantity"));
        existingQtyCol.setId("existingQtyCol");
        
        TableColumn stockQtyCol = new TableColumn("Stock Qty");
        stockQtyCol.setMinWidth(100);
        stockQtyCol.setCellValueFactory(new PropertyValueFactory<>("otherQuantity"));
        stockQtyCol.setId("stockQtyCol");
        
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
        
        TableColumn<MQItemFileRow, Unit> unitCol = new TableColumn("Unit");
        unitCol.setMinWidth(100);
        unitCol.setCellValueFactory(
                new PropertyValueFactory<>("unit"));
        
        
        tbl_item.getColumns().addAll(idCol, codeCol, nameCol, originalQtyCol, existingQtyCol, 
                stockQtyCol, qtyCol, unitCol);
//        tbl_item.getItems().addListener(
//                (javafx.beans.Observable o) -> calculateTotal()
//         );
        tbl_item.setEditable(true);
    }
    
    protected void loadAreaData() {
        cmb_area.setItems(Area.getAllAreas());
    }
    
    protected void setMainButtonsActionsPerformed() {
        area_bottom.setButtonSaveActionPerformed(e -> {
                        if(!validateSave()) {
                            area_bottom.isCanceled(true);
                            return ;
                        }
                        if (v_mode == FormConstant.MODE_NEW) {
                            buildStockReservation();
                            Environment.getEntityManager().getTransaction().begin();
                            boolean result = StockReservation.saveStockReservationByRow(
                                                v_stockReservation, tbl_item.getItems());
                            if(result) {
                                Environment.getEntityManager().getTransaction().commit();
                                disableEditing(true);
                            }
                            else {
                                Environment.getEntityManager().getTransaction().rollback();
                                Alert alert = new Alert(AlertType.ERROR);
                                alert.setTitle("Error");
                                alert.setHeaderText("Error in saving stock reservation");
                                alert.setContentText("Not enough items for to be reserved");
                                alert.showAndWait();
                            }
                        }
                        else {
                            buildStockReservation();
                            Environment.getEntityManager().getTransaction().begin();
                            StockReservation.editStockReservationByRow(
                                    v_stockReservation, tbl_item.getItems());
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
        
        btn_pickSO.setOnAction(
                e -> {
                    FormPickSalesOrder form = new FormPickSalesOrder();
                    form.showAndWait();
                    
                    if (form.getReturnStatus() != FormConstant.RETURN_OK)
                            return;
                        
                        v_salesOrder = form.getSelectedItems().get(0);
                        txt_soNumber.setText(v_salesOrder.getTransactionNumber());
                        ObservableList<MQItemFileRow> rows = FXCollections.observableArrayList();
                        for (ItemFileRow r : SOFile.convertSOFilesToRows(v_salesOrder.getFiles())) {
                            MQItemFileRow row = new MQItemFileRow(r);
                            row.setOriginalQuantity(r.getQuantity());
                            row.setExistingQuantity(SRFile.getExistingQuantityFor(v_salesOrder, r));
                            if(cmb_area.getValue() != null)
                                row.setOtherQuantity(Stock.getStockQuantity(PackedItem.getPackedItemsById( 
                                    new PackedItemPK(r.getId(), r.getUnit().getId())), (Area)cmb_area.getValue()));
                            else
                                row.setOtherQuantity(0);
                            if(row.getOriginalQuantity() >= Utility.DOUBLE_TOLERANCE &&
                              (row.getExistingQuantity() < row.getOriginalQuantity())) {
                                if(row.getOtherQuantity() > row.getOriginalQuantity() - row.getExistingQuantity())
                                    row.setQuantity(row.getOriginalQuantity() - row.getExistingQuantity());
                                else
                                    row.setQuantity(row.getOtherQuantity());
                            }
                            else
                                row.setQuantity(0);
                            
                            row.quantityProperty().addListener(
                               (ObservableValue<? extends Number> 
                                       _value, Number _old, Number _new) -> {
                                    if (!validateQty(row.getOriginalQuantity(), row.getExistingQuantity(),
                                            row.getOtherQuantity(), _new.doubleValue()))
                                        row.setQuantity(_old.doubleValue());
                                }                            
                            );
                            rows.add(row);
                        }
                        tbl_item.setItems(rows);
                }
        );
        
        btn_viewSO.setOnAction(e -> {
                    if(v_salesOrder == null)
                        return;
                    
                    FormSalesOrder form = new FormSalesOrder(v_salesOrder, FormConstant.MODE_VIEW);
                    form.setTitle("View Sales Order");
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
    
    protected void setOthersActionsPerformed() {
        
        cmb_area.valueProperty().addListener(
            (ObservableValue<? extends Area> 
                    _value, Area _old, Area _new) -> {
                if(tbl_item.getItems() != null) {
                    for (MQItemFileRow row : tbl_item.getItems()) {
                        row.setOtherQuantity(Stock.getStockQuantity(PackedItem.getPackedItemsById( 
                                    new PackedItemPK(row.getId(), row.getUnit().getId())), (Area)cmb_area.getValue()));
                    }
                }
                            
             }                            
        );
        
        lbl_area.setOnMouseClicked(e -> {
                    if(e.getClickCount() <= 1)
                        return ;
                    
                    FormArea form = new FormArea() ;
                    form.showAndWait();
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
        tbl_item.getItems().add(row);
    }

    protected void buildStockReservation() {
        if(v_stockReservation == null)
            v_stockReservation = new StockReservation();
        try
        {
            Calendar due = Calendar.getInstance();
            Calendar issue = Calendar.getInstance();
            
            LocalDate dueDate = dtp_dueDate.getValue();
            LocalDate issueDate = dtp_issueDate.getValue();

            if(txt_reservationNumber.getText() != null && txt_reservationNumber.getText().length() >= 0) {
                String sequenceKey = SequenceGenerator.convertLocalDateToSequenceString(issueDate);
                int sequenceValue = SequenceGenerator.getNextSequence(sequenceKey);
                txt_reservationNumber.setText("SR-" + sequenceKey + "-" + sequenceValue );
            }

            due.setTime(java.sql.Date.valueOf(dueDate));
            issue.setTime(java.sql.Date.valueOf(issueDate));
            
            v_stockReservation.setTransactionNumber(txt_reservationNumber.getText());
            v_stockReservation.setSalesOrder(v_salesOrder);
            v_stockReservation.setArea((Area) cmb_area.getValue());
            v_stockReservation.setDueDate(due);
            v_stockReservation.setIssueDate(issue);
            v_stockReservation.setNote(txa_note.getText());
        }
        catch(Exception _e ) {
            _e.printStackTrace();
        }
    }
    
    protected boolean validateSave() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving stock reservation");
        
        if(v_salesOrder == null) {
            alert.setContentText("There is no sales order for this invoice");
            alert.showAndWait();
            return false ;
        }
        
        if(cmb_area.getValue() == null) {
            alert.setContentText("Please choose the area for the stock reservation");
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
    
    protected boolean validateQty(double _oQty, double _eQty, double _rQty, double _qty) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in setting qty");
        
        if (_qty > _rQty) {
            alert.setContentText("There is no enough stock for this item");
            alert.showAndWait();
            return false ;
        }
        
        if (_qty > (_oQty - _eQty)) {
            alert.setContentText("The qty set is more than ordered  and existingquantity");
            alert.showAndWait();
            return false ;
        }
        
        return true;
    }
    
    protected void disableEditing(boolean _disable) {
        txt_reservationNumber.setDisable(_disable);
        txt_reservationNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_soNumber.setDisable(_disable);
        txt_soNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
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
        
        btn_pickSO.setVisible(!_disable);
        
        tbl_item.setEditable(!_disable);
        area_upperContent.setVisible(!_disable);
        area_bottom.setViewMode(_disable);
        
        if(tbl_item.getColumns() != null) {
            for (TableColumn col : tbl_item.getColumns()) {
                if(col.getId() == null)
                    continue;                
                
                if(col.getId().equals("originalQtyCol") ||
                   col.getId().equals("existingQtyCol") ||
                   col.getId().equalsIgnoreCase("stockQtyCol"))
                    col.setVisible(false);
            }
        }
    }
    
    
    protected void clearVariables() {
        v_salesOrder = null;
        v_item = null ;
    }
    
    protected void clearInputs() {
        txt_reservationNumber.clear();
        txt_soNumber.clear();
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
    
   

