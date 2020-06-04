/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source.form;

import java.util.LinkedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import suzane00.global.Environment;
import suzane00.global.FormConstant;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardViewBottomArea;
import suzane00.source.City;
import suzane00.source.Country;
import suzane00.source.Source;
import suzane00.source.view.ViewCity;
import suzane00.source.view.ViewCountry;

/**
 *
 * @author Usere
 */
public class FormViewCountry extends Stage implements Observable{
    
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    
    protected ViewCountry viewCountry = new ViewCountry();
    protected StandardViewBottomArea area_bottom = new StandardViewBottomArea();
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormViewCountry() {
        initComponents();
        setAppearance();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        setScene(scene);
        show();
    }
    
    @Override
    public void addObserver(Observer _o) {
        if( v_observers == null)
            v_observers = new LinkedList<>();
        
        v_observers.add(_o);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : v_observers) {
            observer.update(this, viewCountry.getSelectedItems());
        }
    }
    
    protected void initComponents() {
        
    }
    
    protected void setAppearance() {
        viewCountry = new ViewCountry();
        area_bottom = new StandardViewBottomArea();
        area_main = new VBox(viewCountry, area_bottom);
        area_main.setSpacing(Utility.SHORT_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setBackground(Source.getMasterBackground());
        area_bottom.setBackground(Source.getMasterBackground());
        setTitle("Countries List");
    }
    
    protected void setActionPerformed() {        
        
        area_bottom.setButtonViewActionPerformed(e -> {
                    Country country = viewCountry.getSelectedItems().get(0);
                    FormCountry form = new FormCountry(country, FormConstant.MODE_VIEW);
                    form.setTitle("View Country");
                    form.show();
                }
        );
        area_bottom.setButtonEditActionPerformed(e -> {
                    Country country = viewCountry.getSelectedItems().get(0);
                    FormCountry form = new FormCountry(country, FormConstant.MODE_EDIT);
                    form.setTitle("Edit Country");
                    form.show();
                }
        );
        area_bottom.setButtonDeleteActionPerformed(e -> {
                    Environment.getEntityManager().getTransaction().begin();
                    Country.deleteCountry(viewCountry.getSelectedItems().get(0));
                    Environment.getEntityManager().getTransaction().commit();
                    TableView tbl = viewCountry.getTable();
                    tbl.getItems().remove(tbl.getSelectionModel().getSelectedIndex());
                }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());
    }
}
