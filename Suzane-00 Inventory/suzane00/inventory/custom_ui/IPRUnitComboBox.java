/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.custom_ui;

import suzane00.global.custom_ui.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import static javafx.scene.input.KeyCode.S;
import suzane00.global.Utility;
import suzane00.inventory.Item;
import suzane00.inventory.Unit;
import suzane00.inventory.model.ItemPriceRow;

/**
 *
 * @author Usere
 */
public class IPRUnitComboBox extends TableCell<ItemPriceRow, Unit> {

    private ComboBox cmbBox;

    public IPRUnitComboBox() {
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
    public void updateItem(Unit _unit, boolean empty) {
      super.updateItem(_unit, empty);

      if (empty) {
        setText(null);
        setGraphic(null);
      } else {
        if (isEditing()) {
          if (cmbBox != null) {
            cmbBox.setValue(_unit);
          }
          setText(null);
          setGraphic(cmbBox);
        } else {
          if(_unit != null)
            setText(_unit.getName());
          setGraphic(null);
        }
      }
    }

    private void createComboBox() {
      cmbBox = new ComboBox();
      TableView<ItemPriceRow> tbl = getTableView();
      ItemPriceRow row = tbl.getSelectionModel().getSelectedItem();
      ObservableList<Unit> units = Unit.getAllUnits();
      cmbBox.setOnAction(e -> commitEdit((Unit)cmbBox.getValue()));
      cmbBox.setItems(units);
      cmbBox.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean arg1,Boolean arg2) -> {
                if (!arg2) {
                  commitEdit((Unit)cmbBox.getValue());
                }
            }
      );
    }
  }

  
