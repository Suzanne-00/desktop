/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.vendor_payment.form;

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
import suzane00.purchase.vendor_payment.VendorPayment;
import suzane00.purchase.vendor_payment.view.ViewVendorPayment;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
public class FormViewVendorPayment extends Stage implements Observable{
    
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    
    protected ViewVendorPayment viewVP;
    protected StandardViewBottomArea area_bottom;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormViewVendorPayment() {
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
            observer.update(this, viewVP.getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        viewVP = new ViewVendorPayment();
        area_bottom = new StandardViewBottomArea();
        VBox.setVgrow(viewVP, Priority.ALWAYS);
        area_main = new VBox(viewVP, area_bottom);
        area_main.setPrefSize(Utility.SHORT_FORM_WIDTH, Utility.LONG_FORM_HEIGHT);
        area_main.setSpacing(Utility.SHORT_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_PURCHASE));
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_PURCHASE));
        setTitle("Vendor Payments List");
    }
    
    protected void setActionPerformed() {
        area_bottom.setButtonViewActionPerformed(e -> {
                    VendorPayment vendorPayment = viewVP.getSelectedItems().get(0);
                    FormVendorPayment form = new FormVendorPayment(vendorPayment, FormConstant.MODE_VIEW);
                    form.setTitle("View Vendor payment");
                    form.show();
                }
        );
        area_bottom.setButtonEditActionPerformed(e -> {
                    VendorPayment vendorPayment = viewVP.getSelectedItems().get(0);
                    FormVendorPayment form = new FormVendorPayment(vendorPayment, FormConstant.MODE_EDIT);
                    form.setTitle("Edit Vendor payment");
                    form.show();
                }
        );
        area_bottom.setButtonDeleteActionPerformed(e -> {
                    Environment.getEntityManager().getTransaction().begin();
                    VendorPayment.deleteVendorPayment(viewVP.getSelectedItems().get(0));
                    Environment.getEntityManager().getTransaction().commit();
                    TableView tbl = viewVP.getTable();
                    tbl.getItems().remove(tbl.getSelectionModel().getSelectedIndex());
                }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());
    }
    
}
