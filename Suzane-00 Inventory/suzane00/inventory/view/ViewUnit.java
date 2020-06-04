/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.view;

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
import suzane00.inventory.Area;
import suzane00.inventory.Brand;
import suzane00.inventory.Item;
import suzane00.inventory.ProductType;
import suzane00.inventory.Unit;

/**
 *
 * @author Usere
 */
public class ViewUnit extends VBox implements Observable {

    protected LinkedList<Observer> m_observers;
    
    /*----------------------------------------------- GUI COMPONENTS ---------------------------------------------- */

    /* level 1 area */
    
    protected StandardViewHeaderArea area_header;
    protected TableView<Unit> tbl_unit;
    
    /*level 2 area */
    
    // area_header
    protected GridPane area_upperHeader;
    
    /* level 3 area */
    
    // area_header -> area_upperHeader
    protected Label lbl_name = new Label("Name: ");
    protected Label lbl_note = new Label("Note: ");
    protected TextField txt_name = new TextField();
    protected TextField txt_note = new TextField();
  
    
    /*----------------------------------------------- END GUI COMPONENTS ---------------------------------------------- */
    
    
    public ViewUnit() {
        setAppearance();
        setData();
        setActionPerformed();
    }
    
    public ObservableList<Unit> getSelectedItems() {
        return tbl_unit.getSelectionModel().getSelectedItems();
    }
    
    public TableView getTable() {
        return tbl_unit;
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
            observer.update(this, tbl_unit.getSelectionModel().getSelectedItem());
        }
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        configureItemTable();
    }
    
    protected void setActionPerformed() {
        area_header.setButtonSearchActionPerformed(e -> {
                    String name = txt_name.getText();
                    String note = txt_note.getText();
                    
                    tbl_unit.setItems(FXCollections.observableArrayList(Unit.getUnitsByAttributes(name, note)));
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setHeaderArea();
        tbl_unit = new TableView<>();
        tbl_unit.setPrefWidth(Utility.STANDARD_TABLE_WIDTH);
        tbl_unit.setPrefHeight(Utility.STANDARD_TABLE_HEIGHT);
        setSpacing(Utility.STANDARD_GAP);
        getChildren().addAll(area_header, tbl_unit);
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
        lbl_note = new Label("Note: ");
        txt_name = new TextField();
        txt_note = new TextField();
        area_upperHeader = new GridPane();
        
        txt_name.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        txt_note.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
        area_upperHeader.setHgap(Utility.SHORT_GAP);
        area_upperHeader.setVgap(Utility.SHORT_GAP);
        
        area_upperHeader.add(lbl_name, 0, 0);
        area_upperHeader.add(txt_name, 1, 0);
        area_upperHeader.add(lbl_note, 0, 1);
        area_upperHeader.add(txt_note, 1, 1);
    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */

    
    protected void configureItemTable() {
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
 
        TableColumn noteCol = new TableColumn("Note");
        noteCol.setMinWidth(100);
        noteCol.setCellValueFactory(
                new PropertyValueFactory<>("note"));
 
        tbl_unit.getColumns().addAll(nameCol, noteCol);
    }
    
   
}
