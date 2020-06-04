/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.warehouse.stock_adjustment.form;

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
import suzane00.transaction.Transaction;
import suzane00.warehouse.stock_adjustment.StockAdjustment;
import suzane00.warehouse.stock_adjustment.view.ViewStockAdjustment;

/**
 *
 * @author Usere
 */
public class FormPickStockAdjustment extends Stage implements Observable{
    
    protected ObservableList<StockAdjustment> v_stockAdjustment ;
    protected int v_returnStatus = FormConstant.RETURN_CANCEL;
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    protected ViewStockAdjustment viewStockAdjustment;
    protected StandardSmallDialogBottomArea area_bottom;
   
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormPickStockAdjustment() {
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
    
    public ObservableList<StockAdjustment> getSelectedItems() {
        return v_stockAdjustment;
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
            observer.update(this, viewStockAdjustment.getSelectedItems());
        }
    }
    
    protected void initComponents() {
        //v_returnStatus = FormConstant.RETURN_OK;
    }
    
    protected void setAppearance() {
        setMainArea();
        setTitle("Pick Stock Adjustment");
    }
    
    protected void setActionPerformed() {
        area_bottom.setButtonOkActionPerformed(e -> {
                    v_returnStatus = FormConstant.RETURN_OK;
                    v_stockAdjustment = viewStockAdjustment.getSelectedItems();
                    notifyObservers();
                    close();
                }
        
        );
        area_bottom.setButtonCancelActionPerformed(e -> {
                    v_returnStatus = FormConstant.RETURN_CANCEL;
                    v_stockAdjustment = null;
                    close();
                }
        );
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */

    /* root area */
    
    protected void setMainArea() {
        viewStockAdjustment = new ViewStockAdjustment();
        area_bottom = new StandardSmallDialogBottomArea();
        area_main = new VBox(viewStockAdjustment, area_bottom);
        VBox.setVgrow(viewStockAdjustment, Priority.ALWAYS);
        area_main.setPrefSize(Utility.SHORT_FORM_WIDTH, Utility.STANDARD_FORM_HEIGHT);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setSpacing(Utility.STANDARD_GAP);
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_INVENTORY));
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_INVENTORY));
    }
    
    protected void setBackground() {
        try {
            InputStream isMain = this.getClass().getResource(
                    "/suzane00/global/resource/White Brown Gradient.jpg").openStream();
            BackgroundImage imgMain= new BackgroundImage(new Image(isMain,1600,900,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0, 0, false, false, false, true));
     
            area_main.setBackground(new Background(imgMain));
            area_bottom.setBackground(new Background(imgMain));
        }
        catch(IOException e) {
            e.printStackTrace();;
        }
    }
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
}
