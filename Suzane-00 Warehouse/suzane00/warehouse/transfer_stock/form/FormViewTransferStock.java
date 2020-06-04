/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.warehouse.transfer_stock.form;

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
import suzane00.transaction.Transaction;
import suzane00.warehouse.stock_adjustment.StockAdjustment;
import suzane00.warehouse.stock_adjustment.view.ViewStockAdjustment;
import suzane00.warehouse.transfer_stock.TransferStock;
import suzane00.warehouse.transfer_stock.view.ViewTransferStock;

/**
 *
 * @author Usere
 */
public class FormViewTransferStock extends Stage implements Observable{
    
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    
    protected ViewTransferStock viewTransferStock;
    protected StandardViewBottomArea area_bottom;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormViewTransferStock() {
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
            observer.update(this, viewTransferStock.getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        viewTransferStock = new ViewTransferStock();
        area_bottom = new StandardViewBottomArea();
        VBox.setVgrow(viewTransferStock, Priority.ALWAYS);
        area_main = new VBox(viewTransferStock, area_bottom);
        area_main.setSpacing(Utility.SHORT_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setPrefSize(Utility.SHORT_FORM_WIDTH, Utility.LONG_FORM_HEIGHT);
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_INVENTORY));
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_INVENTORY));
        setTitle("Transfer Stocks List");
    }
    
    protected void setActionPerformed() {
        area_bottom.setButtonViewActionPerformed(e -> {
                    TransferStock transferStock = viewTransferStock.getSelectedItems().get(0);
                    FormTransferStock form = new FormTransferStock(transferStock, FormConstant.MODE_VIEW);
                    form.setTitle("View Transfer Stock");
                    form.show();
                }
        );
        area_bottom.setButtonEditActionPerformed(e -> {
                    TransferStock transferStock = viewTransferStock.getSelectedItems().get(0);
                    FormTransferStock form = new FormTransferStock(transferStock, FormConstant.MODE_EDIT);
                    form.setTitle("Edit Transfer Stock");
                    form.show();
                }
        );
        area_bottom.setButtonDeleteActionPerformed(e -> {
                    Environment.getEntityManager().getTransaction().begin();
                    TransferStock.deleteTransferStock(viewTransferStock.getSelectedItems().get(0));
                    Environment.getEntityManager().getTransaction().commit();
                    TableView tbl = viewTransferStock.getTable();
                    tbl.getItems().remove(tbl.getSelectionModel().getSelectedIndex());
                }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());
    }
}
