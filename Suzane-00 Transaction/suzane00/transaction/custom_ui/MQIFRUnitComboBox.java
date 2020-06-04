/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.transaction.custom_ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import static javafx.scene.input.KeyCode.S;
import suzane00.inventory.Item;
import suzane00.inventory.Unit;
import suzane00.transaction.Transaction;
import suzane00.transaction.model.ItemFileRow;
import suzane00.transaction.model.MQItemFileRow;

/**
 *
 * @author Usere
 */
public class MQIFRUnitComboBox extends TableCell<MQItemFileRow, Unit> {

    private ComboBox cmbBox;

    public MQIFRUnitComboBox() {
    }

    @Override
    public void startEdit() {
      if (!isEmpty()) {
        super.startEdit();
        createComboBox();
        setText(null);
        setGraphic(cmbBox);
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
    public void updateItem(Unit item, boolean empty) {
      super.updateItem(item, empty);

      if (empty) {
        setText(null);
        setGraphic(null);
      } else {
        if (isEditing()) {
          if (cmbBox != null) {
            cmbBox.setValue(item);
          }
          setText(null);
          setGraphic(cmbBox);
        } else {
          if(item != null)
            setText(item.toString());
          setGraphic(null);
        }
      }
    }

    private void createComboBox() {
      cmbBox = new ComboBox();
      TableView<MQItemFileRow> tbl = getTableView();
      ItemFileRow row = tbl.getSelectionModel().getSelectedItem();
      Item item = Transaction.getItem(row);
      ObservableList<Unit> units = Unit.getUnitsByItemId(item);
      cmbBox.setItems(units);
      cmbBox.setOnAction(e -> commitEdit((Unit)cmbBox.getValue()));
      cmbBox.focusedProperty().addListener(
            (ObservableValue<? extends Boolean> arg0, Boolean arg1,Boolean arg2) -> {
                if (!arg2) {
                  commitEdit((Unit)cmbBox.getValue());
                }
            }
      );
    }
  }

  
