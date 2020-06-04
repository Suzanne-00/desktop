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
import suzane00.inventory.Item;
import suzane00.inventory.SellPriceType;
import suzane00.inventory.view.ViewArea;
import suzane00.inventory.view.ViewSellPriceType;

/**
 *
 * @author Usere
 */
public class FormViewSellPriceType extends Stage implements Observable{
    
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    
    protected ViewSellPriceType viewSellPriceType;
    protected StandardViewBottomArea area_bottom;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormViewSellPriceType() {
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
            observer.update(this, viewSellPriceType.getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        viewSellPriceType = new ViewSellPriceType();
        area_bottom = new StandardViewBottomArea();
        area_main = new VBox(viewSellPriceType, area_bottom);
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setBackground(Item.getMasterBackground());
        area_bottom.setBackground(Item.getMasterBackground());
        setTitle("Sell price types List");
    }
    
    protected void setActionPerformed() {        
        
        area_bottom.setButtonViewActionPerformed(e -> {
                    SellPriceType type = viewSellPriceType.getSelectedItems().get(0);
                    FormSellPriceType form = new FormSellPriceType(type, FormConstant.MODE_VIEW);
                    form.setTitle("View Sell Price Type");
                    form.show();
                }
        );
        area_bottom.setButtonEditActionPerformed(e -> {
                    SellPriceType type = viewSellPriceType.getSelectedItems().get(0);
                    FormSellPriceType form = new FormSellPriceType(type, FormConstant.MODE_EDIT);
                    form.setTitle("Edit Sell Price Type");
                    form.show();
                }
        );
        area_bottom.setButtonDeleteActionPerformed(e -> {
                    Environment.getEntityManager().getTransaction().begin();
                    SellPriceType.deletePriceType(viewSellPriceType.getSelectedItems().get(0));
                    Environment.getEntityManager().getTransaction().commit();
                    TableView tbl = viewSellPriceType.getTable();
                    tbl.getItems().remove(tbl.getSelectionModel().getSelectedIndex());
                }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());
    }
}
