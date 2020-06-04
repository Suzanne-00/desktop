/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source.form;

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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import suzane00.global.Environment;
import suzane00.global.FormConstant;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardFormBottomArea;
import suzane00.source.City;
import suzane00.source.Country;
import suzane00.source.Source;

/**
 *
 * @author Usere
 */
public class FormCity extends Stage {
    
    protected City v_city;
    protected int v_mode = FormConstant.MODE_NEW;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    protected VBox area_main;
    
    /* level 1 area */
    
    protected GridPane area_content;
    protected StandardFormBottomArea area_bottom;
    
    /* level 2 area */
    
    // area_content
    protected Label lbl_country ;
    protected Label lbl_name ;
    protected Label lbl_note ;
    protected TextField txt_name ;
    protected ComboBox cmb_country ;
    protected TextArea txa_note ;
    
  
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormCity() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        this.setScene(scene);
    }
    
    public FormCity(City _city, int _mode) {
        this();
        setVariables(_city, _mode);
        disiplayData();
        if (v_mode == FormConstant.MODE_VIEW)
            disableEditing(true);
    }
    
    protected void initComponents() {
        v_mode = FormConstant.MODE_NEW;   
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        ObservableList countries = FXCollections.observableArrayList(Country.getAllCountries());
        cmb_country.setItems(countries);
    }
    
    protected void setActionPerformed() {
        
        area_bottom.setButtonSaveActionPerformed(
            e -> {
                if(!validateSave())
                    return;
                if(v_mode == FormConstant.MODE_NEW ) {
                    v_city = new City(((Country)cmb_country.getValue()), txt_name.getText(), txa_note.getText()) ;
                    Environment.getEntityManager().getTransaction().begin();
                    City.saveCity(v_city);
                    Environment.getEntityManager().getTransaction().commit();
                    clearInputs();
                }
                else {
                    City city = new City(((Country)cmb_country.getValue()), txt_name.getText(), txa_note.getText()) ;
                    Environment.getEntityManager().getTransaction().begin();
                    City.editCity(v_city, city);
                    Environment.getEntityManager().getTransaction().commit();
                    clearInputs();
                }
            }
        );
        
        area_bottom.setButtonCloseActionPerformed(e -> close());
        area_bottom.setButtonNewActionPerformed(e -> clearInputs());
    }
    
    protected void setVariables(City _city, int _mode) {
        v_city = _city ;
        v_mode = _mode;
    }
    
    protected void disiplayData() {
        txt_name.setText(v_city.getName());
        cmb_country.setValue(v_city.getCountry());
        txa_note.setText(v_city.getNote());
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
        area_content = new GridPane();
        lbl_country = new Label("Country: ");
        lbl_name = new Label("Name: ");
        lbl_note = new Label("Note: "); 
        txt_name = new TextField();
        cmb_country = new ComboBox();
        txa_note = new TextArea();
        
        txt_name.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_country.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txa_note.setPrefWidth(Utility.STANDARD_TXA_WIDTH);
        txa_note.setPrefHeight(Utility.STANDARD_TXA_HEIGHT);
        area_content.setHgap(Utility.SHORT_GAP);
        area_content.setVgap(Utility.SHORT_GAP);
        GridPane.setHalignment(lbl_note, HPos.LEFT);
        GridPane.setValignment(lbl_note, VPos.TOP);     
        
        area_content.add(lbl_country, 0, 0);
        area_content.add(cmb_country, 1, 0);
        area_content.add(lbl_name, 0, 1);
        area_content.add(txt_name, 1, 1);
        area_content.add(lbl_note, 0, 2);
        area_content.add(txa_note, 1, 2);
        
    }
  
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
     
    protected boolean validateSave() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving city");
        
        if (cmb_country.getValue() == null) {
            alert.setContentText("Please pick the country for the city");
            alert.showAndWait();
            return false ;
        }
        
        if (txt_name.getText().length() <= 0) {
            alert.setContentText("Please enter the name of the city");
            alert.showAndWait();
            return false ;
        }
        
        return true;
    }
    
    protected void disableEditing(boolean _disable) {
        txt_name.setDisable(_disable);
        txt_name.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_country.setDisable(_disable);
        cmb_country.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txa_note.setDisable(_disable);
        txa_note.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        area_bottom.setViewMode(_disable);
    }
    
    protected void clearInputs() {
        txt_name.clear();
        txa_note.clear();
        cmb_country.setValue(null);
    }
}
