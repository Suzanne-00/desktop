/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source.view;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import java.util.LinkedList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardViewHeaderArea;
import suzane00.source.City;
import suzane00.source.Country;
import suzane00.source.Supplier;
import suzane00.source.SupplierGroup;

/**
 *
 * @author Usere
 */
public class ViewCity extends VBox implements Observable {

    protected LinkedList<Observer> m_observers;
    
    /*----------------------------------------------- GUI COMPONENTS ---------------------------------------------- */

    /* level 1 area */
    
    protected StandardViewHeaderArea area_header;
    protected TableView<City> tbl_city;
    
    /*level 2 area */
    
    // area_header
    protected GridPane area_upperHeader;
    
    /* level 3 area */
    
    // area_header -> area_upperHeader
    protected Label lbl_name;
    protected Label lbl_country;
    protected Label lbl_note;
    protected TextField txt_name;
    protected ComboBox cmb_country;
    protected TextField txt_note;
  
    
    /*----------------------------------------------- END GUI COMPONENTS ---------------------------------------------- */
    
    
    public ViewCity() {
        setAppearance();
        setData();
        setActionPerformed();
    }
    
    public ObservableList<City> getSelectedItems() {
        return tbl_city.getSelectionModel().getSelectedItems();
    }
    
    public TableView getTable() {
        return tbl_city;
    }
    
    @Override
    public void addObserver(Observer _o) {
        if( m_observers == null)
            m_observers = new LinkedList<>();
        
        m_observers.add(_o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : m_observers) {
            observer.update(this, tbl_city.getSelectionModel().getSelectedItem());
        }
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        configureItemTable();
        loadSupplierGroupData();
    }
    
    protected void setActionPerformed() {
        area_header.setButtonSearchActionPerformed(e -> {
                    String name = txt_name.getText();
                    String note = txt_note.getText();
                    Country country = (Country) cmb_country.getValue();
                    
                    tbl_city.setItems(FXCollections.observableArrayList(City.getCitiesByAttributes(name, country, note)));
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setHeaderArea();
        tbl_city = new TableView<City>();
        tbl_city.setPrefWidth(Utility.STANDARD_TABLE_WIDTH);
        tbl_city.setPrefHeight(Utility.STANDARD_TABLE_HEIGHT);
        setSpacing(Utility.STANDARD_GAP);
        getChildren().addAll(area_header, tbl_city);
    }
    
    /* level 1 area */
    
    protected void setHeaderArea() {
        setUpperHeaderArea();
        area_header = new StandardViewHeaderArea();
        area_header.setContent(area_upperHeader);
    }
    
    /* level 2 area */
    
    // setHeaderArea()
    protected void setUpperHeaderArea() {
        lbl_name = new Label("Name: ");
        lbl_country = new Label("Group: ");
        lbl_note = new Label("Note: ");
        txt_name = new TextField();
        cmb_country = new ComboBox();
        txt_note = new TextField();
        area_upperHeader = new GridPane();
        
        txt_name.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_note.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        cmb_country.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_upperHeader.setHgap(Utility.SHORT_GAP);
        area_upperHeader.setVgap(Utility.SHORT_GAP);
        
        area_upperHeader.add(lbl_name, 0, 0);
        area_upperHeader.add(txt_name, 1, 0);
        area_upperHeader.add(lbl_country, 0, 1);
        area_upperHeader.add(cmb_country, 1, 1);
        area_upperHeader.add(lbl_note, 0, 2);
        area_upperHeader.add(txt_note, 1, 2);
    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */

    
    protected void configureItemTable() {
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        
        TableColumn countryCol = new TableColumn("Country");
        countryCol.setMinWidth(100);
        countryCol.setCellValueFactory(
                new PropertyValueFactory<>("country"));
 
        TableColumn noteCol = new TableColumn("Note");
        noteCol.setMinWidth(100);
        noteCol.setCellValueFactory(
                new PropertyValueFactory<>("note"));
 
        tbl_city.getColumns().addAll(nameCol, countryCol, noteCol);
    }
    
    protected void loadSupplierGroupData() {
        
    }
}
