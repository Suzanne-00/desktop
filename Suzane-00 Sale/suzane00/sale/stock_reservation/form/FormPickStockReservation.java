/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.stock_reservation.form;

import java.util.LinkedList;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
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
import suzane00.global.custom_ui.StandardSmallDialogBottomArea;
import suzane00.inventory.Item;
import suzane00.sale.stock_reservation.StockReservation;
import suzane00.sale.stock_reservation.view.ViewStockReservation;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
public class FormPickStockReservation extends Stage implements Observable{
    
    protected ObservableList<StockReservation> v_stockReservation ;
    protected int v_returnStatus = FormConstant.RETURN_CANCEL;
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    protected ViewStockReservation viewStockReservation;
    protected StandardSmallDialogBottomArea area_bottom;
   
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormPickStockReservation() {
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
    
    public ObservableList<StockReservation> getSelectedItems() {
        return v_stockReservation;
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
            observer.update(this, viewStockReservation.getSelectedItems());
        }
    }
    
    protected void initComponents() {
        //v_returnStatus = FormConstant.RETURN_OK;
    }
    
    protected void setAppearance() {
        setMainArea();
        setTitle("Stock Reservations List");
    }
    
    protected void setActionPerformed() {
        area_bottom.setButtonOkActionPerformed(e -> {
                    v_returnStatus = FormConstant.RETURN_OK;
                    v_stockReservation = viewStockReservation.getSelectedItems();
                    notifyObservers();
                    close();
                }
        
        );
        area_bottom.setButtonCancelActionPerformed(e -> {
                    v_returnStatus = FormConstant.RETURN_CANCEL;
                    v_stockReservation = null;
                    close();
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */

    /* root area */
    
    protected void setMainArea() {
        viewStockReservation = new ViewStockReservation();
        area_bottom = new StandardSmallDialogBottomArea();
        VBox.setVgrow(viewStockReservation, Priority.ALWAYS);
        area_main = new VBox(viewStockReservation, area_bottom);
        area_main.setPrefSize(Utility.SHORT_FORM_WIDTH, Utility.STANDARD_FORM_HEIGHT);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_SALE));
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_SALE));
    }
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
}
