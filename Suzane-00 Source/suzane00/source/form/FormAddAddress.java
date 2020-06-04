/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source.form;

import java.util.LinkedList;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import suzane00.global.Environment;
import suzane00.global.FormConstant;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardDialogBottomArea;
import suzane00.global.custom_ui.StandardSmallDialogBottomArea;
import suzane00.source.Address;
import suzane00.source.City;
import suzane00.source.Country;
import suzane00.source.Source;


/**
 *
 * @author Usere
 */
public class FormAddAddress extends Stage implements suzane00.global.Observable{
    
    protected Address v_address;
    protected int v_mode;
    private int v_returnStatus;
    protected LinkedList<Observer> observers ;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    protected GridPane area_content; 
    protected StandardDialogBottomArea area_bottom;
    
    /* level 2 area */
    
    // area_content
    protected Label lbl_country;
    protected Label lbl_city;
    protected Label lbl_street;
    protected Label lbl_phoneNumber;
    protected Label lbl_altPhoneNumber;
    protected Label lbl_email;
    protected Label lbl_altEmail;
    protected Label lbl_postalCode;
    protected Label lbl_faxNumber;    
    protected TextField txt_street;
    protected TextField txt_phoneNumber;
    protected TextField txt_altPhoneNumber;
    protected TextField txt_email;
    protected TextField txt_altEmail;
    protected TextField txt_postalCode;
    protected TextField txt_faxNumber;
    protected ComboBox<Country> cmb_country;
    protected ComboBox cmb_city;
   
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormAddAddress() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        this.setScene(scene);
    }
    
    public FormAddAddress(Address _addr, int _mode) {
        this();
        setVariables(_addr, _mode);
        disiplayData();
        if (v_mode == FormConstant.MODE_VIEW)
            disableEditing(true);
    }
    
    public int getReturnStatus() {
        return v_returnStatus;
    }
    
    public Address getSelectedAddress() {
        return v_address;
    }
    
    @Override
    public void addObserver(Observer _o) {
        if( observers == null)
            observers = new LinkedList<>();
        
        observers.add(_o);
    }

    @Override
    public void notifyObservers() {
        
        Address addr = buildAddress();
        
        if (observers == null)
            return;
        
        for (Observer o : observers) {
            o.update(this, addr);
            //((Stage)o).show();;
        }
    }
    
    protected void initComponents() {
        v_mode = FormConstant.MODE_NEW;
    }
    
    protected void setAppearance() {
        setMainArea(); 
    }
    
    protected void setData() {
        loadCountryData();
    }
    
    
    protected void setActionPerformed() {
        cmb_country.valueProperty().addListener(
            (ObservableValue<? extends Country> observable, Country oldValue, Country newValue) -> {
                cmb_city.setItems(City.getCitiesBasedOnCountry(newValue));
            }
        );
        
        area_bottom.setButtonOkActionPerformed(
                e -> {
                    if(!validateOk())
                        return ;
                    
                    v_returnStatus = FormConstant.RETURN_OK;
                    v_address = buildAddress();
                    notifyObservers();
                    this.close();
                }
        );
        area_bottom.setButtonCancelActionPerformed(e -> this.close());
        area_bottom.setButtonNewActionPerformed(e -> clearInputs());
    }
    
    protected void setVariables(Address _addr, int _mode) {
        v_address = _addr ;
        v_mode = _mode;
    }
    
    
    protected void disiplayData() {
        txt_street.setText(v_address.getStreetName());
        txt_phoneNumber.setText(v_address.getPhoneNumber());
        txt_altPhoneNumber.setText(v_address.getAltPhoneNumber());
        txt_email.setText(v_address.getEmail());
        txt_altEmail.setText(v_address.getAltEmail());
        txt_faxNumber.setText(v_address.getFaxNumber());
        txt_postalCode.setText(v_address.getPostalCode());
        cmb_city.setValue(v_address.getCity());
        cmb_country.setValue(((City)cmb_city.getValue()).getCountry());
    }
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setContentArea();
        
        area_bottom = new StandardDialogBottomArea();
        area_main = new VBox(area_content, area_bottom);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setBackground(Source.getMasterBackground());
        area_bottom.setBackground(Source.getMasterBackground());
    }
    
    /* level 1 area */
    
    protected void setContentArea() {
        v_returnStatus = FormConstant.RETURN_OK;
        lbl_country = new Label("Country: ");
        lbl_city = new Label("City: ");
        lbl_street = new Label("Street: ");
        lbl_phoneNumber = new Label("Phone: ");
        lbl_altPhoneNumber = new Label("Phone(alt): ");
        lbl_email = new Label("Email: ");
        lbl_altEmail = new Label("Email(alt): ");
        lbl_postalCode = new Label("Postal code: ");
        lbl_faxNumber = new Label("Fax number: ");    
        txt_street = new TextField();
        txt_phoneNumber = new TextField();
        txt_altPhoneNumber = new TextField();
        txt_email = new TextField();
        txt_altEmail = new TextField();
        txt_postalCode = new TextField();
        txt_faxNumber = new TextField();
        cmb_country = new ComboBox();
        cmb_city = new ComboBox();
        area_content = new GridPane();
        
        txt_street.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_phoneNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_altPhoneNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_altEmail.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_email.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_postalCode.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_faxNumber.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_country.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_city.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_content.setHgap(Utility.SHORT_GAP);
        area_content.setVgap(Utility.SHORT_GAP);
        
        area_content.add(lbl_country, 0, 0);
        area_content.add(cmb_country, 1, 0);
        area_content.add(lbl_city, 0, 1);
        area_content.add(cmb_city, 1, 1);
        area_content.add(lbl_street, 0, 2);
        area_content.add(txt_street, 1, 2);
        area_content.add(lbl_phoneNumber, 0, 3);
        area_content.add(txt_phoneNumber, 1, 3);
        area_content.add(lbl_altPhoneNumber, 0, 4);
        area_content.add(txt_altPhoneNumber, 1, 4);
        area_content.add(lbl_email, 0, 5);
        area_content.add(txt_email, 1, 5);
        area_content.add(lbl_altEmail, 0, 6);
        area_content.add(txt_altEmail, 1, 6);
        area_content.add(lbl_postalCode, 0, 7);
        area_content.add(txt_postalCode, 1, 7);
        area_content.add(lbl_faxNumber, 0, 8);
        area_content.add(txt_faxNumber, 1, 8);
    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */

   
    
    
    protected void loadCountryData() {
        cmb_country.setItems(Country.getAllCountries());
    }
    
    protected Address buildAddress() {
        Address address = new Address();
        address.setStreetName(txt_street.getText());
        address.setPhoneNumber(txt_phoneNumber.getText());
        address.setAltPhoneNumber(txt_altPhoneNumber.getText());
        address.setEmail(txt_email.getText());
        address.setAltEmail(txt_altEmail.getText());
        address.setPostalCode(txt_postalCode.getText());
        address.setFaxNumber(txt_faxNumber.getText());
        address.setCity((City)cmb_city.getValue());
        
        return address;
    }
    
    protected boolean validateOk() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in adding address");
        
        if (cmb_country.getValue() == null) {
            alert.setContentText("Please enter the country of the address");
            alert.showAndWait();
            return false ;
        }
        
        if (cmb_city.getValue() == null) {
            alert.setContentText("Please enter the city of the address");
            alert.showAndWait();
            return false ;
        }
        
        if (txt_street.getText().length() <= 0) {
            alert.setContentText("Please enter the name of the street");
            alert.showAndWait();
            return false ;
        }
       
        return true;
    }
    
     protected void disableEditing(boolean _disable) {
        txt_street.setDisable(_disable);
        txt_street.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_phoneNumber.setDisable(_disable);
        txt_phoneNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_altPhoneNumber.setDisable(_disable);
        txt_altPhoneNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_email.setDisable(_disable);
        txt_email.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_altEmail.setDisable(_disable);
        txt_altEmail.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_faxNumber.setDisable(_disable);
        txt_faxNumber.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_postalCode.setDisable(_disable);
        txt_postalCode.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_country.setDisable(_disable);
        cmb_country.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_city.setDisable(_disable);
        cmb_city.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        area_bottom.setViewMode(_disable);
    }
     
      protected void clearInputs() {
        txt_street.clear();
        txt_phoneNumber.clear();
        txt_altPhoneNumber.clear();
        txt_email.clear();
        txt_altEmail.clear();
        txt_faxNumber.clear();
        txt_postalCode.clear();
        cmb_country.setValue(null);
        cmb_city.setValue(null);
    }
}
