/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.vendor_payment.form;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
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
import suzane00.global.custom_ui.StandardSmallDialogBottomArea;
import suzane00.inventory.Item;
import suzane00.purchase.vendor_payment.VendorPayment;
import suzane00.purchase.vendor_payment.view.ViewVendorPayment;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
public class FormPickVendorPayment extends Stage implements Observable{
    
    protected ObservableList<VendorPayment> v_vendorPayments ;
    protected int v_returnStatus = FormConstant.RETURN_CANCEL;
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    protected ViewVendorPayment viewVendorPayment;
    protected StandardSmallDialogBottomArea area_bottom;
   
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormPickVendorPayment() {
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
    
    public ObservableList<VendorPayment> getSelectedItems() {
        return v_vendorPayments;
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
            observer.update(this, viewVendorPayment.getSelectedItems());
        }
    }
    
    protected void initComponents() {
       // v_returnStatus = FormConstant.RETURN_OK;
    }
    
    protected void setAppearance() {
        setMainArea();
        setTitle("Pick Vendor Payments");
    }
    
    protected void setActionPerformed() {
        area_bottom.setButtonOkActionPerformed(e -> {
                    v_returnStatus = FormConstant.RETURN_OK;
                    v_vendorPayments = viewVendorPayment.getSelectedItems();
                    notifyObservers();
                    close();
                }
        
        );
        area_bottom.setButtonCancelActionPerformed(e -> {
                    v_returnStatus = FormConstant.RETURN_CANCEL;
                    v_vendorPayments = null;
                    close();
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */

    /* root area */
    
    protected void setMainArea() {
        viewVendorPayment = new ViewVendorPayment();
        area_bottom = new StandardSmallDialogBottomArea();
        VBox.setVgrow(viewVendorPayment, Priority.ALWAYS);
        area_main = new VBox(viewVendorPayment, area_bottom);
        area_main.setPrefSize(Utility.SHORT_FORM_WIDTH, Utility.STANDARD_FORM_HEIGHT);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_PURCHASE));
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_PURCHASE));
    }
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
}
