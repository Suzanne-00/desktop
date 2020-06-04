/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source.form;

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
import suzane00.source.Country;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardFormBottomArea;
import suzane00.source.Source;

/**
 *
 * @author Usere
 */
public class FormCountry extends Stage {
    
    Country v_country;
    int v_mode;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    
    /* root area */
    
    VBox area_main;
    
    /* level 1 area */
    
    GridPane area_content;
    StandardFormBottomArea area_bottom;
    
    /* level 2 area */
    
    // area_content
    protected Label lbl_name;
    protected Label lbl_code;
    protected Label lbl_note; 
    protected TextField txt_name;
    protected TextField txt_code;
    protected TextArea txa_note;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    public FormCountry() {
        initComponents();
        setAppearance();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        this.setScene(scene);
    }
    
    public FormCountry(Country _country, int _mode) {
        this();
        setVariables(_country, _mode);
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
    
    protected void setActionPerformed() {
        area_bottom.setButtonSaveActionPerformed(
            e -> {
                if(!validateSave())
                    return;
                if(v_mode == FormConstant.MODE_NEW) {
                    v_country = new Country(txt_name.getText(), txt_code.getText(), txa_note.getText() );
                    Environment.getEntityManager().getTransaction().begin();
                    Country.saveCountry(v_country);
                    Environment.getEntityManager().getTransaction().commit();
                    clearInputs();
                }
                else {
                    Country country = new Country(txt_name.getText(), txt_code.getText(), txa_note.getText() );
                    Environment.getEntityManager().getTransaction().begin();
                    Country.editCountry(v_country, country);
                    Environment.getEntityManager().getTransaction().commit();
                    clearInputs();
                }
            }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());        
        area_bottom.setButtonNewActionPerformed(e -> clearInputs());
    }
    
    protected void setVariables(Country _country, int _mode) {
        v_country = _country ;
        v_mode = _mode;
    } 
    
    protected void disiplayData() {
        txt_name.setText(v_country.getName());
        txt_code.setText(v_country.getCode());
        txa_note.setText(v_country.getNote());
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
    
    /* level1 area */
    
    protected void setContentArea() {
        lbl_name = new Label("Name: ");
        lbl_code = new Label("Code: ");
        lbl_note = new Label("Note: "); 
        txt_name = new TextField();
        txt_code = new TextField();
        txa_note = new TextArea();
        area_content = new GridPane();
        
        txt_name.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_code.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txa_note.setPrefWidth(Utility.STANDARD_TXA_WIDTH);
        txa_note.setPrefHeight(Utility.STANDARD_TXA_HEIGHT);
        area_content.setHgap(Utility.SHORT_GAP);
        area_content.setVgap(Utility.SHORT_GAP);
        GridPane.setHalignment(lbl_note, HPos.LEFT);
        GridPane.setValignment(lbl_note, VPos.TOP);     
        
        area_content.add(lbl_name, 0, 0);
        area_content.add(txt_name, 1, 0);
        area_content.add(lbl_code, 0, 1);
        area_content.add(txt_code, 1, 1);
        area_content.add(lbl_note, 0, 2);
        area_content.add(txa_note, 1, 2);
        
    }
    
   
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
         
    protected boolean validateSave() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving country");
        
        
        if (txt_name.getText().length() <= 0) {
            alert.setContentText("Please enter the name of the country");
            alert.showAndWait();
            return false ;
        }
        
        return true;
    }
    
    protected void disableEditing(boolean _disable) {
        txt_name.setDisable(_disable);
        txt_name.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_code.setDisable(_disable);
        txt_code.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txa_note.setDisable(_disable);
        txa_note.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        area_bottom.setViewMode(_disable);
    }
    
    private void clearInputs() {
        txt_name.clear();
        txt_code.clear();
        txa_note.clear();
    }
}
