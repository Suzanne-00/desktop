/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_payment.form;

import suzane00.sale.sales_retur.form.*;
import suzane00.sale.delivery_order.form.*;
import suzane00.sale.stock_reservation.form.*;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import suzane00.global.Environment;
import suzane00.global.FormConstant;
import suzane00.global.Observable;
import suzane00.global.Observer;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardViewBottomArea;
import suzane00.sale.delivery_order.DeliveryOrder;
import suzane00.sale.delivery_order.view.ViewDeliveryOrder;
import suzane00.sale.sales_payment.SalesPayment;
import suzane00.sale.sales_payment.view.ViewSalesPayment;
import suzane00.sale.sales_retur.SalesRetur;
import suzane00.sale.sales_retur.view.ViewSalesRetur;
import suzane00.sale.stock_reservation.StockReservation;
import suzane00.sale.stock_reservation.view.ViewStockReservation;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
public class FormViewSalesPayment extends Stage implements Observable{
    
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    
    protected ViewSalesPayment viewSP;
    protected StandardViewBottomArea area_bottom;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormViewSalesPayment() {
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
            observer.update(this, viewSP.getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        viewSP = new ViewSalesPayment();
        area_bottom = new StandardViewBottomArea();
        VBox.setVgrow(viewSP, Priority.ALWAYS);
        area_main = new VBox(viewSP, area_bottom);
        area_main.setPrefSize(Utility.SHORT_FORM_WIDTH, Utility.LONG_FORM_HEIGHT);
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_SALE));
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_SALE));
        setTitle("Sales Payments List");
    }
    
    protected void setActionPerformed() {
        area_bottom.setButtonViewActionPerformed(
                e -> {
                    SalesPayment salesPayment = viewSP.getSelectedItems().get(0);
                    FormSalesPayment form = new FormSalesPayment(salesPayment, FormConstant.MODE_VIEW);
                    form.setTitle("View Sales Payment");
                    form.show();
                }
        );
        area_bottom.setButtonEditActionPerformed(
                e -> {
                    SalesPayment salesPayment = viewSP.getSelectedItems().get(0);
                    FormSalesPayment form = new FormSalesPayment(salesPayment, FormConstant.MODE_EDIT);
                    form.setTitle("Edit Sales Payment");
                    form.show();
                }
        );
        area_bottom.setButtonDeleteActionPerformed(
                e -> {
                    Environment.getEntityManager().getTransaction().begin();
                    SalesPayment.deleteSalesInvoice(viewSP.getSelectedItems().get(0));
                    Environment.getEntityManager().getTransaction().commit();
                    TableView tbl = viewSP.getTable();
                    tbl.getItems().remove(tbl.getSelectionModel().getSelectedIndex());
                }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());
    }
}
