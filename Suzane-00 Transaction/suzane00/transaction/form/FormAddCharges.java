/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.transaction.form;

import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import suzane00.global.FormConstant;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardDialogBottomArea;
import suzane00.inventory.Cost;
import suzane00.inventory.Item;
import suzane00.inventory.custom_ui.ChargeTypeComboBox;
import suzane00.inventory.custom_ui.NumberTextField;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
public class FormAddCharges extends Stage implements Observable {
    
    private int v_returnStatus ;
    private int v_mode;
    private ObservableList<Cost> v_charges;
    private LinkedList<Observer> observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    protected HBox area_content;
    protected StandardDialogBottomArea area_bottom;
    
    /* level 2 area */
    
    // area_content
    protected GridPane area_left;
    protected TableView<Cost> tbl_discount = new TableView<Cost>();
    
    /* level 3 area */
    
    //area_content -> area_left
    protected Label lbl_type = new Label("Type: ");
    protected Label lbl_name = new Label("Name: ");
    protected Label lbl_value = new Label("Value: "); 
    protected TextField txt_name = new TextField();
    protected NumberTextField ntxt_value = new NumberTextField();
    protected ComboBox cmb_type = new ComboBox();
    protected Button btn_add = new Button("Add");
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    
    public FormAddCharges() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        this.setScene(scene);
    }
    
//     public FormAddCharges(ObservableList<Cost> _charges) {
//        this();
//        v_charges = _charges;
//        tbl_discount.setItems(v_charges);
//        if (v_mode == FormConstant.MODE_VIEW)
//            disableEditing(true);
//     }
    
     public FormAddCharges(ObservableList<Cost> _charges, int _mode) {
        this();
        v_charges = _charges;
        v_mode = _mode;
        tbl_discount.setItems(v_charges);
        if (v_mode == FormConstant.MODE_VIEW)
            disableEditing(true);
    }
    
    public int getReturnStatus() {
        return v_returnStatus;
    }
    
    public ObservableList<Cost> getCharges() {
        return tbl_discount.getItems();
    }
    
    @Override
    public void addObserver(Observer _obs) {
        observers.add(_obs);
    }

    @Override
    public void notifyObservers() {
       for (Observer o : observers) {
            o.update(this, tbl_discount.getItems());
        }    
    }
    
    protected void initComponents() {
        v_returnStatus = FormConstant.RETURN_OK;
        v_mode = FormConstant.MODE_NEW;
        ObservableList<Cost> v_charges = null ;
        observers = new LinkedList<Observer>();
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        loadTypeData();
        configureDiscountTableData();
    }
    
    protected void setActionPerformed() {
        
        area_bottom.setButtonOkActionPerformed(
                e -> {
                    notifyObservers();
                    clearInputs();
                    v_returnStatus = FormConstant.RETURN_OK;
                    close();
                }
        );
        
        area_bottom.setButtonCancelActionPerformed(e -> { 
                    v_returnStatus = FormConstant.RETURN_CANCEL;
                    close();
                }
        );
        
        area_bottom.setButtonNewActionPerformed(e -> clearInputs());
        
        btn_add.setOnAction(
                e -> {
                    if(!validateAdd())
                        return;
                    
                    Cost disc = new Cost();
                    buildDiscount(disc);
                    tbl_discount.getItems().add(disc);
                    clearInputs();
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setContentArea();
        setBottomArea();
        
        area_main = new VBox(area_content, area_bottom);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setBackground(Item.getMasterBackground());
    }
    
    /* level 1 area */
    
    protected void setContentArea() {
        setLeftArea();
        tbl_discount.setPrefSize(Utility.STANDARD_TABLE_WIDTH, Utility.STANDARD_TABLE_HEIGHT);
        area_content = new HBox(area_left, tbl_discount);
        area_content.setSpacing(Utility.SHORT_GAP);
    }
    
    protected void setBottomArea() {
        area_bottom = new StandardDialogBottomArea();
    }
    
    /* level 2 area */
    
    // setContentArea()
    protected void setLeftArea() {
        area_left = new GridPane();
        
        txt_name.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_type.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        ntxt_value.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_left.setHgap(Utility.SHORT_GAP);
        area_left.setVgap(Utility.SHORT_GAP);
        GridPane.setHalignment(lbl_value, HPos.LEFT);
        GridPane.setValignment(lbl_value, VPos.TOP);     
        
        area_left.add(lbl_type, 0, 0);
        area_left.add(cmb_type, 1, 0);
        area_left.add(lbl_name, 0, 1);
        area_left.add(txt_name, 1, 1);
        area_left.add(lbl_value, 0, 2);
        area_left.add(ntxt_value, 1, 2);
        area_left.add(btn_add, 1, 3);
        
        GridPane.setValignment(btn_add, VPos.BOTTOM);
        GridPane.setHalignment(btn_add, HPos.RIGHT);
        
    }   
     
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
     
    protected void configureDiscountTableData() {
        TableColumn<Cost, String> typeCol = new TableColumn("Type");
        typeCol.setPrefWidth(100);
        typeCol.setCellValueFactory(
                new PropertyValueFactory<>("type"));
        typeCol.setCellFactory((TableColumn<Cost, String> p) -> {
                 return new ChargeTypeComboBox();
            }
        );
        typeCol.setOnEditCommit((TableColumn.CellEditEvent<Cost, String> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setType(t.getNewValue());
            }
        );
        
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
        
        TableColumn<Cost, Number> valueCol = new TableColumn("Value");
        valueCol.setPrefWidth(100);
        valueCol.setCellValueFactory(
                new PropertyValueFactory<>("value"));
        valueCol.setCellFactory(TextFieldTableCell.<Cost, Number>forTableColumn(new NumberStringConverter()));
        valueCol.setOnEditCommit((TableColumn.CellEditEvent<Cost, Number> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setValue(t.getNewValue().doubleValue());
            }
        );
        
        tbl_discount.getColumns().addAll(typeCol, nameCol, valueCol);
        tbl_discount.setEditable(true);    
    }
    
    
    protected void loadTypeData() {
        cmb_type.getItems().add(Utility.TYPE_PERCENTAGE);
        cmb_type.getItems().add(Utility.TYPE_VALUE);
    }
    
     protected boolean validateAdd() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in adding cost");
        
        if (cmb_type.getValue() == null) {
            alert.setContentText("Please enter the type of the cost");
            alert.showAndWait();
            return false ;
        }
        
        if (txt_name.getText().length() <= 0) {
            alert.setContentText("Please enter the name of the cost");
            alert.showAndWait();
            return false ;
        }
        
        if (ntxt_value.getValue() <= Utility.DOUBLE_TOLERANCE) {
            alert.setContentText("Please enter the value of the cost");
            alert.showAndWait();
            return false ;
        }
        
         for (Cost charge : tbl_discount.getItems()) {
             if(cmb_type.getValue().equals(charge.getType()) && 
                     txt_name.getText().equals(charge.getName())) {
                  alert.setContentText("There is already cost with the same type and name");
                  alert.showAndWait();
                  return false ;
             }
         }
        return true;
    }
     
    protected void disableEditing(boolean _disable) {
        txt_name.setDisable(_disable);
        txt_name.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        ntxt_value.setDisable(_disable);
        ntxt_value.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_type.setDisable(_disable);
        cmb_type.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        tbl_discount.setEditable(!_disable);
        btn_add.setVisible(!_disable);
        area_bottom.setViewMode(_disable);
    }
    
    protected void clearInputs() {
        txt_name.clear();
        ntxt_value.clear();
        cmb_type.setValue(null);
    }
    
    protected void buildDiscount(Cost _discount) {
        _discount.setType((String) cmb_type.getValue());
        _discount.setName(txt_name.getText());
        _discount.setValue(ntxt_value.getValue());
    }
}
