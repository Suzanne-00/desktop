/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.transaction.form;

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
public abstract class FormGenericTransactionAbstract extends Stage {
    
    public StandardBigFormBottomArea area_bottom;

    public TableView<ItemFileRow> tbl_item = new TableView();
    
    public Label lbl_poNumber = new Label("PO Number; ");
    public Label lbl_supplier = new Label("Supplier: ");
    public Label lbl_area = new Label("Destination area: ");
    public TextField txt_poNumber = new TextField();
    public ComboBox cmb_area = new ComboBox();
    public ComboBox cmb_supplier = new ComboBox();
   
    
    public Label lbl_issueDate = new Label("Issue date: ");
    public Label lbl_dueDate = new Label("Due date: ");
    public DatePicker dtp_issueDate = new DatePicker(LocalDate.now());
    public DatePicker dtp_dueDate = new DatePicker(LocalDate.now());
        
    public Label lbl_note = new Label("Notes: ");
    public Label lbl_amount = new Label("Amount: ");
    public Label lbl_discount = new Label("Discount: ");
    public Label lbl_miscCharge = new Label("Misc charge: ");
    public Label lbl_total = new Label("Total: ");    
    public TextArea txa_note = new TextArea();
    public NumberTextField ntxt_amount = new NumberTextField();
    public NumberTextField ntxt_discount = new NumberTextField();
    public NumberTextField ntxt_miscCharge = new NumberTextField();
    public NumberTextField ntxt_total = new NumberTextField();    
    public Button btn_disc = new Button(Utility.STANDARD_EXPAND_ICON);
    public Button btn_miscCharge = new Button(Utility.STANDARD_EXPAND_ICON);
    
//    Button btn_save = new Button("Save") ;
//    Button btn_close = new Button("Close");
//    Button btn_new = new Button("New");
//    Button btn_print = new Button("Print");
}
