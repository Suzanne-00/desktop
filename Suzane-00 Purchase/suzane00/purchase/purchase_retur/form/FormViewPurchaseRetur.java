/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_retur.form;

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
import suzane00.purchase.purchase_retur.PurchaseRetur;
import suzane00.purchase.purchase_retur.view.ViewPurchaseRetur;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
public class FormViewPurchaseRetur extends Stage implements Observable{
    
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    
    protected ViewPurchaseRetur viewPR;
    protected StandardViewBottomArea area_bottom;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormViewPurchaseRetur() {
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
            observer.update(this, viewPR.getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        viewPR = new ViewPurchaseRetur();
        area_bottom = new StandardViewBottomArea();
        VBox.setVgrow(viewPR, Priority.ALWAYS);
        area_main = new VBox(viewPR, area_bottom);
        area_main.setPrefSize(Utility.SHORT_FORM_WIDTH, Utility.LONG_FORM_HEIGHT);
        area_main.setSpacing(Utility.SHORT_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setBackground(Transaction.getBackground(Transaction.TYPE_PURCHASE));
        area_bottom.setBackground(Transaction.getBackground(Transaction.TYPE_PURCHASE));
    }
    
    protected void setActionPerformed() {
        area_bottom.setButtonViewActionPerformed(e -> {
                    PurchaseRetur purchaseRetur = viewPR.getSelectedItems().get(0);
                    FormPurchaseRetur form = new FormPurchaseRetur(purchaseRetur, FormConstant.MODE_VIEW);
                    form.setTitle("View Purchase Retur");
                    form.show();
                }
        );
        area_bottom.setButtonEditActionPerformed(e -> {
                    PurchaseRetur purchaseRetur = viewPR.getSelectedItems().get(0);
                    FormPurchaseRetur form = new FormPurchaseRetur(purchaseRetur, FormConstant.MODE_EDIT);
                    form.setTitle("Edit Purchase Retur");
                    form.show();
                }
        );
        area_bottom.setButtonDeleteActionPerformed(e -> {
                    Environment.getEntityManager().getTransaction().begin();
                    PurchaseRetur.deletePurchaseRetur(viewPR.getSelectedItems().get(0));
                    Environment.getEntityManager().getTransaction().commit();
                    TableView tbl = viewPR.getTable();
                    tbl.getItems().remove(tbl.getSelectionModel().getSelectedIndex());
                }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());
    }
    
    protected void setBackground() {
        try {
            InputStream isMain = this.getClass().getResource(
                    "/suzane00/global/resource/White Green Gradient.jpg").openStream();
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
}
