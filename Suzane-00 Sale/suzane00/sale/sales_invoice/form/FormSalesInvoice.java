/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_invoice.form;

import suzane00.sale.form.FormPickItemPrice;
import com.sun.jmx.remote.util.EnvHelp;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
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
import suzane00.sale.sales_order.SalesOrder;
import suzane00.inventory.SellPriceType;
import suzane00.transaction.custom_ui.IFRUnitComboBox;
import suzane00.inventory.custom_ui.NumberTextField;
import suzane00.sale.sales_invoice.SIDiscount;
import suzane00.sale.sales_invoice.SIFile;
import suzane00.sale.sales_invoice.SIMiscCharge;
import suzane00.sale.sales_invoice.SalesInvoice;
import suzane00.transaction.model.ItemFileRow;
import suzane00.sale.sales_order.SODiscount;
import suzane00.sale.sales_order.SOFile;
import suzane00.sale.sales_order.SOMiscCharge;
import suzane00.sale.sales_order.form.FormPickSalesOrder;
import suzane00.sale.sales_order.form.FormSalesOrder;
//import suzane00.sales.model.SOFileRow;
import suzane00.source.Address;
import suzane00.source.Customer;
import suzane00.source.Supplier;
import suzane00.source.form.FormAddAddress;
import suzane00.source.form.FormSupplier;
import suzane00.transaction.Transaction;
import suzane00.transaction.custom_ui.MQIFRUnitComboBox;
import suzane00.transaction.form.FormAddCharges;
import suzane00.transaction.model.MQItemFileRow;

/**
 *
 * @author Usere
 */
public class FormSalesInvoice extends Stage {
    
    protected static final int COL_ID = 0 ;
    protected static final int COL_CODE = 1 ;
    protected static final int COL_NAME = 2 ;
    protected static final int COL_ORIGIN_QTY = 3 ;
    protected static final int COL_EXISTING_QTY = 4 ;
    protected static final int COL_QTY = 5 ;
    protected static final int COL_UNIT = 6 ;
    protected static final int COL_DISC = 7 ;
    protected static final int COL_DISCS = 8 ;
    protected static final int COL_PRICE = 9 ;
    protected static final int COL_TOTAL = 10 ;
    
    protected SalesInvoice v_salesInvoice ;
    protected SalesOrder v_salesOrder ;
    protected Item v_item ;
    protected ObservableList<Cost> v_fileDiscounts;
    protected ObservableList<Cost> v_discounts;
    protected ObservableList<Cost> v_miscCharges;
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
    protected Label lbl_siNumber;
    protected Label lbl_soNumber;
    protected Label lbl_paymentType;
    protected TextField txt_siNumber;
    protected TextField txt_soNumber;
    protected ComboBox cmb_paymentType;
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
    protected Label lbl_itemDiscount;
    protected Label lbl_itemPrice;
    protected Label lbl_itemUnit;
    protected TextField txt_itemCode;
    protected TextField txt_itemName;
    protected NumberTextField ntxt_itemQty;
    protected NumberTextField ntxt_itemDiscount;
    protected NumberTextField ntxt_itemPrice;
    protected ComboBox cmb_itemUnit;
    protected Button btn_add;
    protected Button btn_item ;
    protected Button btn_itemDisc;
    
    // area_footer -> area_leftFooter
    protected Label lbl_note;
    protected TextArea txa_note;
    
    // area_footer -> area_rightFooter
    protected Label lbl_amount;
    protected Label lbl_discount;
    protected Label lbl_miscCharge;
    protected Label lbl_total;  
    protected NumberTextField ntxt_amount;
    protected NumberTextField ntxt_discount;
    protected NumberTextField ntxt_miscCharge;
    protected NumberTextField ntxt_total;  
    protected Button btn_disc;
    protected Button btn_miscCharge;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    public Node getRoot() {
        return area_main ;
    }
    
    public FormSalesInvoice() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        setScene(scene);
    }
    
    public FormSalesInvoice(SalesInvoice _si, int _mode) {
        this();
        setVariables(_si, _mode);
        disiplayData();
        if (v_mode == FormConstant.MODE_VIEW) {
            disableEditing(true);
            setViewActionPerformed();
        }
    }
    
    protected void initComponents() {
        v_salesInvoice = null;
        v_item = null;
        v_fileDiscounts = null;
        v_discounts = null;
        v_miscCharges = null ;
        v_mode = FormConstant.MODE_NEW;;
    }
    
    protected void setAppearance() {
        setMainArea() ;
    }
    
    protected void setData() {
        configureItemTableByRow();
        loadPaymentTypeData();
    }
    
    protected void setActionPerformed() {
        setMainButtonsActionsPerformed();
        setButtonsActionsPerformed();
        setTableActionPerformed();
        setOthersActionsPerformed();
    }
    
    protected void setVariables(SalesInvoice _si, int _mode) {
        v_salesInvoice = _si;
        v_salesOrder = _si.getSalesOrder();
        v_mode = _mode;
    }
    
    protected void disiplayData() {
        Date issueDate = v_salesInvoice.getIssueDate().getTime();
        LocalDate issueLocalDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date dueDate = v_salesInvoice.getDueDate().getTime();
        LocalDate dueLocalDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        txt_siNumber.setText(v_salesInvoice.getTransactionNumber());
        txt_soNumber.setText(v_salesInvoice.getSalesOrder().getTransactionNumber());
        txa_note.setText(v_salesInvoice.getNote());
        cmb_paymentType.setValue(v_salesInvoice.getPaymentType());
        
        dtp_issueDate.setValue(issueLocalDate);
        dtp_dueDate.setValue(dueLocalDate);
        List<MQItemFileRow> siRows = SIFile.convertSIFilesToRows(v_salesInvoice.getFiles());
        List<ItemFileRow> soRows = SOFile.convertSOFilesToRows(v_salesInvoice.getSalesOrder()
                                         .getFiles());
        for (MQItemFileRow siRow : siRows) {
            for (ItemFileRow soRow : soRows) {
                if(siRow.getId() == soRow.getId() && siRow.getUnit().equals(soRow.getUnit())) {
                    siRow.setOriginalQuantity(soRow.getQuantity());
                    siRow.setExistingQuantity(SIFile.getExistingQuantityFor(v_salesInvoice.getSalesOrder(), siRow));
                }
            }
        }
        
        tbl_item.getItems().addAll(siRows);
        v_discounts = SIDiscount.convertSIDiscountsToCosts(v_salesInvoice.getDiscounts());
        v_miscCharges = SIMiscCharge.convertSIMiscChargesToCosts(v_salesInvoice.getMiscCharges());
        calculateTotal();
    }
    
    protected void setViewActionPerformed() {
        tbl_item.setOnMouseClicked(e -> {
                    if(e.getClickCount() >= 2) {
                        TablePosition pos = tbl_item.getSelectionModel().getSelectedCells().get(0);
                        ItemFileRow row = tbl_item.getSelectionModel().getSelectedItem();
                        if(pos.getColumn() != COL_DISC || row.getDiscounts() == null)
                            return;
                        FormAddCharges form ;
                        form = new FormAddCharges(row.getDiscounts(), FormConstant.MODE_VIEW);
                        form.setTitle("View Item Discounts");
                        form.showAndWait();
                    }
                }
        );
        
        ntxt_discount.setDisable(false);
        ntxt_discount.setEditable(false);
        ntxt_discount.setOnMouseClicked(e -> {
                        if(e.getClickCount() >= 2 && v_discounts != null) {
                            FormAddCharges form = new FormAddCharges(v_discounts, FormConstant.MODE_VIEW);
                            form.setTitle("View Discounts");
                            form.show();
                        }
                    }
            );
        
        ntxt_miscCharge.setDisable(false);
        ntxt_miscCharge.setEditable(false);
        ntxt_miscCharge.setOnMouseClicked(
                e -> {
                    if(e.getClickCount() >= 2 && v_miscCharges != null) {
                        FormAddCharges form = new FormAddCharges(v_miscCharges, FormConstant.MODE_VIEW);
                        form.setTitle("View Misc Charges");
                        form.show();
                    }
                }
        );
    }
    
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
        lbl_siNumber = new Label("Invoice Number; ");
        lbl_soNumber = new Label("SO Number: ");
        lbl_paymentType = new Label("Payment type: ");
        txt_siNumber = new TextField();
        txt_soNumber = new TextField();
        cmb_paymentType = new ComboBox(); 
        btn_pickSO = new Button(Utility.STANDARD_EXPAND_ICON);
        btn_viewSO = new Button("View SO");
        area_leftHeader = new GridPane();
        
        txt_siNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_soNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_paymentType.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        
        area_leftHeader = new GridPane();
        area_leftHeader.setHgap(Utility.SHORT_GAP);
        area_leftHeader.setVgap(Utility.SHORT_GAP);
        area_leftHeader.addColumn(0, lbl_siNumber);
        area_leftHeader.addColumn(1, txt_siNumber);
        area_leftHeader.addColumn(0, lbl_soNumber);
        area_leftHeader.addColumn(1, txt_soNumber);
        area_leftHeader.addColumn(0, lbl_paymentType);
        area_leftHeader.addColumn(1, cmb_paymentType);
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
        lbl_itemDiscount = new Label("Discount: ");
        lbl_itemPrice = new Label("Price: ");
        lbl_itemUnit = new Label("Unit: ");
        txt_itemCode = new TextField();
        txt_itemName = new TextField();
        ntxt_itemQty = new NumberTextField(); 
        ntxt_itemDiscount = new NumberTextField();
        ntxt_itemPrice = new NumberTextField();
        cmb_itemUnit = new ComboBox();
        btn_add = new Button("Add");
        btn_item = new Button(Utility.STANDARD_EXPAND_ICON);
        btn_itemDisc = new Button(Utility.STANDARD_EXPAND_ICON);
        area_upperContent = new GridPane();
        
        txt_itemCode.setPrefWidth(Utility.SHORT_TEXT_WIDTH);
        txt_itemCode.setEditable(false);
        txt_itemName.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_itemName.setEditable(false);
        ntxt_itemQty.setPrefWidth(Utility.SHORT_TEXT_WIDTH);
        ntxt_itemDiscount.setPrefWidth(Utility.SHORT_TEXT_WIDTH);
        ntxt_itemDiscount.setEditable(false);
        ntxt_itemPrice.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
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
        area_upperContent.add(lbl_itemDiscount, 5, 0);
        area_upperContent.add(ntxt_itemDiscount, 5, 1);
        area_upperContent.add(btn_itemDisc, 6, 1);
        area_upperContent.add(lbl_itemPrice, 7, 0);
        area_upperContent.add(ntxt_itemPrice, 7, 1);
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
        lbl_amount = new Label("Amount: ");
        lbl_discount = new Label("Discount: ");
        lbl_miscCharge = new Label("Misc charge: ");
        lbl_total = new Label("Total: ");    
        ntxt_amount = new NumberTextField();
        ntxt_discount = new NumberTextField();
        ntxt_miscCharge = new NumberTextField();
        ntxt_total = new NumberTextField();    
        btn_disc = new Button(Utility.STANDARD_EXPAND_ICON);
        btn_miscCharge = new Button(Utility.STANDARD_EXPAND_ICON);
        area_rightFooter = new GridPane();
        
        ntxt_amount.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        ntxt_miscCharge.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        ntxt_discount.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        ntxt_total.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        ntxt_amount.setEditable(false);
        ntxt_miscCharge.setEditable(false);
        ntxt_discount.setEditable(false);
        ntxt_total.setEditable(false);
       
        area_rightFooter.setHgap(Utility.SHORT_GAP);
        area_rightFooter.setVgap(Utility.SHORT_GAP);
        
        area_rightFooter.add(lbl_amount, 1, 0);
        area_rightFooter.add(ntxt_amount, 2, 0);
        area_rightFooter.add(btn_miscCharge, 0, 1);
        area_rightFooter.add(lbl_miscCharge, 1, 1);
        area_rightFooter.add(ntxt_miscCharge, 2, 1);
        area_rightFooter.add(btn_disc, 0, 2);
        area_rightFooter.add(lbl_discount, 1, 2);
        area_rightFooter.add(ntxt_discount, 2, 2);
        area_rightFooter.add(lbl_total, 1, 3);
        area_rightFooter.add(ntxt_total, 2, 3);
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
        ObservableList<Unit> units = FXCollections.observableArrayList();
        unitCol.setMinWidth(100);
        unitCol.setCellValueFactory(
                new PropertyValueFactory<>("unit"));
        unitCol.setCellFactory((TableColumn<MQItemFileRow, Unit> p) -> {
                 return new MQIFRUnitComboBox();
            }
        );
        unitCol.setOnEditCommit((TableColumn.CellEditEvent<MQItemFileRow, Unit> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setUnit(t.getNewValue());
            }
        );
         
        TableColumn<MQItemFileRow, Number> priceCol = new TableColumn("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setEditable(true);
        priceCol.setCellFactory(TextFieldTableCell.<MQItemFileRow, Number>forTableColumn(new NumberStringConverter()));
        priceCol.setOnEditCommit((TableColumn.CellEditEvent<MQItemFileRow, Number> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setPrice(t.getNewValue().doubleValue());
            }
        );
        
        
        TableColumn discCol = new TableColumn("Discount");
        discCol.setMinWidth(100);
        discCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        
        TableColumn discsCol = new TableColumn("");
        discsCol.setCellValueFactory(new PropertyValueFactory<>("discounts"));
        discsCol.setPrefWidth(0);
        discsCol.setVisible(false);
        
        TableColumn totalCol = new TableColumn("Total");
        totalCol.setMinWidth(100);
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        
        tbl_item.getColumns().addAll(idCol, codeCol, nameCol, originalQtyCol, existingQtyCol, 
                qtyCol, unitCol, discCol, discsCol, priceCol, totalCol );
        tbl_item.getItems().addListener(
                (javafx.beans.Observable o) -> calculateTotal()
         );
        tbl_item.setEditable(true);
    }
    
    protected void loadPaymentTypeData() {
        ObservableList<String> payments = FXCollections.observableArrayList();
        payments.add(SalesInvoice.TYPE_CASH);
        payments.add(SalesInvoice.TYPE_CREDIT);
        cmb_paymentType.setItems(payments);
    }
    
    protected void setMainButtonsActionsPerformed() {
        area_bottom.setButtonSaveActionPerformed(
                e -> {
                        if(!validateSave())
                            return;
                        if (v_mode == FormConstant.MODE_NEW) {
                            buildSalesInvoice();
                            Environment.getEntityManager().getTransaction().begin();
                            SalesInvoice.saveSalesInvoiceByRow(v_salesInvoice, tbl_item.getItems(), v_discounts, v_miscCharges);
                            Environment.getEntityManager().getTransaction().commit();
                            disableEditing(true);
                        }
                        else {
                            buildSalesInvoice();
                            Environment.getEntityManager().getTransaction().begin();
                            SalesInvoice.editSalesInvoiceByRow(v_salesInvoice, tbl_item.getItems(), v_discounts, v_miscCharges);
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
        btn_disc.setOnAction(
                e -> {
                    FormAddCharges form ;
                    if (v_discounts != null) 
                        form = new FormAddCharges(v_discounts, FormConstant.MODE_EDIT);
                    else
                        form = new FormAddCharges();
                    
                    form.setTitle("Add Discounts");
                    form.showAndWait();
                    if ( form.getReturnStatus() != FormConstant.RETURN_OK)
                        return;

                    v_discounts = form.getCharges();
                    calculateTotal();
                }
        );
        
        btn_miscCharge.setOnAction(
                e -> {
                    FormAddCharges form ;
                    if (v_miscCharges != null) 
                        form = new FormAddCharges(v_miscCharges, FormConstant.MODE_EDIT);
                    else
                        form = new FormAddCharges();

                    form.setTitle("Add Misc Charges");
                    form.showAndWait();
                    if ( form.getReturnStatus() != FormConstant.RETURN_OK)
                        return;

                    v_miscCharges = form.getCharges();
                    calculateTotal();
                }
        );
        
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
                        v_fileDiscounts = null;
                        ntxt_itemDiscount.setValue(new Double(0));
                        cmb_itemUnit.setItems(FXCollections.observableArrayList(Unit.getUnitsByItemId(v_item)));
                    }
                    else if (items.size() > 1) {
                        for (Item _item : items) {
                            MQItemFileRow row = new MQItemFileRow();
                            row.setName(_item.getName());
                            row.setCode(_item.getCode());
                            row.totalProperty().addListener(o -> calculateTotal());
                            insertedFiles.add(row);
                        }
                        tbl_item.getItems().addAll(insertedFiles);
                    }
               }
        );
        
        btn_itemDisc.setOnAction(e -> {
                        FormAddCharges form ;
                        if (v_fileDiscounts != null) 
                            form = new FormAddCharges(v_fileDiscounts, FormConstant.MODE_EDIT);
                        else
                            form = new FormAddCharges();
                        
                        form.setTitle("Add Item Discounts");
                        form.showAndWait();
                        if ( form.getReturnStatus() != FormConstant.RETURN_OK)
                            return;
                        
                        v_fileDiscounts = form.getCharges();
                        calculateDiscountValue();
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
                        v_discounts = SODiscount.convertSODiscountsToCosts(
                                v_salesOrder.getDiscounts());
                        v_miscCharges = SOMiscCharge.convertSOMiscChargesToCosts(
                                v_salesOrder.getMiscCharges());
                        ObservableList<MQItemFileRow> rows = FXCollections.observableArrayList();
                        for (ItemFileRow r : SOFile.convertSOFilesToRows(v_salesOrder.getFiles())) {
                            MQItemFileRow row = new MQItemFileRow(r);
                            row.setOriginalQuantity(r.getQuantity());
                            row.setExistingQuantity(SIFile.getExistingQuantityFor(v_salesOrder, r));
                            if(row.getOriginalQuantity() >= Utility.DOUBLE_TOLERANCE &&
                              (row.getExistingQuantity() < row.getOriginalQuantity()))
                                row.setQuantity(row.getOriginalQuantity() - row.getExistingQuantity());
                            else
                                row.setQuantity(0);
                            row.totalProperty().addListener(o -> calculateTotal());
                            row.quantityProperty().addListener(
                               (ObservableValue<? extends Number> 
                                       _value, Number _old, Number _new) -> {
                                    if (!validateQty(row.getOriginalQuantity(), row.getExistingQuantity(),
                                            _new.doubleValue()))
                                        row.setQuantity(_old.doubleValue());
                                }                            
                            );
                            rows.add(row);
                        }
                        tbl_item.setItems(rows);
                        calculateTotal();;
                }
        );
        
        btn_viewSO.setOnAction(
                e -> {
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
        
        tbl_item.setOnMouseClicked(
                e -> {
                    TablePosition pos = tbl_item.getSelectionModel().getSelectedCells().get(0);
                    ItemFileRow row = tbl_item.getSelectionModel().getSelectedItem();
                    if(e.getClickCount() >= 2) {
                        if(pos.getColumn() == COL_DISC) {
                            FormAddCharges form ;
                            if(row.getDiscounts() != null)
                                form = new FormAddCharges(row.getDiscounts(), FormConstant.MODE_EDIT);
                            else
                                form = new FormAddCharges();
                            form.showAndWait();
                            if(form.getReturnStatus() == FormConstant.RETURN_OK)
                                row.setDiscounts(form.getCharges());
                        }
                    }
                    else if(e.getButton() == MouseButton.SECONDARY) {
                        if(pos.getColumn() == COL_PRICE - 1) {
                            FormPickItemPrice form ;
                            form = new FormPickItemPrice(ItemPrice.getByPackedItem(Transaction.getPackedItem(row)));
                            form.showAndWait();
                            if(form.getReturnStatus() == FormConstant.RETURN_OK)
                                row.setPrice(form.getSelectedPrice().getPrice());
                        }
                    }
                }
        );
    }
    
    protected void setOthersActionsPerformed() {
        
        lbl_paymentType.setOnMouseClicked(e -> {
                    if(e.getClickCount() <= 1)
                        return ;
                    
                    FormSupplier form = new FormSupplier() ;
                    form.showAndWait();
                }
        );
        
        ntxt_itemQty.valueProperty().addListener(
                o -> calculateDiscountValue()
        );
        
        ntxt_itemPrice.valueProperty().addListener(
                o -> calculateDiscountValue()
        );
        
        ntxt_itemPrice.setOnMouseClicked(
                e -> {
                    if(e.getButton() == MouseButton.SECONDARY) {
                            FormPickItemPrice form ;
                            form = new FormPickItemPrice(ItemPrice.getByPackedItem(
                                    PackedItem.getPackedItem(v_item, (Unit) cmb_itemUnit.getValue())));
                            form.showAndWait();
                            if(form.getReturnStatus() == FormConstant.RETURN_OK)
                                ntxt_itemPrice.setValue(form.getSelectedPrice().getPrice());
                    }
                }
        );
    }
    
    protected void calculateDiscountValue() {
        double qty = ntxt_itemQty.getValue();
        double itemPrice = ntxt_itemPrice.getValue();
        double totalDiscount = Cost.getTotalCostsValue(v_fileDiscounts, qty * itemPrice);
        ntxt_itemDiscount.setValue(totalDiscount);
    }
    
    protected void calculateTotal() {
        double total = 0 ;
        
        if (tbl_item.getItems() != null)
            for (ItemFileRow row : tbl_item.getItems()) {
                total += row.getTotal();
            }
        
        ntxt_amount.setValue(total);
        ntxt_discount.setValue(Cost.getTotalCostsValue(v_discounts, total));
        ntxt_miscCharge.setValue(Cost.getTotalCostsValue(v_miscCharges, total));
        ntxt_total.setValue(total - ntxt_discount.getValue() + ntxt_miscCharge.getValue());
    }
    

    protected void addByRow() {
        MQItemFileRow row = new MQItemFileRow() ;
        double qty = ntxt_itemQty.getValue();
        double price = ntxt_itemPrice.getValue();
        row.setId(v_item.getId());
        row.setCode(txt_itemCode.getText());
        row.setName(txt_itemName.getText());
        row.setQuantity(qty);
        row.setUnit((Unit)cmb_itemUnit.getValue());
        row.setPrice(price );
        row.setDiscounts(v_fileDiscounts);
        row.totalProperty().addListener(
                o -> calculateTotal()
        );
        tbl_item.getItems().add(row);
    }

    protected void buildSalesInvoice() {
        if(v_salesInvoice == null)
            v_salesInvoice = new SalesInvoice();
        try
        {
            Calendar due = Calendar.getInstance();
            Calendar issue = Calendar.getInstance();
            
            LocalDate dueDate = dtp_dueDate.getValue();
            LocalDate issueDate = dtp_issueDate.getValue();

            if(txt_siNumber.getText() != null && txt_siNumber.getText().length() >= 0) {
                String sequenceKey = SequenceGenerator.convertLocalDateToSequenceString(issueDate);
                int sequenceValue = SequenceGenerator.getNextSequence(sequenceKey);
                txt_siNumber.setText("SI-" + sequenceKey + "-" + sequenceValue );
            }

            due.setTime(java.sql.Date.valueOf(dueDate));
            issue.setTime(java.sql.Date.valueOf(issueDate));
            
            v_salesInvoice.setTransactionNumber(txt_siNumber.getText());
            v_salesInvoice.setSalesOrder(v_salesOrder);
            v_salesInvoice.setPaymentType((String) cmb_paymentType.getValue());
            v_salesInvoice.setDueDate(due);
            v_salesInvoice.setIssueDate(issue);
            v_salesInvoice.setNote(txa_note.getText());
        }
        catch(Exception _e ) {
            _e.printStackTrace();
        }
    }
    
    protected boolean validateSave() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving sales invoice");
        
        if(v_salesOrder == null) {
            alert.setContentText("There is no sales order for this invoice");
            alert.showAndWait();
            return false ;
        }
        
        if (tbl_item.getItems() == null || tbl_item.getItems().size() <= 0) {
            alert.setContentText("There is no item to be saved");
            alert.showAndWait();
            return false ;
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
        
        if (ntxt_itemPrice.getValue() == null || ntxt_itemPrice.getValue() <= 0) {
            alert.setContentText("The price of the item is not valid");
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
    
    protected boolean validateQty(double _oQty, double _eQty, double _qty) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in setting qty");
        
        if (_qty > (_oQty + _eQty)) {
            alert.setContentText("The qty set is more than ordered  and existingquantity");
            alert.showAndWait();
            return false ;
        }
        
        return true;
    }
    
    protected void disableEditing(boolean _disable) {
        txt_siNumber.setDisable(_disable);
        txt_siNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_soNumber.setDisable(_disable);
        txt_soNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txa_note.setDisable(_disable);
        txa_note.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        ntxt_amount.setDisable(_disable);
        ntxt_amount.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        ntxt_miscCharge.setDisable(_disable);
        ntxt_miscCharge.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        ntxt_discount.setDisable(_disable);
        ntxt_discount.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        ntxt_total.setDisable(_disable);
        ntxt_total.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_paymentType.setDisable(_disable);
        cmb_paymentType.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
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
        ntxt_itemDiscount.setDisable(_disable);
        ntxt_itemDiscount.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        ntxt_itemPrice.setDisable(_disable);
        ntxt_itemPrice.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_itemUnit.setDisable(_disable); 
        cmb_itemUnit.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        
        btn_miscCharge.setVisible(!_disable);
        btn_disc.setVisible(!_disable);
        btn_pickSO.setVisible(!_disable);
        
        tbl_item.setEditable(!_disable);
        area_upperContent.setVisible(!_disable);
        area_bottom.setViewMode(_disable);
        
         if(tbl_item.getColumns() != null) {
            for (TableColumn col : tbl_item.getColumns()) {
                if(col.getId() == null)
                    continue;                
                
                if(col.getId().equals("originalQtyCol") ||
                   col.getId().equals("existingQtyCol"))
                    col.setVisible(false);
            }
        }
    }
    
    
    protected void clearVariables() {
        v_miscCharges = null ;
        v_discounts = null ;
        v_item = null ;
        v_fileDiscounts = null ;
    }
    
    protected void clearInputs() {
        txt_siNumber.clear();
        txt_soNumber.clear();
        dtp_dueDate.setValue(LocalDate.now());
        dtp_issueDate.setValue(LocalDate.now());
        ntxt_amount.clear();
        ntxt_miscCharge.clear();
        ntxt_discount.clear();
        ntxt_total.clear();
        cmb_paymentType.setValue(null);
        v_discounts = null;
        clearUpperMidInputs();
        clearVariables();
    }
    
    protected void clearUpperMidInputs() {
        txt_itemCode.clear();
        txt_itemName.clear();
        ntxt_itemQty.clear();
        ntxt_itemPrice.clear();
        ntxt_itemDiscount.clear();
        cmb_itemUnit.setValue(null);
        v_item = null ;
        v_fileDiscounts = null ;
    }
}
    
   

