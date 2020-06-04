/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source.form;

import java.util.LinkedList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
import suzane00.global.custom_ui.StandardViewBottomArea;
import suzane00.source.CustomerGroup;
import suzane00.source.Source;
import suzane00.source.SupplierGroup;
import suzane00.source.view.ViewCustomerGroup;
import suzane00.source.view.ViewSupplierGroup;

/**
 *
 * @author Usere
 */
public class FormViewCustomerGroup extends Stage implements Observable{
    
    protected LinkedList<Observer> v_observers;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    
    protected ViewCustomerGroup viewGroup;
    protected StandardViewBottomArea area_bottom;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormViewCustomerGroup() {
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
            observer.update(this, viewGroup.getSelectedItems());
        }
    }
    
    protected void setAppearance() {
        viewGroup = new ViewCustomerGroup();
        area_bottom = new StandardViewBottomArea();
        area_main = new VBox(viewGroup, area_bottom);
        area_main.setSpacing(Utility.SHORT_GAP);
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setBackground(Source.getMasterBackground());
        area_bottom.setBackground(Source.getMasterBackground());
        setTitle("Supplier Groups List");
    }
    
    protected void setActionPerformed() {        
        
        area_bottom.setButtonViewActionPerformed(e -> {
                    CustomerGroup group = viewGroup.getSelectedItems().get(0);
                    FormCustomerGroup form = new FormCustomerGroup(group, FormConstant.MODE_VIEW);
                    form.setTitle("View Supplier Group");
                    form.show();
                }
        );
        area_bottom.setButtonEditActionPerformed(e -> {
                    CustomerGroup group = viewGroup.getSelectedItems().get(0);
                    FormCustomerGroup form = new FormCustomerGroup(group, FormConstant.MODE_EDIT);
                    form.setTitle("Edit Supplier Group");
                    form.show();
                }
        );
        area_bottom.setButtonDeleteActionPerformed(e -> {
                    Environment.getEntityManager().getTransaction().begin();
                    CustomerGroup.deleteGroup(viewGroup.getSelectedItems().get(0));
                    Environment.getEntityManager().getTransaction().commit();
                    TableView tbl = viewGroup.getTable();
                    tbl.getItems().remove(tbl.getSelectionModel().getSelectedIndex());
                }
        );
        area_bottom.setButtonCloseActionPerformed(e -> close());
    }
}
