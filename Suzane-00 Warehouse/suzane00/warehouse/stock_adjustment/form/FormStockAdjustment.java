/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.warehouse.stock_adjustment.form;

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
import suzane00.inventory.SellPriceType;
import suzane00.transaction.custom_ui.IFRUnitComboBox;
import suzane00.inventory.custom_ui.NumberTextField;
import suzane00.inventory.model.StockFileRow;
import suzane00.inventory.stock.Stock;
import suzane00.transaction.Transaction;
import suzane00.transaction.custom_ui.MQIFRUnitComboBox;
import suzane00.transaction.form.FormAddCharges;
import suzane00.transaction.model.ItemFileRow;
import suzane00.transaction.model.MQItemFileRow;
import suzane00.transaction.model.PaymentFileRow;
import suzane00.warehouse.stock_adjustment.SAFile;
import suzane00.warehouse.stock_adjustment.StockAdjustment;

/**
 *
 * @author Usere
 */
public class FormStockAdjustment extends Stage {
    
    protected static final int COL_ID = 0 ;
    protected static final int COL_CODE = 1 ;
    protected static final int COL_NAME = 2 ;
    protected static final int COL_ORIGIN_QTY = 3 ;
    protected static final int COL_QTY = 4 ;
    protected static final int COL_UNIT = 5 ;
    
    protected StockAdjustment v_stockAdjustment ;
    protected ChangeListener<? super Area> v_areaListener;
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
    protected TableView<StockFileRow> tbl_item;
    
    // area_footer
    protected GridPane area_leftFooter;
    protected GridPane area_rightFooter;
    
    /* level 3 area */
    
    //area_header -> area_leftHeader
    protected Label lbl_adjustmentNumber;
    protected Label lbl_area;
    protected TextField txt_adjustmentNumber;
    protected ComboBox<Area> cmb_area;
    
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
    
    public FormStockAdjustment() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        setScene(scene);
    }
    
    public FormStockAdjustment(StockAdjustment _sa, int _mode) {
        this();
        setVariables(_sa, _mode);
        if (v_mode == FormConstant.MODE_VIEW) {
            disableEditing(true);
            setViewActionPerformed();
        }
        disiplayData();
    }
    
    protected void initComponents() {
        v_stockAdjustment = null;
        v_mode = FormConstant.MODE_NEW;;
    }
    
    protected void setAppearance() {
        setMainArea() ;
    }
    
    protected void setData() {
        configureItemTableByRow();
        loadAreaData();
//        loadPaymentTypeData();
    }
    
    protected void setActionPerformed() {
        setMainButtonsActionsPerformed();
        setOthersActionsPerformed();
        setTableActionPerformed();
    }
    
    protected void setViewActionPerformed() {
        tbl_item.setEditable(false);
        cmb_area.valueProperty().removeListener(v_areaListener);
    }
    
    protected void setVariables(StockAdjustment _sa, int _mode) {
        v_stockAdjustment = _sa;
        v_mode = _mode;
    }
    
    protected void disiplayData() {
        Date issueDate = v_stockAdjustment.getIssueDate().getTime();
        LocalDate issueLocalDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date dueDate = v_stockAdjustment.getDueDate().getTime();
        LocalDate dueLocalDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        txt_adjustmentNumber.setText(v_stockAdjustment.getTransactionNumber());
        cmb_area.setValue(v_stockAdjustment.getArea());
        txa_note.setText(v_stockAdjustment.getNote());
        
        dtp_issueDate.setValue(issueLocalDate);
        dtp_dueDate.setValue(dueLocalDate);
        List<StockFileRow> rows = SAFile.convertFilesToRows(v_stockAdjustment.getFiles());
        tbl_item.getItems().addAll(rows);
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
        VBox.setVgrow(area_footer, Priority.ALWAYS);
        area_main.setPrefSize(Utility.STANDARD_FORM_WIDTH, Utility.STANDARD_FORM_HEIGHT);
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_INVENTORY));
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
        //setRightFooterArea();
        
        area_footer = new BorderPane();
        
        area_footer.setLeft(area_leftFooter);
        //area_footer.setRight(area_rightFooter);
    }
    
    protected void setBottomArea() {
        area_bottom = new StandardBigFormBottomArea();
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_INVENTORY));
    }
    
    /* level 2 area */
    
    // setHeaderArea()
    protected void setLeftHeaderArea() {
        lbl_adjustmentNumber = new Label("Payment Number; ");
        lbl_area = new Label("Area: ");
        txt_adjustmentNumber = new TextField();
        cmb_area = new ComboBox();
        area_leftHeader = new GridPane();
        
        txt_adjustmentNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_area.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        
        area_leftHeader = new GridPane();
        area_leftHeader.setHgap(Utility.SHORT_GAP);
        area_leftHeader.setVgap(Utility.SHORT_GAP);
        area_leftHeader.addColumn(0, lbl_adjustmentNumber);
        area_leftHeader.addColumn(1, txt_adjustmentNumber);
        area_leftHeader.addColumn(0, lbl_area);
        area_leftHeader.addColumn(1, cmb_area);
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
    
//    protected void setRightFooterArea()  {
//        
//        lbl_total = new Label("Total: ");    
//        ntxt_total = new NumberTextField();    
//        area_rightFooter = new GridPane();
//        
//        ntxt_total.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
//        ntxt_total.setEditable(false);
//       
//        area_rightFooter.setHgap(Utility.SHORT_GAP);
//        area_rightFooter.setVgap(Utility.SHORT_GAP);
//        
//        area_rightFooter.add(lbl_total, 1, 0);
//        area_rightFooter.add(ntxt_total, 2, 0);
//    }
    
    protected void setBackground() {
        try {
            InputStream isMain = this.getClass().getResource(
                    "/suzane00/global/resource/White Brown Gradient.jpg").openStream();
            BackgroundImage imgMain= new BackgroundImage(new Image(isMain,32,32,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0, 0, false, false, false, true));
          
            area_leftHeader.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
            area_rightHeader.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
            area_leftFooter.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
            //area_rightFooter.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
            area_header.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
            area_footer.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
//            area_content.setStyle("-fx-background-color: rgba(0, 0, 0, 0);");
            area_main.setBackground(new Background(imgMain));
            area_bottom.setBackground(new Background(imgMain));
        }
        catch(IOException e) {
            e.printStackTrace();;
        }
    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
    
    private void configureItemTableByRow() {
        TableColumn areaCol = new TableColumn("Area");
        areaCol.setPrefWidth(0);
        areaCol.setCellValueFactory(
                new PropertyValueFactory<>("area"));
        
        TableColumn itemCol = new TableColumn("Item");
        itemCol.setMinWidth(100);
        itemCol.setCellValueFactory(
                new PropertyValueFactory<>("item"));
        
        TableColumn unitCol = new TableColumn("Unit");
        unitCol.setMinWidth(100);
        unitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
        
        TableColumn qtyCol = new TableColumn("Qty");
        qtyCol.setMinWidth(100);
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
        TableColumn<StockFileRow, Number> adjustedQty = new TableColumn("Adjusted Qty");
        adjustedQty.setMinWidth(100);
        adjustedQty.setCellValueFactory(
                new PropertyValueFactory<>("adjustedQuantity"));
        
        adjustedQty.setCellFactory(TextFieldTableCell.<StockFileRow, Number>forTableColumn(new NumberStringConverter()));
        adjustedQty.setOnEditCommit((TableColumn.CellEditEvent<StockFileRow, Number> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setAdjustedQuantity(t.getNewValue().doubleValue());
            }
        );
        tbl_item.getColumns().addAll(areaCol, itemCol, unitCol, qtyCol, adjustedQty);
//        tbl_item.getItems().addListener(
//                (javafx.beans.Observable o) -> calculateTotal()
//         );
        tbl_item.setEditable(true);
    }
    
    protected void loadAreaData() {
        cmb_area.setItems(Area.getAllAreas());
    }
    
//    protected void loadPaymentTypeData() {
//        ObservableList<String> payments = FXCollections.observableArrayList();
//        payments.add(Transaction.TYPE_CASH);
//        payments.add(Transaction.TYPE_CREDIT);
//        cmb_area.setItems(payments);
//    }
    
//    protected void loadPaymentTypeData() {
//        ObservableList<String> payments = FXCollections.observableArrayList();
//        payments.add(Transaction.TYPE_CASH);
//        payments.add(Transaction.TYPE_CREDIT);
//        cmb_paymentType.setItems(payments);
//    }
    
//    protected void calculateTotal() {
//        double total = 0 ;
//        
//        if (tbl_item.getItems() != null)
//            for (PaymentFileRow row : tbl_item.getItems()) {
//                total += row.getAmount();
//            }
//        
//        ntxt_total.setValue(total);
//    }
    
    protected void setMainButtonsActionsPerformed() {
        area_bottom.setButtonSaveActionPerformed(e -> {
                        if(!validateSave()) {
                            area_bottom.isCanceled(true);
                            return ;
                        }
                        if (v_mode == FormConstant.MODE_NEW) {
                            buildStockAdjustment();
                            Environment.getEntityManager().getTransaction().begin();
                            StockAdjustment.saveStockAdjustmentByRow(v_stockAdjustment, tbl_item.getItems());
                            Environment.getEntityManager().getTransaction().commit();
                            disableEditing(true);
                        }
                        else {
                            buildStockAdjustment();
                            Environment.getEntityManager().getTransaction().begin();
                            StockAdjustment.editStockAdjustmentByRow(v_stockAdjustment, tbl_item.getItems());
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
        v_areaListener = 
                (ObservableValue<? extends Area> area, Area oldValue, Area newValue) -> {
                ObservableList<Stock> stocks = Stock.getStocksBasedOnArea(newValue);
                ObservableList<StockFileRow> rows = Stock.convertStocksToRows(stocks);
//                for (StockFileRow row : rows) {
//                    //row.totalProperty().addListener(o -> calculateTotal());
//                    row.adjustedQuantity().addListener(
//                           (ObservableValue<? extends Number> _value, Number _old, Number _new) -> {
//                                if(!validatePrice(row.getTotal(), row.getAmount()))
//                                    row.setAmount(_old.doubleValue());
//                                else
//                                    calculateTotal();
//                            }
//                    );
//                }
                tbl_item.setItems(rows);
//                calculateTotal();
            };
        cmb_area.valueProperty().addListener(v_areaListener);
        
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

    protected void buildStockAdjustment() {
        if(v_stockAdjustment == null)
            v_stockAdjustment = new StockAdjustment();
        try
        {
            Calendar due = Calendar.getInstance();
            Calendar issue = Calendar.getInstance();
            
            LocalDate dueDate = dtp_dueDate.getValue();
            LocalDate issueDate = dtp_issueDate.getValue();

            if(txt_adjustmentNumber.getText() != null && txt_adjustmentNumber.getText().length() >= 0) {
                String sequenceKey = SequenceGenerator.convertLocalDateToSequenceString(issueDate);
                int sequenceValue = SequenceGenerator.getNextSequence(sequenceKey);
                txt_adjustmentNumber.setText("SA-" + sequenceKey + "-" + sequenceValue );
            }

            due.setTime(java.sql.Date.valueOf(dueDate));
            issue.setTime(java.sql.Date.valueOf(issueDate));
            
            v_stockAdjustment.setTransactionNumber(txt_adjustmentNumber.getText());
            v_stockAdjustment.setArea((Area)cmb_area.getValue());
            v_stockAdjustment.setDueDate(due);
            v_stockAdjustment.setIssueDate(issue);
            v_stockAdjustment.setNote(txa_note.getText());
        }
        catch(Exception _e ) {
            _e.printStackTrace();
        }
    }
    
    protected boolean validateSave() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving stock adjustment ");
        
        if(cmb_area.getValue() == null) {
            alert.setContentText("There is no area for this stock adjustment");
            alert.showAndWait();
            return false ;
        }
        
        if (tbl_item.getItems() == null || tbl_item.getItems().size() <= 0) {
            alert.setContentText("There is no item to be saved");
            alert.showAndWait();
            return false ;
        }
        
        for(int i = 0 ; i < tbl_item.getItems().size(); i++) {
            StockFileRow row = tbl_item.getItems().get(i);
            if(row.getAdjustedQuantity()> Utility.DOUBLE_TOLERANCE)
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
    
//    protected boolean validatePrice(double _oAmount, double _amount) {
//        Alert alert = new Alert(AlertType.ERROR);
//        alert.setTitle("Error");
//        alert.setHeaderText("Error in setting amount");
//        
//       
//        if (_amount > _oAmount ) {
//            alert.setContentText("The amount set is more than invoiced amount");
//            alert.showAndWait();
//            return false ;
//        }
//        
//        return true;
//    }
    
    protected void disableEditing(boolean _disable) {
        txt_adjustmentNumber.setDisable(_disable);
        txt_adjustmentNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_area.setDisable(_disable);
        cmb_area.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txa_note.setDisable(_disable);
        txa_note.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
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
        
//        if(tbl_item.getColumns() != null) {
//            for (TableColumn col : tbl_item.getColumns()) {
//                if(col.getId() == null)
//                    continue;                
//                
//                if(col.getId().equals("paidCol") || col.getId().equals("leftAmountCol"))
//                    col.setVisible(false);
//            }
//        }
    }
    
    
    protected void clearVariables() {
        v_stockAdjustment = null;
    }
    
    protected void clearInputs() {
        txt_adjustmentNumber.clear();
        cmb_area.setValue(null);
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
    
   

