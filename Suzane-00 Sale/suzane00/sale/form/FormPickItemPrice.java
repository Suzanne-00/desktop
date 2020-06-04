/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.form;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import suzane00.global.FormConstant;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardDialogBottomArea;
import suzane00.global.custom_ui.StandardSmallDialogBottomArea;
import suzane00.inventory.ItemPrice;

/**
 *
 * @author Usere
 */
public class FormPickItemPrice extends Stage {
    
    int v_returnStatus = FormConstant.RETURN_CANCEL;
    ItemPrice v_itemPrice = null;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    VBox area_main;
    
    /* level 1 area */
    TableView<ItemPrice> tbl_price;
    StandardSmallDialogBottomArea area_bottom;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public FormPickItemPrice( ObservableList<ItemPrice> _prices) {
        initComponents();
        setAppearance();
        configurePriceTable(_prices);
        setActionPerformed();
        Scene scene = new Scene(area_main);
        setScene(scene);
    }
    
    public ItemPrice getSelectedPrice() {
        return v_itemPrice;
    }
    
    public int getReturnStatus() {
        return v_returnStatus;
    }
    
    protected void initComponents() {
        v_returnStatus = FormConstant.RETURN_OK;
    }
    
    protected void setAppearance() {
        tbl_price = new TableView<>();
        area_bottom = new StandardDialogBottomArea();
        tbl_price.setPrefSize(Utility.SMALL_TABLE_WIDTH, Utility.STANDARD_TABLE_HEIGHT);
        area_main = new VBox(tbl_price, area_bottom);
    }
    
    protected void setActionPerformed() {
        area_bottom.setButtonOkActionPerformed(
                e -> {
                    v_returnStatus = FormConstant.RETURN_OK;
                    v_itemPrice = tbl_price.getSelectionModel().getSelectedItem();
                    close();
                }
        );
        area_bottom.setButtonCancelActionPerformed(
                e -> {
                     v_returnStatus = FormConstant.RETURN_CANCEL;
                    v_itemPrice = null;
                    close();
                }
        );
    }
    
    protected void configurePriceTable(ObservableList<ItemPrice> _prices) {
        TableColumn<ItemPrice, Object> typeCol = new TableColumn("Price Type");
        typeCol.setPrefWidth(100);
        typeCol.setCellValueFactory(
                new PropertyValueFactory<>("sellPriceType"));
        
        TableColumn<ItemPrice, Double> nameCol = new TableColumn("Price");
        nameCol.setMinWidth(100);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("price")); 
        
        tbl_price.getColumns().addAll(typeCol, nameCol);
        tbl_price.getItems().addAll(_prices);
    }
}
