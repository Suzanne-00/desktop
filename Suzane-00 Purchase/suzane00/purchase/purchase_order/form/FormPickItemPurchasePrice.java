/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order.form;

import suzane00.inventory.form.*;
import java.util.LinkedList;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
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
import suzane00.global.custom_ui.StandardDialogBottomArea;
import suzane00.global.custom_ui.StandardSmallDialogBottomArea;
import suzane00.inventory.Item;
import suzane00.purchase.purchase_order.view.ViewItemPurchasePrice;
import suzane00.purchase.purchase_order.ItemPurchasePrice;

/**
 *
 * @author Usere
 */
public class FormPickItemPurchasePrice extends Stage implements Observable{
    
    protected ObservableList<ItemPurchasePrice> v_itemPrices;
    protected int v_returnStatus;
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    protected ViewItemPurchasePrice viewItemPurchasePrice;
    protected StandardSmallDialogBottomArea area_bottom;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormPickItemPurchasePrice() {
        initComponents();
        setAppearance();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        setScene(scene);
        //show();
    }
    
    public int getReturnStatus() {
        return v_returnStatus;
    }
    
    public ObservableList<ItemPurchasePrice> getSelectedPrices() {
        return v_itemPrices;
    }
    
    @Override
    public void addObserver(Observer _o) {
        if( v_observers == null)
            v_observers = new LinkedList<>();
        
        v_observers.add(_o);
    }

    @Override
    public void notifyObservers() {
        if(v_observers == null)
            return;
        for (Observer observer : v_observers) {
            observer.update(this, viewItemPurchasePrice.getSelectedItems());
        }
    }
    
    protected void initComponents() {
        v_returnStatus = FormConstant.RETURN_OK;
    }
    
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setActionPerformed() {
        area_bottom.setButtonOkActionPerformed(e -> {
                    v_returnStatus = FormConstant.RETURN_OK;
                    v_itemPrices = viewItemPurchasePrice.getSelectedItems();
                    notifyObservers();
                    close();
                }
        
        );
        
        area_bottom.setButtonCancelActionPerformed(e -> {
                    v_returnStatus = FormConstant.RETURN_CANCEL;
                    close();
                }
                            
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */

    /* root area */
    
    protected void setMainArea() {
        viewItemPurchasePrice = new ViewItemPurchasePrice();
        area_bottom = new StandardDialogBottomArea();
        area_main = new VBox(viewItemPurchasePrice, area_bottom);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setSpacing(Utility.STANDARD_GAP);
    }
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
}
