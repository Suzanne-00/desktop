/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.form;

import suzane00.inventory.form.*;
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
import suzane00.inventory.Area;
import suzane00.inventory.Brand;
import suzane00.inventory.Item;
import suzane00.inventory.view.ViewArea;
import suzane00.inventory.view.ViewBrand;

/**
 *
 * @author Usere
 */
public class FormViewBrand extends Stage implements Observable{
    
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    
    protected ViewBrand viewBrand;
    protected StandardViewBottomArea area_bottom;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormViewBrand() {
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
            observer.update(this, viewBrand.getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        viewBrand = new ViewBrand();
        area_bottom = new StandardViewBottomArea();
        area_main = new VBox(viewBrand, area_bottom);
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setBackground(Item.getMasterBackground());
        area_bottom.setBackground(Item.getMasterBackground());
        setTitle("Brands List");
    }
    
    protected void setActionPerformed() {        
        
        area_bottom.setButtonViewActionPerformed(e -> {
                    Brand brand = viewBrand.getSelectedItems().get(0);
                    FormBrand form = new FormBrand(brand, FormConstant.MODE_VIEW);
                    form.setTitle("View Brand");
                    form.show();
                }
        );
        area_bottom.setButtonEditActionPerformed(e -> {
                    Brand brand = viewBrand.getSelectedItems().get(0);
                    FormBrand form = new FormBrand(brand, FormConstant.MODE_EDIT);
                    form.setTitle("Edit Brand");
                    form.show();
                }
        );
        area_bottom.setButtonDeleteActionPerformed(e -> {
                    Environment.getEntityManager().getTransaction().begin();
                    Brand.deleteBrand(viewBrand.getSelectedItems().get(0));
                    Environment.getEntityManager().getTransaction().commit();
                    TableView tbl = viewBrand.getTable();
                    tbl.getItems().remove(tbl.getSelectionModel().getSelectedIndex());
                }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());
    }
}
