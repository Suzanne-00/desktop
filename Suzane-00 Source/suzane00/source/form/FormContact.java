/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source.form;

import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import suzane00.global.Environment;
import suzane00.global.FormConstant;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardFormBottomArea;
import suzane00.source.Address;
import suzane00.source.Source;
import suzane00.source.Supplier;
import suzane00.source.SupplierGroup;

/**
 *
 * @author Usere
 */
public abstract class FormContact extends Stage {
    
    Source v_source ;
    ObservableList<Address> v_addresses;
    int v_mode = FormConstant.MODE_NEW;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    
    /* root area */
    
    VBox area_main;
    
    /* level 1 area */
    
    GridPane area_content;
    StandardFormBottomArea area_bottom;
    
    /* level 2 area */
    
    // area_content
    protected Label lbl_addresses ;
    protected ListView<Address> list_addresses ;
    protected Button btn_addAddress ;
    protected Label lbl_name ;
    protected Label lbl_note ;
    protected TextField txt_name ;
    protected TextArea txa_note ;
    
   
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormContact() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed() ;
        Scene scene = new Scene(area_main);
        this.setScene(scene);
    }
    
    public FormContact(Source _source, int _mode) {
        this();
        setVariables(_source, _mode);
        disiplayData();
        if (v_mode == FormConstant.MODE_VIEW) {
            setViewActionPerformed();
            disableEditing(true);
        }
    }
    
    protected void initComponents() {
        v_source = null;
        v_addresses = null;
        v_mode = FormConstant.MODE_NEW;
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void disiplayData() {
        txt_name.setText(v_source.getName());
        txa_note.setText(v_source.getNote());
        list_addresses.getItems().addAll(v_source.getAddresses());
    }
    
    protected void setActionPerformed() {
        area_bottom.setButtonCloseActionPerformed(e -> close());
        area_bottom.setButtonNewActionPerformed(e -> clearInputs());

        btn_addAddress.setOnAction ( 
               e -> {
                   FormAddAddress form = new FormAddAddress();
                   setTitle("Add Addresses");
                   form.showAndWait();

                   if(form.getReturnStatus() == FormConstant.RETURN_OK)
                       list_addresses.getItems().add(form.getSelectedAddress());
               }
        );

        list_addresses.setOnMouseClicked(
                e -> {
                    if(e.getClickCount() >= 2) {
                        Address addr = list_addresses.getSelectionModel().getSelectedItem();
                        FormAddAddress form = new FormAddAddress(addr, FormConstant.MODE_VIEW);
                        setTitle("View Addresses");
                        form.show();
                    }
                }
        );
    }
    
    protected void setViewActionPerformed() {
        list_addresses.setOnMouseClicked(
                e -> {
                    if(e.getClickCount() >= 2) {
                        Address addr = list_addresses.getSelectionModel().getSelectedItem();
                        FormAddAddress form = new FormAddAddress(addr, FormConstant.MODE_VIEW);
                        form.show();
                    }
                }
        );
    }
    
    protected abstract void setData();
    
    protected void setVariables(Source _source, int _mode) {
        v_source = _source;
        v_mode = _mode;
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setContentArea();
        area_bottom = new StandardFormBottomArea();
        area_main = new VBox(area_content, area_bottom);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setBackground(Source.getMasterBackground());
        area_bottom.setBackground(Source.getMasterBackground());
    }
    
    /* level 1 area */
    
    protected void setContentArea() {
        lbl_addresses = new Label("Addresses: ");
        list_addresses = new ListView<>();
        btn_addAddress = new Button(Utility.STANDARD_EXPAND_ICON); 
        lbl_name = new Label("Name; ");
        lbl_note = new Label("Note: ");
        txt_name = new TextField();
        txa_note = new TextArea();
        area_content = new GridPane();
        
        txt_name.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txa_note.setPrefWidth(Utility.STANDARD_TXA_WIDTH);
        txa_note.setPrefHeight(Utility.STANDARD_TXA_HEIGHT);
        list_addresses.setPrefWidth(Utility.STANDARD_LIST_WIDTH);
        list_addresses.setPrefHeight(Utility.STANDARD_LIST_HEIGHT);
        area_content.setHgap(Utility.SHORT_GAP);
        area_content.setVgap(Utility.SHORT_GAP);
        GridPane.setHalignment(lbl_note, HPos.LEFT);
        GridPane.setValignment(lbl_note, VPos.TOP);     
        GridPane.setColumnSpan(list_addresses, 2);
        
        area_content.addColumn(0, lbl_name);
        area_content.addColumn(1, txt_name);
        area_content.addColumn(0, lbl_note);
        area_content.addColumn(1, txa_note);
        area_content.addColumn(0, lbl_addresses);
        area_content.addColumn(1, btn_addAddress);
        area_content.addColumn(0, list_addresses);
        
//        area_content.add(lbl_name, 0, 0);
//        area_content.add(txt_name, 1, 0);
//        area_content.add(lbl_note, 0, 1);
//        area_content.add(txa_note, 1, 1);
//        area_content.add(lbl_addresses, 0, 2);
//        area_content.add(btn_addAddress, 1, 2);
//        area_content.add(list_addresses, 0, 3); 
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    protected ObservableList<Address> buildAddresses() {
        ObservableList<Address> addresses = FXCollections.observableArrayList();
        for (Address _a : (ObservableList<Address>) list_addresses.getItems()) {
            addresses.add(_a);
        }
        return addresses;
    }
    
    protected void clearInputs() {
        txt_name.clear();
        txa_note.clear();
        list_addresses.getItems().clear();
    }
    
    protected void disableEditing(boolean _disable) {
        lbl_addresses.setVisible(_disable);
        txt_name.setDisable(_disable);
        txt_name.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txa_note.setDisable(_disable);
        txa_note.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white"); 
        btn_addAddress.setVisible(!_disable);
        area_bottom.setViewMode(_disable);
    }
}
