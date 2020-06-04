/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.custom_ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.S;
import javafx.scene.input.MouseButton;
import suzane00.global.FormConstant;
import suzane00.inventory.Item;
import suzane00.inventory.ItemPrice;
import suzane00.inventory.Unit;
import suzane00.inventory.custom_ui.NumberTextField;
import suzane00.transaction.model.ItemFileRow;
import suzane00.sale.form.FormPickItemPrice;
import suzane00.transaction.Transaction;

/**
 *
 * @author Usere
 */
public class RowSellPriceText extends TableCell<ItemFileRow, Double> {

    private NumberTextField ntxt_price;

    public RowSellPriceText() {
    }

    @Override
    public void startEdit() {
      if (!isEmpty()) {
        super.startEdit();
        createTextField();
        setText(null);
        setGraphic(ntxt_price);
      }
    }

    @Override
    public void cancelEdit() {
      super.cancelEdit();

      if (getItem() != null)
        setText(getItem().toString());
      setGraphic(null);
    }

    @Override
    public void updateItem(Double item, boolean empty) {
      super.updateItem(item, empty);

      if (empty) {
        setText(null);
        setGraphic(null);
      } else {
        if (isEditing()) {
          if (ntxt_price != null) {
            ntxt_price.setText(String.valueOf(item));
          }
          setText(null);
          setGraphic(ntxt_price);
        } else {
          if(item != null)
            setText(String.valueOf(item));
          setGraphic(null);
        }
      }
    }

    private void createTextField() {
      ntxt_price = new NumberTextField();
      TableView<ItemFileRow> tbl = getTableView();
      ItemFileRow row = tbl.getSelectionModel().getSelectedItem();
      ObservableList<ItemPrice> prices = Transaction.getItemPricesByRow(row);
      
        setOnMouseClicked(
                e -> {
                    if (e.getButton() == MouseButton.SECONDARY) {
                        FormPickItemPrice form = new FormPickItemPrice(prices); 
                        form.showAndWait();;
                        
                        if (form.getReturnStatus() == FormConstant.RETURN_OK) {
                            ItemPrice price = form.getSelectedPrice();
                            ntxt_price.setValue(price.getPrice());
                        }
                            
                    }
                }
        );
    }
  }

  
