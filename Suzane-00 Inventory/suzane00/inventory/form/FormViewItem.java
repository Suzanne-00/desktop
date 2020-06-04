/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.form;

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
import suzane00.inventory.Item;
import suzane00.inventory.view.ViewItem;

/**
 *
 * @author Usere
 */
public class FormViewItem extends Stage implements Observable{
    
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    
    protected ViewItem viewItem;
    protected StandardViewBottomArea area_bottom;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormViewItem() {
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
            observer.update(this, viewItem.getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        viewItem = new ViewItem();
        area_bottom = new StandardViewBottomArea();
        area_main = new VBox(viewItem, area_bottom);
        VBox.setVgrow(viewItem, Priority.ALWAYS);
        area_main.setSpacing(Utility.SHORT_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setPrefSize(Utility.SHORT_FORM_WIDTH, Utility.LONG_FORM_HEIGHT);
        area_main.setBackground(Item.getMasterBackground());
        area_bottom.setBackground(Item.getMasterBackground());
        setTitle("Items List");
    }
    
    protected void setBackground() {
        try {
            InputStream isMain = this.getClass().getResource(
                    "/suzane00/global/resource/Blue Gradient 2.jpg").openStream();
            BackgroundImage imgMain= new BackgroundImage(new Image(isMain,32,32,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0, 0, false, false, false, true));
          
            
            area_main.setBackground(new Background(imgMain));
            area_bottom.setBackground(new Background(imgMain));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
      
    
    protected void setActionPerformed() {        
        
        area_bottom.setButtonViewActionPerformed(
                e -> {
                    Item item = viewItem.getSelectedItems().get(0);
                    FormItem form = new FormItem(item, FormConstant.MODE_VIEW);
                    form.setTitle("View Item");
                    form.show();
                }
        );
        area_bottom.setButtonEditActionPerformed(
                e -> {
                    Item item = viewItem.getSelectedItems().get(0);
                    FormItem form = new FormItem(item, FormConstant.MODE_EDIT);
                    form.setTitle("Edit Item");
                    form.show();
                }
        );
        area_bottom.setButtonDeleteActionPerformed(
                e -> {
                    Environment.getEntityManager().getTransaction().begin();
                    Item.deleteItem(viewItem.getSelectedItems().get(0));
                    Environment.getEntityManager().getTransaction().commit();
                    TableView tbl = viewItem.getTable();
                    tbl.getItems().remove(tbl.getSelectionModel().getSelectedIndex());
                }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());
    }
}
