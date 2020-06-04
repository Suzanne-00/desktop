/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order.form;

import com.sun.jmx.remote.util.EnvHelp;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.application.Application;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
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
import suzane00.transaction.custom_ui.IFRUnitComboBox;
import suzane00.inventory.custom_ui.NumberTextField;
import suzane00.inventory.form.FormPickItem;
import suzane00.inventory.form.FormArea;
import suzane00.transaction.model.ItemFileRow;
import suzane00.source.Address;
import suzane00.source.Supplier;
import suzane00.source.form.FormAddAddress;
import suzane00.source.form.FormSupplier;
import suzane00.purchase.purchase_order.PODiscount;
import suzane00.purchase.purchase_order.POFile;
import suzane00.purchase.purchase_order.POMiscCharge;
import suzane00.purchase.purchase_order.PurchaseOrder;
import suzane00.transaction.Transaction;
import suzane00.transaction.form.FormAddCharges;

/**
 *
 * @author Usere
 */
public class FormPurchaseOrder extends FormPurchaseOrderAbstract implements Observer {
    
    protected static final int COL_ID = 0 ;
    protected static final int COL_CODE = 1 ;
    protected static final int COL_NAME = 2 ;
    protected static final int COL_QTY = 3 ;
    protected static final int COL_UNIT = 4 ;
    protected static final int COL_DISC = 5 ;
    protected static final int COL_DISCS = 6 ;
    protected static final int COL_PRICE = 7 ;
    protected static final int COL_TOTAL = 8 ;
    
    protected PurchaseOrder v_purchaseOrder ;
    protected Item v_item ;
    protected ObservableList<Cost> v_fileDiscounts;
    protected ObservableList<Cost> v_poDiscounts;
    protected ObservableList<Cost> v_miscCharges;
    protected int v_mode = FormConstant.MODE_NEW;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    VBox area_main;
    
    /* level 1 area */
    
    BorderPane area_header;
    VBox area_content;
    BorderPane area_footer;
    // ! StandardBigFormBottomArea area_bottom;
    
    /* level 2 area */
    
    // area_header
    GridPane area_leftHeader;
    GridPane area_rightHeader;
    
    //area_content
    GridPane area_upperContent;    
    // ! TableView<Map> tbl_item = new TableView();
    
    // area_footer
    GridPane area_leftFooter;
    GridPane area_rightFooter;
    
    /* level 3 area */
    
//    area_header -> area_leftHeader
//    ! Label lbl_poNumber = new Label("PO Number; ");
//    ! Label lbl_supplier = new Label("Supplier: ");
//    ! Label lbl_area = new Label("Destination area: ");
//    ! TextField txt_poNumber = new TextField();
//    ! ComboBox cmb_area = new ComboBox();
//    ! ComboBox cmb_supplier = new ComboBox();
//    ! Button btn_supplier = new Button(Utility.STANDARD_EXPAND_ICON); 
//    ! Button btn_area = new Button(Utility.STANDARD_EXPAND_ICON);
    
//    area_header -> area_rightHeader
//    ! Label lbl_issueDate = new Label("Issue date: ");
//    ! Label lbl_dueDate = new Label("Due date: ");
//    ! DatePicker dtp_issueDate = new DatePicker();
//    ! DatePicker dtp_dueDate = new DatePicker();
    
    // area_content -> area_upperContent
    Label lbl_itemCode = new Label("Code: ");
    Label lbl_itemName = new Label("Name: ");
    Label lbl_itemQty = new Label("Qty: ");
    Label lbl_itemDiscount = new Label("Discount: ");
    Label lbl_itemPrice = new Label("Price: ");
    Label lbl_itemUnit = new Label("Unit: ");
    TextField txt_itemCode = new TextField();
    TextField txt_itemName = new TextField();
    NumberTextField ntxt_itemQty = new NumberTextField(); 
    NumberTextField ntxt_itemDiscount = new NumberTextField();
    NumberTextField ntxt_itemPrice = new NumberTextField();
    ComboBox cmb_itemUnit = new ComboBox();
    Button btn_add = new Button("Add");
    Button btn_item = new Button(Utility.STANDARD_EXPAND_ICON);
    Button btn_itemDisc = new Button(Utility.STANDARD_EXPAND_ICON);
    
//     area_footer -> area_leftFooter
//    ! Label lbl_note = new Label("Notes: ");
//    ! TextArea txa_note = new TextArea();
    
//     area_footer -> area_rightFooter
//    ! Label lbl_amount = new Label("Amount: ");
//    ! Label lbl_discount = new Label("Charge: ");
//    ! Label lbl_miscCharge = new Label("Misc charge: ");
//    ! Label lbl_total = new Label("Total: ");    
//    ! NumberTextField ntxt_amount = new NumberTextField();
//    ! NumberTextField ntxt_discount = new NumberTextField();
//    ! NumberTextField ntxt_miscCharge = new NumberTextField();
//    ! NumberTextField ntxt_total = new NumberTextField();    
//    ! Button btn_disc = new Button(Utility.STANDARD_EXPAND_ICON);
//    ! Button btn_miscCharge = new Button(Utility.STANDARD_EXPAND_ICON);
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    public Node getRoot() {
        return area_main ;
    }
    
    public FormPurchaseOrder() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        this.setScene(scene);
    }
    
    public FormPurchaseOrder(PurchaseOrder _po, int _mode) {
        this();
        setVariables(_po, _mode);
        disiplayData();
        if (v_mode == FormConstant.MODE_VIEW) {
            disableEditing(true);
            setViewActionPerformed();
        }
//       System.out.println("AFTER LOAD: " + ( v_purchaseOrder.getDiscounts().get(0).getPurchaseOrder() == null ? "NULL" : "NOT NULL"));
//                            System.out.println("AFTER LOAD: " + ( v_purchaseOrder.getDiscounts().get(0).hashCode()));
    }
        
    @Override
    public void update(Observable _source, Object _object) {
        if (_object == null )  
            return ;
        
        
        if ((_source instanceof FormPickItem))
        {
            ObservableList<ItemFileRow> insertedPOFiles = FXCollections.observableArrayList();
            List<Item> items = (List<Item>) _object;
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
                    ItemFileRow row = new ItemFileRow();
                    row.setName(_item.getName());
                    row.setCode(_item.getCode());
                    row.totalProperty().addListener(o -> calculateTotal());
                    insertedPOFiles.add(row);
                }
                tbl_item.getItems().addAll(insertedPOFiles);
            }
        }
        else if ((_source instanceof FormAddCharges))
        {
            v_poDiscounts = (ObservableList<Cost>) _object;
            calculateTotal();
//            double currentTotal = ntxt_amount.getValue();
//            double totalDiscount = Charge.getTotalDiscountValue(v_poDiscounts, currentTotal);
//            ntxt_discount.setValue(totalDiscount);
//            ntxt_total.setValue(currentTotal - totalDiscount + ntxt_miscCharge.getValue());
        }
    }
    
    protected void initComponents() {
        v_purchaseOrder = null ;
        v_item = null ;
        v_fileDiscounts = null;
        v_poDiscounts = null;
        v_miscCharges = null;
        v_mode = FormConstant.MODE_NEW;
    }
    
    protected void setAppearance() {
        setMainArea() ;
    }
    
    protected void setData() {
        configureItemTableByRow();
        loadSupplierData();
        loadAreaData();
    }
    
    protected void setActionPerformed() {
        setMainButtonsActionsPerformed();
        setButtonsActionsPerformed();
        setTableActionPerformed();
        setOthersActionsPerformed();
    }
    
    protected void setViewActionPerformed() {
        tbl_item.setOnMouseClicked(e -> {
                    if(e.getClickCount() >= 2) {
                        TablePosition pos = tbl_item.getSelectionModel().getSelectedCells().get(0);
                        ItemFileRow row = tbl_item.getSelectionModel().getSelectedItem();
                        if(pos.getColumn() != 5 || row.getDiscounts() == null)
                            return;
                        FormAddCharges form ;
                        form = new FormAddCharges(row.getDiscounts(), FormConstant.MODE_VIEW);
                        form.setTitle("View Item Discounuts");
                        form.showAndWait();
                    }
                }
        );
        
        ntxt_discount.setDisable(false);
        ntxt_discount.setEditable(false);
        ntxt_discount.setOnMouseClicked(
                    e -> {
                        if(e.getClickCount() >= 2 && v_poDiscounts != null) {
                            FormAddCharges form = new FormAddCharges(v_poDiscounts, FormConstant.MODE_VIEW);
                            form.setTitle("View Discounts");
                            form.show();
                        }
                    }
            );
        
        ntxt_miscCharge.setDisable(false);
        ntxt_miscCharge.setEditable(false);
        ntxt_miscCharge. setOnMouseClicked(
                e -> {
                    if(e.getClickCount() >= 2 && v_miscCharges != null) {
                        FormAddCharges form = new FormAddCharges(v_miscCharges, FormConstant.MODE_VIEW);
                        form.setTitle("View Misc Charges");
                        form.show();
                    }
                }
        );
        
    }
    
    protected void setVariables(PurchaseOrder _po, int _mode) {
        v_purchaseOrder = _po;
        v_mode = _mode;
    }
    
    protected void disiplayData() {
        Date issueDate = v_purchaseOrder.getIssueDate().getTime();
        LocalDate issueLocalDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date dueDate = v_purchaseOrder.getDueDate().getTime();
        LocalDate dueLocalDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        txt_poNumber.setText(v_purchaseOrder.getTransactionNumber());
        txa_note.setText(v_purchaseOrder.getNote());
        cmb_supplier.setValue(v_purchaseOrder.getSupplier());
        cmb_area.setValue(v_purchaseOrder.getArea());
        
        dtp_issueDate.setValue(issueLocalDate);
        dtp_dueDate.setValue(dueLocalDate);
        List<ItemFileRow> rows = POFile.convertPOFilesToRows(v_purchaseOrder.getFiles());
        for (ItemFileRow row : rows) {
            row.totalProperty().addListener(o -> calculateTotal());
        }
        tbl_item.getItems().addAll(rows);
        v_poDiscounts = PODiscount.convertPODiscountsToCosts(v_purchaseOrder.getDiscounts());
        v_miscCharges = POMiscCharge.convertPOMiscChargesToCosts(v_purchaseOrder.getMiscCharges());
        calculateTotal();
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
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_PURCHASE));
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
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_PURCHASE));
    }
    
    /* level 2 area */
    
    // setHeaderArea()
    protected void setLeftHeaderArea() {
        area_leftHeader = new GridPane();
        
        txt_poNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_area.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_supplier.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        
        area_leftHeader.setHgap(Utility.SHORT_GAP);
        area_leftHeader.setVgap(Utility.SHORT_GAP);
        area_leftHeader.add(lbl_poNumber, 0, 0);
        area_leftHeader.add(txt_poNumber, 1, 0);
        area_leftHeader.add(lbl_supplier, 0, 1);
        area_leftHeader.add(cmb_supplier, 1, 1);
        area_leftHeader.add(lbl_area, 0, 2);
        area_leftHeader.add(cmb_area, 1, 2);
        
    }
    
    protected void setRightHeaderArea() {
        area_rightHeader = new GridPane();
        
        dtp_issueDate.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        dtp_dueDate.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        
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
        area_upperContent.setPadding(new Insets(Utility.SHORT_GAP));
        
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
        area_upperContent.setBackground(Transaction.getBackground(Transaction.TYPE_PURCHASE));
    }
    
    // setFooterArea()
    protected void setLeftFooterArea()  {
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
        
  
        TableColumn<ItemFileRow, Number> qtyCol = new TableColumn("Quantity");
        qtyCol.setMinWidth(100);
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setCellFactory(TextFieldTableCell.<ItemFileRow, Number>forTableColumn(new NumberStringConverter()));
        qtyCol.setOnEditCommit((TableColumn.CellEditEvent<ItemFileRow, Number> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setQuantity(t.getNewValue().doubleValue());
            }
        );
        
     
        TableColumn<ItemFileRow, Unit> unitCol = new TableColumn("Unit");
        ObservableList<Unit> units = FXCollections.observableArrayList();
        unitCol.setMinWidth(100);
        unitCol.setCellValueFactory(
                new PropertyValueFactory<>("unit"));
        unitCol.setCellFactory((TableColumn<ItemFileRow, Unit> p) -> {
                 return new IFRUnitComboBox();
            }
        );
        unitCol.setOnEditCommit((TableColumn.CellEditEvent<ItemFileRow, Unit> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setUnit(t.getNewValue());
            }
        );
         
        TableColumn<ItemFileRow, Number> priceCol = new TableColumn("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setEditable(true);
        priceCol.setCellFactory(TextFieldTableCell.<ItemFileRow, Number>forTableColumn(new NumberStringConverter()));
        priceCol.setOnEditCommit((TableColumn.CellEditEvent<ItemFileRow, Number> t) -> {
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
        
        tbl_item.getColumns().addAll(idCol, codeCol, nameCol, qtyCol, unitCol, discCol, discsCol, priceCol, totalCol );
        tbl_item.getItems().addListener(
                (javafx.beans.Observable o) -> calculateTotal()
         );
        tbl_item.setEditable(true);
    }
    
    private void configureItemTableByMapping() {
        TableColumn idCol = new TableColumn("Id");
        idCol.setPrefWidth(0);
        idCol.setCellValueFactory(
                new MapValueFactory<>("id"));
        
        TableColumn codeCol = new TableColumn("Code");
        codeCol.setMinWidth(100);
        codeCol.setCellValueFactory(
                new MapValueFactory<>("code"));
        
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new MapValueFactory<>("name"));
        
        TableColumn qtyCol = new TableColumn("Quantity");
        qtyCol.setMinWidth(100);
        qtyCol.setCellValueFactory(
                new MapValueFactory<>("quantity"));
        qtyCol.setEditable(true);
 
        TableColumn unitCol = new TableColumn("Unit");
        unitCol.setMinWidth(100);
        unitCol.setCellValueFactory(
                new MapValueFactory<>("unit"));
        unitCol.setEditable(true);
 
        TableColumn priceCol = new TableColumn("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(
                new MapValueFactory<>("price"));
        priceCol.setEditable(true);
        
        TableColumn discCol = new TableColumn("Discount");
        discCol.setMinWidth(100);
        discCol.setCellValueFactory(
                new MapValueFactory<>("discount"));
        discCol.setEditable(true);
        
        TableColumn discsCol = new TableColumn("");
        discsCol.setPrefWidth(0);
        discsCol.setCellValueFactory(
                new MapValueFactory<>("discounts"));
        discsCol.setEditable(false);
        
        TableColumn totalCol = new TableColumn("Total");
        totalCol.setMinWidth(100);
        totalCol.setCellValueFactory(
                new MapValueFactory<>("total"));
        totalCol.setEditable(false);
        
        tbl_item.getColumns().addAll(idCol, codeCol, nameCol, qtyCol, unitCol, discCol, discsCol, priceCol, totalCol );
        tbl_item.setEditable(true);
    }
    
    protected void loadSupplierData() {
        cmb_supplier.setItems(Supplier.getAllSuppliers());
    }
    
    protected void loadAreaData() {
        cmb_area.setItems(Area.getAllAreas());
    }
    
    protected void setMainButtonsActionsPerformed() {
        area_bottom.setButtonSaveActionPerformed(
                e -> {
                        if(!validateSave()) {
                            area_bottom.isCanceled(true);
                            return;
                        }
                        if (v_mode == FormConstant.MODE_NEW) {
                            buildPurchaseOrder();
                            Environment.getEntityManager().getTransaction().begin();
                            PurchaseOrder.savePurchaseOrderByRow(
                                    v_purchaseOrder, tbl_item.getItems(), v_poDiscounts, v_miscCharges);
                            Environment.getEntityManager().getTransaction().commit();
                            disableEditing(true);
                        }
                        else {
                            buildPurchaseOrder();
                            Environment.getEntityManager().getTransaction().begin();
                            PurchaseOrder.editPurchaseOrderByRow(
                                    v_purchaseOrder, tbl_item.getItems(), v_poDiscounts, v_miscCharges);
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
                    if (v_poDiscounts != null) 
                        form = new FormAddCharges(v_poDiscounts, FormConstant.MODE_EDIT);
                    else
                        form = new FormAddCharges();
                    
                    form.setTitle("Add Discounts");
                    form.showAndWait();
                    if ( form.getReturnStatus() != FormConstant.RETURN_OK)
                        return;

                    v_poDiscounts = form.getCharges();
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
                  
                    ObservableList<ItemFileRow> insertedPOFiles = FXCollections.observableArrayList();
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
                            ItemFileRow row = new ItemFileRow();
                            row.setName(_item.getName());
                            row.setCode(_item.getCode());
                            row.totalProperty().addListener(o -> calculateTotal());
                            insertedPOFiles.add(row);
                        }
                        tbl_item.getItems().addAll(insertedPOFiles);
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
        
        tbl_item.setOnMouseClicked(e -> {
                    if(e.getClickCount() >= 2) {
                        TablePosition pos = tbl_item.getSelectionModel().getSelectedCells().get(0);
                        ItemFileRow row = tbl_item.getSelectionModel().getSelectedItem();
                        if(pos.getColumn() != 5)
                            return;
                        FormAddCharges form ;
                        if(row.getDiscounts() != null)
                            form = new FormAddCharges(row.getDiscounts(), FormConstant.MODE_EDIT);
                        else
                            form = new FormAddCharges();
                        
                        form.setTitle("Add Item Discounts");
                        form.showAndWait();
                        if(form.getReturnStatus() == FormConstant.RETURN_OK)
                            row.setDiscounts(form.getCharges());
                    }
                }
        );
    }
    
    protected void setOthersActionsPerformed() {
        
        lbl_supplier.setOnMouseClicked(e -> {
                    if(e.getClickCount() <= 1)
                        return ;
                    
                    FormSupplier form = new FormSupplier() ;
                    form.setTitle("New Supplier");
                    form.showAndWait();
                }
        );
        
        lbl_area.setOnMouseClicked(e -> {
                    if(e.getClickCount() <= 1)
                        return ;
                    
                    FormArea form = new FormArea() ;
                    form.setTitle("New Area");
                    form.showAndWait();
                }
        );
        
        ntxt_itemQty.valueProperty().addListener(
                o -> calculateDiscountValue()
        );
        
        ntxt_itemPrice.valueProperty().addListener(
                o -> calculateDiscountValue()
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
        ntxt_discount.setValue(Cost.getTotalCostsValue(v_poDiscounts, total));
        ntxt_miscCharge.setValue(Cost.getTotalCostsValue(v_miscCharges, total));
        ntxt_total.setValue(total - ntxt_discount.getValue() + ntxt_miscCharge.getValue());
    }
    
//    protected void addByMapping() {
//        Map row = new HashMap();
//        double qty = Double.valueOf(txt_itemQty.getText());
//        double price = Double.valueOf(txt_itemPrice.getText());
//        double disc = Double.valueOf(txt_itemDiscount.getText());
//        row.put("id", v_item.getId());
//        row.put("code", txt_itemCode.getText());
//        row.put("name", txt_itemName.getText());
//        row.put("quantity", qty);
//        row.put("unit", cmb_itemUnit.getValue());
//        row.put("price", price );
//        row.put("discount", disc);
//        row.put("discounts", v_fileDiscounts);
//        row.put("total", qty * (price - disc));
//        //tbl_item.getItems().add(row);
//    }
    
    protected void addByRow() {
        ItemFileRow row = new ItemFileRow() ;
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

    protected void buildPurchaseOrder() {
        if(v_purchaseOrder == null)
            v_purchaseOrder = new PurchaseOrder();
        try
        {
            Calendar due = Calendar.getInstance();
            Calendar issue = Calendar.getInstance();
            
            LocalDate dueDate = dtp_dueDate.getValue();
            LocalDate issueDate = dtp_issueDate.getValue();

            if(txt_poNumber.getText() != null && txt_poNumber.getText().length() >= 0) {
                String sequenceKey = SequenceGenerator.convertLocalDateToSequenceString(issueDate);
                int sequenceValue = SequenceGenerator.getNextSequence(sequenceKey);
                txt_poNumber.setText("PO-" + sequenceKey + "-" + sequenceValue );
            } 

            due.setTime(java.sql.Date.valueOf(dueDate));
            issue.setTime(java.sql.Date.valueOf(issueDate));

            v_purchaseOrder.setTransactionNumber(txt_poNumber.getText());
            v_purchaseOrder.setArea((Area)cmb_area.getValue());
            v_purchaseOrder.setSupplier((Supplier) cmb_supplier.getValue());
            v_purchaseOrder.setDueDate(due);
            v_purchaseOrder.setIssueDate(issue);
            v_purchaseOrder.setNote(txa_note.getText());
        }
        catch(Exception _e ) {
            _e.printStackTrace();
        }
    }
    
    protected boolean validateSave() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving purchase order");
        
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
            alert.setHeaderText("Error in adding item");
            alert.setContentText("The quantity of the item is not valid");
            alert.showAndWait();
            return false ;
        }
        
        if (ntxt_itemPrice.getValue() == null || ntxt_itemPrice.getValue() <= 0) {
            alert.setHeaderText("Error in adding item");
            alert.setContentText("The price of the item is not valid");
            alert.showAndWait();
            return false ;
        }
        
        if (cmb_itemUnit.getValue() == null) {
            alert.setHeaderText("Error in adding item");
            alert.setContentText("Please chhose the unit in the item");
            alert.showAndWait();
            return false ;
        }
        
        return true;
    }
    
    protected void disableEditing(boolean _disable) {
        txt_poNumber.setDisable(_disable);
        txt_poNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
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
        cmb_supplier.setDisable(_disable);
        cmb_supplier.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
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
        ntxt_itemDiscount.setDisable(_disable);
        ntxt_itemDiscount.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        ntxt_itemPrice.setDisable(_disable);
        ntxt_itemPrice.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_itemUnit.setDisable(_disable); 
        cmb_itemUnit.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        
        btn_miscCharge.setVisible(!_disable);
        btn_disc.setVisible(!_disable);
        
        tbl_item.setEditable(!_disable);
        area_upperContent.setVisible(!_disable);
        area_bottom.setViewMode(_disable);
    }
    
    
    protected void clearVariables() {
        v_miscCharges = null ;
        v_poDiscounts = null ;
        v_item = null ;
        v_fileDiscounts = null ;
    }
    
    protected void clearInputs() {
        txt_poNumber.clear();
        dtp_dueDate.setValue(LocalDate.now());
        dtp_issueDate.setValue(LocalDate.now());
        ntxt_amount.clear();
        ntxt_miscCharge.clear();
        ntxt_discount.clear();
        ntxt_total.clear();
        cmb_supplier.setValue(null);
        cmb_area.setValue(null);
        v_poDiscounts = null;
        tbl_item.getItems().clear();
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
    
   

