/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order.form;

import java.util.List;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import suzane00.global.Utility;
import suzane00.inventory.Cost;
import suzane00.purchase.purchase_order.POMiscCharge;

/**
 *
 * @author Usere
 */
public class DlgAddMiscCharge extends Dialog<ObservableList<Cost>> {
    
    private final String V_TITLE = "Add Misc Charge";
    private final String v_DESCRIPTION = "Please enter the misc charges and click ok button";
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
            
    protected HBox area_main;
    
    /* level 1 area */
    
    // area_main
    protected GridPane area_left;
    protected TableView<Cost> tbl_miscCharge = new TableView<Cost>();
    
    /* level 2 area */
    
    //area_main -> area_left
    protected Label lbl_type = new Label("Type: ");
    protected Label lbl_name = new Label("Name: ");
    protected Label lbl_value = new Label("Value: "); 
    protected TextField txt_name = new TextField();
    protected TextField txt_value = new TextField();
    protected ComboBox cmb_type = new ComboBox();
    
    // aadditional components
    protected Button btn_add = new Button("Add");
    protected ButtonType btn_ok = new ButtonType("Ok", ButtonData.OK_DONE);
    protected ButtonType btn_cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    protected ButtonType btn_new = new ButtonType("New", ButtonData.FINISH);
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    
    public DlgAddMiscCharge() {
        setAppearance();
        setData();
        setActionPerformed();
        setDialog();
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        loadTypeData();
        configureDiscountTableData();
    }
    
    protected void setActionPerformed() {
       
        btn_add.setOnAction(e -> {
                    Cost misc = new Cost();
                    buildMiscCharge(misc);
                    tbl_miscCharge.getItems().add(misc);
                    clearInputs();
                }
        );
    }
    
    protected void setDialog() {
        setTitle(V_TITLE);
        setHeaderText(v_DESCRIPTION);
        setResizable(true);
        getDialogPane().setContent(area_main);
        getDialogPane().getButtonTypes().addAll(btn_new, btn_ok, btn_cancel);
        setResultConverter(
                b -> {
                    if (b == btn_ok) 
                        return tbl_miscCharge.getItems();
                    else 
                        return null ;
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setLeftArea();
        tbl_miscCharge.setPrefSize(Utility.STANDARD_TABLE_WIDTH, Utility.STANDARD_TABLE_HEIGHT);
        area_main = new HBox(area_left, tbl_miscCharge);
        area_main.setSpacing(Utility.SHORT_GAP);
    }
    
    /* level 1 area */
    
    // setMainArea()
    protected void setLeftArea() {
        area_left = new GridPane();
        
        txt_name.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_type.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_value.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_left.setHgap(Utility.SHORT_GAP);
        area_left.setVgap(Utility.SHORT_GAP);
        GridPane.setHalignment(lbl_value, HPos.LEFT);
        GridPane.setValignment(lbl_value, VPos.TOP);     
        
        area_left.add(lbl_type, 0, 0);
        area_left.add(cmb_type, 1, 0);
        area_left.add(lbl_name, 0, 1);
        area_left.add(txt_name, 1, 1);
        area_left.add(lbl_value, 0, 2);
        area_left.add(txt_value, 1, 2);
        area_left.add(btn_add, 1, 3);
        
        GridPane.setValignment(btn_add, VPos.BOTTOM);
        GridPane.setHalignment(btn_add, HPos.RIGHT);
        
    }
     
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
     
    protected void configureDiscountTableData() {
        TableColumn typeCol = new TableColumn("Type");
        typeCol.setPrefWidth(100);
        typeCol.setCellValueFactory(
                new PropertyValueFactory<>("type"));
        
        TableColumn<Cost, String> nameCol = new TableColumn("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.<Cost>forTableColumn());
        nameCol.setOnEditCommit((TableColumn.CellEditEvent<Cost, String> t) -> {
                ((Cost) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                        ).setName(t.getNewValue());
        });
        
        TableColumn valueCol = new TableColumn("Value");
        valueCol.setPrefWidth(100);
        valueCol.setCellValueFactory(
                new PropertyValueFactory<>("value"));
        
        tbl_miscCharge.getColumns().addAll(typeCol, nameCol, valueCol);
        tbl_miscCharge.setEditable(true);    
    }
    
    
    protected void loadTypeData() {
        cmb_type.getItems().add(Utility.TYPE_PERCENTAGE);
        cmb_type.getItems().add(Utility.TYPE_VALUE);
    }
    
    protected void clearInputs() {
        txt_name.clear();
        txt_value.clear();
        cmb_type.setValue(null);
    }
    
    protected void buildMiscCharge(Cost _misc) {
        _misc.setType((String) cmb_type.getValue());
        _misc.setName(txt_name.getText());
        _misc.setValue(Double.valueOf(txt_value.getText()));
    }
}
