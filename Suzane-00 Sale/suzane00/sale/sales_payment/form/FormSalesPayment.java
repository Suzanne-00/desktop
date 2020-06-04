/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_payment.form;

import suzane00.sale.sales_retur.form.*;
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
import javafx.beans.value.ChangeListener;
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
import suzane00.sale.sales_invoice.SalesInvoice;
import suzane00.sale.sales_order.SOFile;
import suzane00.sale.sales_order.SalesOrder;
import suzane00.sale.sales_order.form.FormPickSalesOrder;
import suzane00.sale.sales_order.form.FormSalesOrder;
import suzane00.sale.sales_payment.SPFile;
import suzane00.sale.sales_payment.SalesPayment;
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
import suzane00.transaction.model.PaymentFileRow;

/**
 *
 * @author Usere
 */
public class FormSalesPayment extends Stage {
    
    protected static final int COL_ID = 0 ;
    protected static final int COL_CODE = 1 ;
    protected static final int COL_NAME = 2 ;
    protected static final int COL_ORIGIN_QTY = 3 ;
    protected static final int COL_QTY = 4 ;
    protected static final int COL_UNIT = 5 ;
    
    protected SalesPayment v_salesPayment ;
    protected ChangeListener<? super Customer> v_customerListener;
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
    //protected GridPane area_upperContent;    
    protected TableView<PaymentFileRow> tbl_item;
    
    // area_footer
    protected GridPane area_leftFooter;
    protected GridPane area_rightFooter;
    
    /* level 3 area */
    
    //area_header -> area_leftHeader
    protected Label lbl_paymentNumber;
    protected Label lbl_customer;
    protected Label lbl_paymentType;
    protected TextField txt_paymentNumber;
    protected ComboBox<Customer> cmb_customer;
    protected ComboBox<String> cmb_paymentType;
    
    // area_header -> area_rightHeader
    protected Label lbl_issueDate;
    protected Label lbl_dueDate;
    DatePicker dtp_issueDate;
    DatePicker dtp_dueDate;
    
    // area_content -> area_upperContent
//    protected Label lbl_itemCode;
//    protected Label lbl_itemName;
//    protected Label lbl_itemQty;
//    protected Label lbl_itemDiscount;
//    protected Label lbl_itemPrice;
//    protected Label lbl_itemUnit;
//    protected TextField txt_itemCode;
//    protected TextField txt_itemName;
//    protected NumberTextField ntxt_itemQty;
//    protected NumberTextField ntxt_itemDiscount;
//    protected NumberTextField ntxt_itemPrice;
//    protected ComboBox cmb_itemUnit;
//    protected Button btn_add;
//    protected Button btn_item ;
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
    
    public FormSalesPayment() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        setScene(scene);
    }
    
    public FormSalesPayment(SalesPayment _sp, int _mode) {
        this();
        setVariables(_sp, _mode);
        if (v_mode == FormConstant.MODE_VIEW) {
            disableEditing(true);
            setViewActionPerformed();
        }
        disiplayData();
    }
    
    protected void initComponents() {
        v_salesPayment = null;
        v_mode = FormConstant.MODE_NEW;;
    }
    
    protected void setAppearance() {
        setMainArea() ;
    }
    
    protected void setData() {
        configureItemTableByRow();
        loadCustomerData();
        loadPaymentTypeData();
    }
    
    protected void setActionPerformed() {
        setMainButtonsActionsPerformed();
        setOthersActionsPerformed();
        setTableActionPerformed();
    }
    
    protected void setViewActionPerformed() {
        tbl_item.setEditable(false);
        cmb_customer.valueProperty().removeListener(v_customerListener);
    }
    
    protected void setVariables(SalesPayment _sp, int _mode) {
        v_salesPayment = _sp;
        v_mode = _mode;
    }
    
    protected void disiplayData() {
        Date issueDate = v_salesPayment.getIssueDate().getTime();
        LocalDate issueLocalDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date dueDate = v_salesPayment.getDueDate().getTime();
        LocalDate dueLocalDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        txt_paymentNumber.setText(v_salesPayment.getTransactionNumber());
        cmb_customer.setValue(v_salesPayment.getCustomer());
        cmb_paymentType.setValue(v_salesPayment.getPaymentType());
        txa_note.setText(v_salesPayment.getNote());
        
        dtp_issueDate.setValue(issueLocalDate);
        dtp_dueDate.setValue(dueLocalDate);
        ObservableList<PaymentFileRow> stRows = SPFile.convertFilesToRows(v_salesPayment.getFiles());
        tbl_item.getItems().addAll(stRows);
        calculateTotal();
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
        area_main = new VBox(area_header, tbl_item, area_footer, area_bottom);
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
        //setUpperMidEArea();
        tbl_item = new TableView<>();
        tbl_item.setPrefWidth(Utility.STANDARD_TABLE_WIDTH);
        tbl_item.setPrefHeight(Utility.STANDARD_TABLE_HEIGHT);
//        
//        area_content = new VBox(area_upperContent, tbl_item);
//        area_content.setSpacing(Utility.SHORT_GAP);
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
        lbl_paymentNumber = new Label("Payment Number; ");
        lbl_customer = new Label("Customer: ");
        lbl_paymentType = new Label("Payment Type: ");
        txt_paymentNumber = new TextField();
        cmb_customer = new ComboBox();
        cmb_paymentType = new ComboBox(); 
        area_leftHeader = new GridPane();
        
        txt_paymentNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_customer.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_paymentType.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        
        area_leftHeader = new GridPane();
        area_leftHeader.setHgap(Utility.SHORT_GAP);
        area_leftHeader.setVgap(Utility.SHORT_GAP);
        area_leftHeader.addColumn(0, lbl_paymentNumber);
        area_leftHeader.addColumn(1, txt_paymentNumber);
        area_leftHeader.addColumn(0, lbl_customer);
        area_leftHeader.addColumn(1, cmb_customer);
        area_leftHeader.addColumn(0, lbl_paymentType);
        area_leftHeader.addColumn(1, cmb_paymentType);
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
//    protected void setUpperMidEArea() {
//        lbl_itemCode = new Label("Code: ");
//        lbl_itemName = new Label("Name: ");
//        lbl_itemQty = new Label("Qty: ");
//        lbl_itemUnit = new Label("Unit: ");
//        txt_itemCode = new TextField();
//        txt_itemName = new TextField();
//        ntxt_itemQty = new NumberTextField(); 
//        cmb_itemUnit = new ComboBox();
//        btn_add = new Button("Add");
//        btn_item = new Button(Utility.STANDARD_EXPAND_ICON);
//        area_upperContent = new GridPane();
//        
//        txt_itemCode.setPrefWidth(Utility.SHORT_TEXT_WIDTH);
//        txt_itemCode.setEditable(false);
//        txt_itemName.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
//        txt_itemName.setEditable(false);
//        ntxt_itemQty.setPrefWidth(Utility.SHORT_TEXT_WIDTH);
//        cmb_itemUnit.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
//        area_upperContent.setHgap(Utility.SHORT_GAP);
//        area_upperContent.setVgap(Utility.SHORT_GAP);
//        
//        area_upperContent.add(lbl_itemCode, 0, 0);
//        area_upperContent.add(txt_itemCode, 0, 1);
//        area_upperContent.add(btn_item, 1, 1);
//        area_upperContent.add(lbl_itemName, 2, 0);
//        area_upperContent.add(txt_itemName, 2, 1);
//        area_upperContent.add(lbl_itemQty, 3, 0);
//        area_upperContent.add(ntxt_itemQty, 3, 1);
//        area_upperContent.add(lbl_itemUnit, 4, 0);
//        area_upperContent.add(cmb_itemUnit, 4, 1);
//        area_upperContent.add(btn_add, 8, 1);        
//    }
    
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
        
        TableColumn invoiceNumbCol = new TableColumn("Invoice number");
        invoiceNumbCol.setMinWidth(100);
        invoiceNumbCol.setCellValueFactory(
                new PropertyValueFactory<>("invoiceNumber"));
        
        TableColumn issueDateCol = new TableColumn("Issue Date");
        issueDateCol.setMinWidth(100);
        issueDateCol.setCellValueFactory(new PropertyValueFactory<>("issueDate"));
        
        TableColumn dueDateCol = new TableColumn("Due Date");
        dueDateCol.setMinWidth(100);
        dueDateCol.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        
        TableColumn totalCol = new TableColumn("Total");
        totalCol.setMinWidth(100);
        totalCol.setCellValueFactory(
                new PropertyValueFactory<>("total"));
        
        TableColumn paidCol = new TableColumn("Paid");
        paidCol.setMinWidth(100);
        paidCol.setCellValueFactory(
                new PropertyValueFactory<>("paid"));
        paidCol.setId("paidCol");
        
        TableColumn<PaymentFileRow, Number> amountCol= new TableColumn("Amount");
        amountCol.setMinWidth(100);
        amountCol.setCellValueFactory(
                new PropertyValueFactory<>("amount"));
        amountCol.setCellFactory(TextFieldTableCell.<PaymentFileRow, Number>forTableColumn(new NumberStringConverter()));
        amountCol.setOnEditCommit((TableColumn.CellEditEvent<PaymentFileRow, Number> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setAmount(t.getNewValue().doubleValue());
            }
        );
        
        TableColumn leftAmountCol = new TableColumn("Left Amount");
        leftAmountCol.setMinWidth(100);
        leftAmountCol.setCellValueFactory(
                new PropertyValueFactory<>("leftAmount"));
        leftAmountCol.setId("leftAmountCol");
        
        
        tbl_item.getColumns().addAll(idCol, invoiceNumbCol, issueDateCol, dueDateCol, totalCol, paidCol,
                amountCol, leftAmountCol);
//        tbl_item.getItems().addListener(
//                (javafx.beans.Observable o) -> calculateTotal()
//         );
        tbl_item.setEditable(true);
    }
    
    protected void loadCustomerData() {
        cmb_customer.setItems(Customer.getAllCustomers());
    }
    
    protected void loadPaymentTypeData() {
        ObservableList<String> payments = FXCollections.observableArrayList();
        payments.add(Transaction.TYPE_CASH);
        payments.add(Transaction.TYPE_CREDIT);
        cmb_paymentType.setItems(payments);
    }
    
    protected void calculateTotal() {
        double total = 0 ;
        
        if (tbl_item.getItems() != null)
            for (PaymentFileRow row : tbl_item.getItems()) {
                total += row.getAmount();
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
                            buildSalesPayment();
                            Environment.getEntityManager().getTransaction().begin();
                            SalesPayment.saveSalesPaymentByRow(v_salesPayment, tbl_item.getItems());
                            Environment.getEntityManager().getTransaction().commit();
                            disableEditing(true);
                        }
                        else {
                            buildSalesPayment();
                            Environment.getEntityManager().getTransaction().begin();
                            SalesPayment.editSalesPaymentByRow(v_salesPayment, tbl_item.getItems());
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
    
    protected void setOthersActionsPerformed() {
        v_customerListener = 
                (ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) -> {
                ObservableList<SalesInvoice> invoices = SalesInvoice.getByCustomer(newValue);
                ObservableList<PaymentFileRow> rows = SalesInvoice.convertToRows(invoices);
                for (PaymentFileRow row : rows) {
                    //row.totalProperty().addListener(o -> calculateTotal());
                    row.amountProperty().addListener(
                           (ObservableValue<? extends Number> _value, Number _old, Number _new) -> {
                                if(!validatePrice(row.getTotal(), row.getAmount()))
                                    row.setAmount(_old.doubleValue());
                                else
                                    calculateTotal();
                            }
                    );
                }
                tbl_item.setItems(rows);
                calculateTotal();
            };
        cmb_customer.valueProperty().addListener(v_customerListener);
        
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
    
    
//    protected void addByRow() {
//        MQItemFileRow row = new MQItemFileRow() ;
//        double qty = ntxt_itemQty.getValue();
//        row.setId(v_item.getId());
//        row.setCode(txt_itemCode.getText());
//        row.setName(txt_itemName.getText());
//        row.setQuantity(qty);
//        row.setUnit((Unit)cmb_itemUnit.getValue());
//        row.totalProperty().addListener(
//                o -> calculateTotal()
//        );
//        tbl_item.getItems().add(row);
//    }

    protected void buildSalesPayment() {
        if(v_salesPayment == null)
            v_salesPayment = new SalesPayment();
        try
        {
            Calendar due = Calendar.getInstance();
            Calendar issue = Calendar.getInstance();
            
            LocalDate dueDate = dtp_dueDate.getValue();
            LocalDate issueDate = dtp_issueDate.getValue();

            if(txt_paymentNumber.getText() != null && txt_paymentNumber.getText().length() >= 0) {
                String sequenceKey = SequenceGenerator.convertLocalDateToSequenceString(issueDate);
                int sequenceValue = SequenceGenerator.getNextSequence(sequenceKey);
                txt_paymentNumber.setText("SP-" + sequenceKey + "-" + sequenceValue );
            }

            due.setTime(java.sql.Date.valueOf(dueDate));
            issue.setTime(java.sql.Date.valueOf(issueDate));
            
            v_salesPayment.setTransactionNumber(txt_paymentNumber.getText());
            v_salesPayment.setCustomer((Customer)cmb_customer.getValue());
            v_salesPayment.setPaymentType((String) cmb_paymentType.getValue());
            v_salesPayment.setDueDate(due);
            v_salesPayment.setIssueDate(issue);
            v_salesPayment.setNote(txa_note.getText());
        }
        catch(Exception _e ) {
            _e.printStackTrace();
        }
    }
    
    protected boolean validateSave() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving sales payment");
        
        if(cmb_customer.getValue() == null) {
            alert.setContentText("There is no customer for this sales payment");
            alert.showAndWait();
            return false ;
        }
        
        if (tbl_item.getItems() == null || tbl_item.getItems().size() <= 0) {
            alert.setContentText("There is no item to be saved");
            alert.showAndWait();
            return false ;
        }
        
        for(int i = 0 ; i < tbl_item.getItems().size(); i++) {
            PaymentFileRow row = tbl_item.getItems().get(i);
            if(row.getAmount() > Utility.DOUBLE_TOLERANCE)
                break;
            
            if(i == tbl_item.getItems().size() - 1) {
                alert.setContentText("There is no item to be saved");
                alert.showAndWait();
                return false;
            }
        }
        
        return true;
    }
    
//    protected boolean validateAdd() {
//        Alert alert = new Alert(AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setHeaderText("Error in adding item");
//        
//        if (v_item == null) {
//            alert.setContentText("There is no item to be added");
//            alert.showAndWait();
//            return false ;
//        }
//        
//        if (ntxt_itemQty.getValue() == null || ntxt_itemQty.getValue() <= 0) {
//            alert.setContentText("The quantity of the item is not valid");
//            alert.showAndWait();
//            return false ;
//        }
//        
//        
//        if (cmb_itemUnit.getValue() == null) {
//            alert.setContentText("Please chhose the unit in the item");
//            alert.showAndWait();
//            return false ;
//        }
//        
//        return true;
//    }
    
    protected boolean validatePrice(double _oAmount, double _amount) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in setting amount");
        
       
        if (_amount > _oAmount ) {
            alert.setContentText("The amount set is more than invoiced amount");
            alert.showAndWait();
            return false ;
        }
        
        return true;
    }
    
    protected void disableEditing(boolean _disable) {
        txt_paymentNumber.setDisable(_disable);
        txt_paymentNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_customer.setDisable(_disable);
        cmb_customer.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txa_note.setDisable(_disable);
        txa_note.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_paymentType.setDisable(_disable);
        cmb_paymentType.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        dtp_issueDate.setDisable(_disable);
        dtp_issueDate.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        dtp_dueDate.setDisable(_disable);
        dtp_dueDate.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");

//        txt_itemCode.setDisable(_disable);
//        txt_itemCode.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
//        txt_itemName.setDisable(_disable);
//        txt_itemName.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
//        ntxt_itemQty.setDisable(_disable);
//        ntxt_itemQty.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
//        cmb_itemUnit.setDisable(_disable); 
//        cmb_itemUnit.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
//        
//        btn_pickDO.setVisible(!_disable);
        
        tbl_item.setEditable(!_disable);
//        area_upperContent.setVisible(!_disable);
        area_bottom.setViewMode(_disable);
        
        if(tbl_item.getColumns() != null) {
            for (TableColumn col : tbl_item.getColumns()) {
                if(col.getId() == null)
                    continue;                
                
                if(col.getId().equals("paidCol") || col.getId().equals("leftAmountCol"))
                    col.setVisible(false);
            }
        }
    }
    
    
    protected void clearVariables() {
        v_salesPayment = null;
    }
    
    protected void clearInputs() {
        txt_paymentNumber.clear();
        cmb_customer.setValue(null);
        cmb_paymentType.setValue(null);
        dtp_dueDate.setValue(LocalDate.now());
        dtp_issueDate.setValue(LocalDate.now());
        
//        clearUpperMidInputs();
        clearVariables();
    }
    
//    protected void clearUpperMidInputs() {
//        txt_itemCode.clear();
//        txt_itemName.clear();
//        ntxt_itemQty.clear();
//        cmb_itemUnit.setValue(null);
//        v_item = null ;
//    }
}
    
   

