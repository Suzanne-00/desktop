/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.warehouse.transfer_stock.form;

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
import suzane00.inventory.model.StockFileRow;
import suzane00.inventory.stock.Stock;
import suzane00.inventory.stock.form.FormPickStock;
import suzane00.inventory.stock.form.FormViewStock;
import suzane00.transaction.Transaction;
import suzane00.transaction.custom_ui.MQIFRUnitComboBox;
import suzane00.transaction.form.FormAddCharges;
import suzane00.transaction.model.ItemFileRow;
import suzane00.transaction.model.MQItemFileRow;
import suzane00.transaction.model.PaymentFileRow;
import suzane00.warehouse.stock_adjustment.SAFile;
import suzane00.warehouse.stock_adjustment.StockAdjustment;
import suzane00.warehouse.transfer_stock.TSFile;
import suzane00.warehouse.transfer_stock.TransferStock;

/**
 *
 * @author Usere
 */
public class FormTransferStock extends Stage {
    
    protected static final int COL_ID = 0 ;
    protected static final int COL_CODE = 1 ;
    protected static final int COL_NAME = 2 ;
    protected static final int COL_ORIGIN_QTY = 3 ;
    protected static final int COL_QTY = 4 ;
    protected static final int COL_UNIT = 5 ;
    
    protected TransferStock v_transferStock ;
    protected ChangeListener<? super Area> v_areaListener;
    protected int v_mode = FormConstant.MODE_NEW;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    protected VBox area_main;
    
    /* level 1 area */
    
    protected BorderPane area_header;
    protected HBox area_content;
    protected BorderPane area_footer;
    protected StandardBigFormBottomArea area_bottom;   
    
    /* level 2 area */
    
    // area_header
    protected GridPane area_leftHeader;
    protected GridPane area_rightHeader;
    
    //area_content
    protected VBox area_leftContent;
    protected VBox area_rightContent;
    protected Button btn_transfer;
    
    // area_footer
    protected GridPane area_leftFooter;
    protected GridPane area_rightFooter;
    
    /* level 3 area */
    
    //area_header -> area_leftHeader
    protected Label lbl_transferNumber;
    protected TextField txt_transferNumber;
    
    // area_header -> area_rightHeader
    protected Label lbl_issueDate;
    protected Label lbl_dueDate;
    DatePicker dtp_issueDate;
    DatePicker dtp_dueDate;
    
    // area_content -> area_leftContent 
    protected HBox area_upperLeftContent;
    protected TableView<StockFileRow> tbl_sourceStock;

    // area_content -> area_rightContent 
    protected HBox area_upperRightContent;
    protected TableView<StockFileRow> tbl_destinationStock;
   
    
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
    
    /* level 4 area */
    
    // area_content -> area_leftContent -> area_upperleftContent
    protected Label lbl_sourceArea;
    protected ComboBox<Area> cmb_sourceArea;
    protected Button btn_pickStock;
    
    // area_content -> area_rightContent -> area_upperRightContent
    protected Label lbl_destinationArea;
    protected ComboBox<Area> cmb_destinationArea;
    protected Button btn_viewStock;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    public Node getRoot() {
        return area_main ;
    }
    
    public FormTransferStock() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        setScene(scene);
    } 
    
    public FormTransferStock(TransferStock _ts, int _mode) {
        this();
        setVariables(_ts, _mode);
        if (v_mode == FormConstant.MODE_VIEW) {
            disableEditing(true);
            setViewActionPerformed(); 
        }
        disiplayData();
    }
    
    protected void initComponents() {
        v_transferStock = null;
        v_mode = FormConstant.MODE_NEW;;
    }
    
    protected void setAppearance() {
        setMainArea() ;
    }
    
    protected void setData() {
        configureTablesByRow();
        loadAreaData();
//        loadPaymentTypeData();
    }
    
    protected void setActionPerformed() {
        setMainButtonsActionsPerformed();
        setButtonsActionPerformed();
        setOthersActionsPerformed();
        setTableActionPerformed();
    }
    
    protected void setViewActionPerformed() {
        tbl_sourceStock.setEditable(false);
        cmb_sourceArea.valueProperty().removeListener(v_areaListener);
    }
    
    protected void setVariables(TransferStock _ts, int _mode) {
        v_transferStock = _ts;
        v_mode = _mode;
    }
    
    protected void disiplayData() {
        Date issueDate = v_transferStock.getIssueDate().getTime();
        LocalDate issueLocalDate = issueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Date dueDate = v_transferStock.getDueDate().getTime();
        LocalDate dueLocalDate = dueDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        txt_transferNumber.setText(v_transferStock.getTransactionNumber());
        cmb_sourceArea.setValue(v_transferStock.getSourceArea());
        cmb_destinationArea.setValue(v_transferStock.getDestinationArea());
        txa_note.setText(v_transferStock.getNote());
        
        dtp_issueDate.setValue(issueLocalDate);
        dtp_dueDate.setValue(dueLocalDate);
        List<StockFileRow> rows = TSFile.convertFilesToRows(v_transferStock.getFiles());
        tbl_sourceStock.getItems().addAll(rows);
        reaarangeAppearance();
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
        VBox.setVgrow(area_content, Priority.ALWAYS);
        area_main.setPrefSize(Utility.STANDARD_FORM_WIDTH, Utility.STANDARD_FORM_HEIGHT);
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_INVENTORY));
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_INVENTORY));
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
        setLeftContentArea();
        setRightContentArea();
        btn_transfer = new Button(">>>");
        area_content = new HBox(area_leftContent, btn_transfer, area_rightContent);
        HBox.setHgrow(area_leftContent, Priority.ALWAYS);
        HBox.setHgrow(area_rightContent, Priority.ALWAYS);
        area_content.setSpacing(Utility.STANDARD_GAP);
        area_content.setAlignment(Pos.CENTER);
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
    }
    
    /* level 2 area */
    
    // setHeaderArea()
    protected void setLeftHeaderArea() {
        lbl_transferNumber = new Label("Payment Number; ");
        txt_transferNumber = new TextField();
        area_leftHeader = new GridPane();
        
        txt_transferNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        
        area_leftHeader = new GridPane();
        area_leftHeader.setHgap(Utility.SHORT_GAP);
        area_leftHeader.setVgap(Utility.SHORT_GAP);
        area_leftHeader.addColumn(0, lbl_transferNumber);
        area_leftHeader.addColumn(1, txt_transferNumber);
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
    protected void setLeftContentArea() {
        setUpperLeftContentArea();
        tbl_sourceStock = new TableView<>();
        area_leftContent = new VBox(area_upperLeftContent, tbl_sourceStock);
        VBox.setVgrow(tbl_sourceStock, Priority.ALWAYS);
        tbl_sourceStock.setPrefSize(Utility.SMALL_TABLE_WIDTH, Utility.SMALL_TABLE_HEIGHT);
        area_leftContent.setSpacing(Utility.SHORT_GAP);
    }
    
    protected void setRightContentArea() {
        setUpperRightContentArea();
        tbl_destinationStock = new TableView<>();
        area_rightContent = new VBox(area_upperRightContent, tbl_destinationStock);
        VBox.setVgrow(tbl_destinationStock, Priority.ALWAYS);
        tbl_destinationStock.setPrefSize(Utility.SMALL_TABLE_WIDTH, Utility.SMALL_TABLE_HEIGHT);
        area_rightContent.setSpacing(Utility.SHORT_GAP);
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
    
    /* level 3 area */
    
    // setContentArea() -> setLeftContentArea()
    protected void setUpperLeftContentArea() {
        lbl_sourceArea = new Label("Source area: ");
        cmb_sourceArea = new ComboBox<>();
        btn_pickStock = new Button("Choose stocks");
        cmb_sourceArea.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_upperLeftContent = new HBox(lbl_sourceArea, cmb_sourceArea, btn_pickStock);
        area_upperLeftContent.setSpacing(Utility.SHORT_GAP);
    }
    
    // setContentArea() -> setRightContentArea()
    protected void setUpperRightContentArea() {
        lbl_destinationArea = new Label("Dest area: ");
        cmb_destinationArea = new ComboBox<>();
        btn_viewStock = new Button("View stocks");
        cmb_sourceArea.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_upperRightContent = new HBox(lbl_destinationArea, cmb_destinationArea, btn_viewStock);
        area_upperRightContent.setSpacing(Utility.SHORT_GAP);
    }
    
    protected void reaarangeAppearance() {
        area_main.getChildren().clear();
        area_content.getChildren().clear();
        area_leftContent.getChildren().clear();
        area_rightContent.getChildren().clear();
        area_upperLeftContent.getChildren().clear();
        area_upperRightContent.getChildren().clear();
        area_leftHeader.addColumn(0, lbl_sourceArea);
        area_leftHeader.addColumn(1, cmb_sourceArea);
        area_leftHeader.addColumn(0, lbl_destinationArea);
        area_leftHeader.addColumn(1, cmb_destinationArea);
        area_main.getChildren().addAll(area_header, tbl_sourceStock, area_footer, area_bottom);
    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
    
    private void configureTablesByRow() {
      
        TableColumn sourceItemCol = new TableColumn("Item");
        sourceItemCol.setMinWidth(100);
        sourceItemCol.setCellValueFactory(
                new PropertyValueFactory<>("item"));
        
        TableColumn sourceUnitCol = new TableColumn("Unit");
        sourceUnitCol.setMinWidth(100);
        sourceUnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
        
        TableColumn<StockFileRow, Number> sourceQtyCol = new TableColumn("Qty");
        sourceQtyCol.setMinWidth(100);
        sourceQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
//        sourceQtyCol.setCellFactory(TextFieldTableCell.<StockFileRow, Number>forTableColumn(new NumberStringConverter()));
//        sourceQtyCol.setOnEditCommit((TableColumn.CellEditEvent<StockFileRow, Number> t) -> {
//                t.getTableView().getItems().get(
//                t.getTablePosition().getRow())
//                 .setQuantity(t.getNewValue().doubleValue());
//            }
//        );
        sourceQtyCol.setId("sourceQtyCol");
        
        TableColumn<StockFileRow, Number> sourceAdjQtyCol = new TableColumn("Transfer Qty");
        sourceAdjQtyCol.setMinWidth(100);
        sourceAdjQtyCol.setCellValueFactory(new PropertyValueFactory<>("adjustedQuantity"));
        sourceAdjQtyCol.setCellFactory(TextFieldTableCell.<StockFileRow, Number>forTableColumn(new NumberStringConverter()));
        sourceAdjQtyCol.setOnEditCommit((TableColumn.CellEditEvent<StockFileRow, Number> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setAdjustedQuantity(t.getNewValue().doubleValue());
            }
        );
        
        TableColumn destItemCol = new TableColumn("Item");
        destItemCol.setMinWidth(100);
        destItemCol.setCellValueFactory(
                new PropertyValueFactory<>("item"));
        
        TableColumn destUnitCol = new TableColumn("Unit");
        destUnitCol.setMinWidth(100);
        destUnitCol.setCellValueFactory(new PropertyValueFactory<>("unit"));
        
        TableColumn destQtyCol = new TableColumn("Qty");
        destQtyCol.setMinWidth(100);
        destQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        
       
        tbl_sourceStock.getColumns().addAll(sourceItemCol, sourceUnitCol, sourceQtyCol, sourceAdjQtyCol);
        tbl_destinationStock.getColumns().addAll(destItemCol, destUnitCol, destQtyCol);
//        tbl_item.getItems().addListener(
//                (javafx.beans.Observable o) -> calculateTotal()
//         );
        tbl_sourceStock.setEditable(true);
    }
    
    protected void loadAreaData() {
        cmb_sourceArea.setItems(Area.getAllAreas());
        cmb_destinationArea.setItems(Area.getAllAreas());
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
        area_bottom.setButtonSaveActionPerformed(
                e -> {
                        if(!validateSave()) {
                            area_bottom.isCanceled(true);
                            return ;
                        }
                        if (v_mode == FormConstant.MODE_NEW) {
                            buildTransferStock();
                            Environment.getEntityManager().getTransaction().begin();
                            TransferStock.saveTransferStockByRow(v_transferStock, tbl_destinationStock.getItems());
                            Environment.getEntityManager().getTransaction().commit();
                            disableEditing(true);
                        }
                        else {
                            buildTransferStock();
                            Environment.getEntityManager().getTransaction().begin();
                            TransferStock.editTransferStockByRow(v_transferStock, tbl_destinationStock.getItems());
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
    
    protected void setButtonsActionPerformed() {
        btn_pickStock.setOnAction (
            e -> {
                FormPickStock form = new FormPickStock();
                form.searchStocksBasedOnArea((Area) cmb_sourceArea.getValue());
                form.showAndWait();
                if(form.getReturnStatus() != FormConstant.RETURN_OK)
                    return;

                ObservableList<StockFileRow> insertedStocks = Stock.convertStocksToRows(form.getSelectedItems());
                for (StockFileRow insertedStock : insertedStocks) {
                    insertedStock.setQuantity(0);
                }
                tbl_sourceStock.setItems(insertedStocks);
            }
        );
        
        btn_transfer.setOnAction(
            e -> {
                if(!validateTransfer())
                    return;
                
                for (StockFileRow row : tbl_sourceStock.getItems()) {
                    if(row.getAdjustedQuantity()> Utility.DOUBLE_TOLERANCE) {
                        StockFileRow r = new StockFileRow();
                        r.setArea(row.getArea());
                        r.setItem(row.getItem());
                        r.setUnit(row.getUnit());
                        r.setQuantity(row.getAdjustedQuantity());
                        r.setAdjustedQuantity(0);
                        tbl_destinationStock.getItems().add(r);
                    }
                }
            }
        );
        
        btn_viewStock.setOnAction(
                e -> {
                    FormViewStock form = new FormViewStock();
                    form.searchStocksBasedOnArea((Area) cmb_destinationArea.getValue());
                    form.setTitle("Stocks List");
                    form.showAndWait();
                }
        );
    }
    
    protected void setOthersActionsPerformed() {
        v_areaListener = 
                (ObservableValue<? extends Area> area, Area oldValue, Area newValue) -> {
                    ObservableList<StockFileRow> rows = Stock.convertStocksToRows(
                            Stock.getStocksBasedOnArea(newValue));
                    for (StockFileRow row : rows) {
                        row.setAdjustedQuantity(0);
                    }
                    tbl_sourceStock.setItems(rows);
                };
        cmb_sourceArea.valueProperty().addListener(v_areaListener);
    }
    
    protected void setTableActionPerformed() {
        tbl_sourceStock.setOnKeyPressed(e -> {
                    if(e.getCode()== KeyCode.DELETE) {
                        int sel = tbl_sourceStock.getSelectionModel().getSelectedIndex() ;
                        tbl_sourceStock.getItems().remove(sel);
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

    protected void buildTransferStock() {
        if(v_transferStock == null)
            v_transferStock = new TransferStock();
        try
        {
            Calendar due = Calendar.getInstance();
            Calendar issue = Calendar.getInstance();
            
            LocalDate dueDate = dtp_dueDate.getValue();
            LocalDate issueDate = dtp_issueDate.getValue();

            if(txt_transferNumber.getText() != null && txt_transferNumber.getText().length() >= 0) {
                String sequenceKey = SequenceGenerator.convertLocalDateToSequenceString(issueDate);
                int sequenceValue = SequenceGenerator.getNextSequence(sequenceKey);
                txt_transferNumber.setText("TS-" + sequenceKey + "-" + sequenceValue );
            }

            due.setTime(java.sql.Date.valueOf(dueDate));
            issue.setTime(java.sql.Date.valueOf(issueDate));
            
            v_transferStock.setTransactionNumber(txt_transferNumber.getText());
            v_transferStock.setSourceArea((Area)cmb_sourceArea.getValue());
            v_transferStock.setDestinationArea((Area)cmb_destinationArea.getValue());
            v_transferStock.setDueDate(due);
            v_transferStock.setIssueDate(issue);
            v_transferStock.setNote(txa_note.getText());
        }
        catch(Exception _e ) {
            _e.printStackTrace();
        }
    }
    
    
    protected boolean validateSave() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving transfer stock");
        
        if(cmb_sourceArea.getValue() == null) {
            alert.setContentText("There is no source area for this transfer stock");
            alert.showAndWait();
            return false ;
        }
        
        if(cmb_destinationArea.getValue() == null) {
            alert.setContentText("There is no destination area for this transfer stock");
            alert.showAndWait();
            return false ;
        }
        
        if (tbl_destinationStock.getItems() == null || tbl_sourceStock.getItems().size() <= 0) {
            alert.setContentText("There is no item to be saved");
            alert.showAndWait();
            return false ;
        }
        
//        for(int i = 0 ; i < tbl_sourceStock.getItems().size(); i++) {
//            StockFileRow row = tbl_sourceStock.getItems().get(i);
//            if(row.getAdjustedQuantity()> Utility.DOUBLE_TOLERANCE)
//                break;
//            
//            if(i == tbl_sourceStock.getItems().size() - 1) {
//                alert.setContentText("There is no item to be saved");
//                alert.showAndWait();
//                return false;
//            }
//        }
        
        return true;
    }
    
    protected boolean validateTransfer() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving transfer stock");
        
        for (StockFileRow stock : tbl_sourceStock.getItems()) {
            if(stock.getAdjustedQuantity()> Utility.DOUBLE_TOLERANCE)
                return true;
            
            if(stock.getAdjustedQuantity() < 0) {
                alert.setContentText("There is invalid negative input on the transfered stocks");
                alert.showAndWait();
                return false ;
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
        txt_transferNumber.setDisable(_disable);
        txt_transferNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_sourceArea.setDisable(_disable);
        cmb_sourceArea.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_destinationArea.setDisable(_disable);
        cmb_destinationArea.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
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
        
        tbl_sourceStock.setEditable(!_disable);
//        area_upperContent.setVisible(!_disable);
        area_bottom.setViewMode(_disable);
        
        if(tbl_sourceStock.getColumns() != null) {
            for (TableColumn col : tbl_sourceStock.getColumns()) {
                if(col.getId() == null)
                    continue;                
                
                if(col.getId().equals("sourceQtyCol"))
                    col.setVisible(false);
            }
        }
    }
    
    
    protected void clearVariables() {
        v_transferStock = null;
    }
    
    protected void clearInputs() {
        txt_transferNumber.clear();
        cmb_sourceArea.setValue(null);
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
    
   

