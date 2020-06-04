/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order.form;

import java.io.IOException;
import java.io.InputStream;
import suzane00.inventory.form.*;
import java.util.LinkedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import suzane00.global.Environment;
import suzane00.global.FormConstant;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardViewBottomArea;
import suzane00.purchase.purchase_order.view.ViewItemPurchasePrice;
import suzane00.purchase.purchase_order.PurchaseOrder;
import suzane00.purchase.purchase_order.view.ViewPurchaseOrder;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
public class FormViewPurchaseOrder extends Stage implements Observable{
    
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    
    protected ViewPurchaseOrder viewPO;
    protected StandardViewBottomArea area_bottom;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormViewPurchaseOrder() {
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
            observer.update(this, viewPO.getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        viewPO = new ViewPurchaseOrder();
        area_bottom = new StandardViewBottomArea();
        VBox.setVgrow(viewPO, Priority.ALWAYS);
        area_main = new VBox(viewPO, area_bottom);
        area_main.setPrefSize(Utility.SHORT_FORM_WIDTH, Utility.LONG_FORM_HEIGHT);
        area_main.setSpacing(Utility.SHORT_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_PURCHASE));
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_PURCHASE));
        setTitle("Purchase Orders List");
    }
    
    
    protected void setActionPerformed() {
        area_bottom.setButtonViewActionPerformed(
                e -> {
                    PurchaseOrder po = viewPO.getSelectedItems().get(0);
                    FormPurchaseOrder form = new FormPurchaseOrder(po, FormConstant.MODE_VIEW);
                    form.setTitle("View Purchase Order");
                    form.show();
                }
        );
        area_bottom.setButtonEditActionPerformed(
                e -> {
                    PurchaseOrder po = viewPO.getSelectedItems().get(0);
                    FormPurchaseOrder form = new FormPurchaseOrder(po, FormConstant.MODE_EDIT);
                    form.setTitle("Edit Purchase Order");
                    form.show();
                }
        );
        area_bottom.setButtonDeleteActionPerformed(
                e -> {
                    Environment.getEntityManager().getTransaction().begin();
                    PurchaseOrder.deletePurchaseOrder(viewPO.getSelectedItems().get(0));
                    Environment.getEntityManager().getTransaction().commit();
                    TableView tbl = viewPO.getTable();
                    tbl.getItems().remove(tbl.getSelectionModel().getSelectedIndex());
                }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());
    }
}
