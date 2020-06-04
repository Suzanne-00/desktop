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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import suzane00.global.Environment;
import suzane00.global.FormConstant;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardFormBottomArea;
import suzane00.source.Address;
import suzane00.source.Customer;
import suzane00.source.CustomerGroup;
import suzane00.source.Source;
import suzane00.source.Supplier;
import suzane00.source.SupplierGroup;

/**
 *
 * @author Usere
 */
public class FormCustomer extends FormContact {
    
    ObservableList<Address> v_addresses;
    Customer v_customer;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    
    /* root area */
    
    // ! VBox area_main;
    
    /* level 1 area */
    
    // ! GridPane area_content;
    // ! StandardFormBottomArea area_bottom;
    
    /* level 2 area */
    
    // area_content
    // ! Label lbl_addresses = new Label("Addresses: ");
    // ! ListView<Address> list_addresses = new ListView<>();
    // ! Button btn_addAddress = new Button(Utility.STANDARD_EXPAND_ICON); 
    // ! Label lbl_name = new Label("Name; ");
    protected Label lbl_group ;
    // ! Label lbl_note = new Label("Note: ");
    // 1 TextField txt_name = new TextField();
    protected ComboBox cmb_group ;
    // ! TextArea txa_note = new TextArea();
    
   
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
 
    
    public FormCustomer() {
        super();
    }
    
    public FormCustomer(Customer _cust, int _mode) {
        super(_cust, _mode);
    }
    
    @Override
    protected void initComponents() {
        super.initComponents();
    }
    
    @Override
    protected void setActionPerformed() {
        super.setActionPerformed();
        area_bottom.setButtonSaveActionPerformed(e -> {
                if(!validateSave())
                    return;
                if(v_mode == FormConstant.MODE_NEW) {
                    v_customer = buildCustomer();
                    Environment.getEntityManager().getTransaction().begin();
                    Customer.saveCustomer(v_customer);
                    Environment.getEntityManager().getTransaction().commit();
                    clearInputs();
                }
                else {
                    Customer customer = buildCustomer();
                    Environment.getEntityManager().getTransaction().begin();
                    Customer.editCustomer(v_customer, customer);
                    Environment.getEntityManager().getTransaction().commit();
                    clearInputs();
                }
            }
        );
    }
    
    @Override
    protected void setData() {
        cmb_group.setItems(CustomerGroup.getAllGroups());
    }
    
    @Override
    protected void disiplayData() {
        super.disiplayData();
        cmb_group.setValue(v_customer.getGroup());
    }
    
    
    @Override
    protected void setVariables(Source _source, int _mode) {
        super.setVariables(_source, _mode);
        v_customer = (Customer) _source;
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
   
    /* level 1 area */
    
    @Override
    protected void setContentArea() {
        super.setContentArea();
        lbl_group = new Label("Group: ");
        cmb_group = new ComboBox();
        area_content.getChildren().clear();
        area_content.addColumn(0, lbl_name);
        area_content.addColumn(1, txt_name);
        area_content.addColumn(0, lbl_group);
        area_content.addColumn(1, cmb_group);
        area_content.addColumn(0, lbl_note);
        area_content.addColumn(1, txa_note);
        area_content.addColumn(0, lbl_addresses);
        area_content.addColumn(1, btn_addAddress);
        area_content.addColumn(0, list_addresses);
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    
    
    protected Customer buildCustomer() {
        Customer customer = new Customer();
        ObservableList<Address> addresses = buildAddresses();
        customer.setName(txt_name.getText());
        customer.setNote(txa_note.getText());
        customer.setGroup((CustomerGroup)cmb_group.getValue());
        customer.setAddresses(addresses);
        for (Address _address : addresses) {
           _address.setSource(customer);
        } 
        
        return customer;
    }
    
    protected boolean validateSave() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving customer");
        
        
        if (txt_name.getText().length() <= 0) {
            alert.setContentText("Please enter the name of the customer");
            alert.showAndWait();
            return false ;
        }
        
        return true;
    }
    
    @Override
    protected void disableEditing(boolean _disable) {
        super.disableEditing(_disable);
        cmb_group.setDisable(_disable);
        cmb_group.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white"); 
    }
    
    protected void clearInputs() {
        super.clearInputs();
        cmb_group.setValue(null);
    }
}
