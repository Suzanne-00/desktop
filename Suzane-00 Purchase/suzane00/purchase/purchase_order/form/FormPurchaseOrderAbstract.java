/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order.form;

import java.time.LocalDate;
import java.util.Map;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardBigFormBottomArea;
import suzane00.inventory.custom_ui.NumberTextField;
import suzane00.transaction.model.ItemFileRow;

/**
 *
 * @author Usere
 */
public abstract class FormPurchaseOrderAbstract extends Stage {
    
    StandardBigFormBottomArea area_bottom;

    TableView<ItemFileRow> tbl_item = new TableView();
    
    Label lbl_poNumber = new Label("PO Number; ");
    Label lbl_supplier = new Label("Supplier: ");
    Label lbl_area = new Label("Destination area: ");
    TextField txt_poNumber = new TextField();
    ComboBox cmb_area = new ComboBox();
    ComboBox cmb_supplier = new ComboBox();
   
    
    Label lbl_issueDate = new Label("Issue date: ");
    Label lbl_dueDate = new Label("Due date: ");
    DatePicker dtp_issueDate = new DatePicker(LocalDate.now());
    DatePicker dtp_dueDate = new DatePicker(LocalDate.now());
        
    Label lbl_note = new Label("Notes: ");
    Label lbl_amount = new Label("Amount: ");
    Label lbl_discount = new Label("Discount: ");
    Label lbl_miscCharge = new Label("Misc charge: ");
    Label lbl_total = new Label("Total: ");    
    TextArea txa_note = new TextArea();
    NumberTextField ntxt_amount = new NumberTextField();
    NumberTextField ntxt_discount = new NumberTextField();
    NumberTextField ntxt_miscCharge = new NumberTextField();
    NumberTextField ntxt_total = new NumberTextField();    
    Button btn_disc = new Button(Utility.STANDARD_EXPAND_ICON);
    Button btn_miscCharge = new Button(Utility.STANDARD_EXPAND_ICON);
    
//    Button btn_save = new Button("Save") ;
//    Button btn_close = new Button("Close");
//    Button btn_new = new Button("New");
//    Button btn_print = new Button("Print");
}
