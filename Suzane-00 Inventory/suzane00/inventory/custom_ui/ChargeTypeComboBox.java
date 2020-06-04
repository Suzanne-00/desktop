/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.custom_ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import static javafx.scene.input.KeyCode.S;
import suzane00.inventory.Cost;
import suzane00.global.Utility;
import suzane00.inventory.Item;
import suzane00.inventory.Unit;

/**
 *
 * @author Usere
 */
public class ChargeTypeComboBox extends TableCell<Cost, String> {

    private ComboBox cmbBox;

    public ChargeTypeComboBox() {
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
    public void updateItem(String _type, boolean empty) {
      super.updateItem(_type, empty);

      if (empty) {
        setText(null);
        setGraphic(null);
      } else {
        if (isEditing()) {
          if (cmbBox != null) {
            cmbBox.setValue(_type);
          }
          setText(null);
          setGraphic(cmbBox);
        } else {
          if(_type != null)
            setText(_type);
          setGraphic(null);
        }
      }
    }

    private void createComboBox() {
      cmbBox = new ComboBox();
      TableView<Cost> tbl = getTableView();
      Cost row = tbl.getSelectionModel().getSelectedItem();
      ObservableList<String> types = FXCollections.observableArrayList();
      types.addAll(Utility.TYPE_PERCENTAGE, Utility.TYPE_VALUE);
      cmbBox.setItems(types);
      cmbBox.setOnAction(e -> commitEdit((String)cmbBox.getValue()));
      cmbBox.focusedProperty().addListener((ObservableValue<? extends Boolean> arg0, Boolean arg1,Boolean arg2) -> {
                if (!arg2) {
                  commitEdit((String)cmbBox.getValue());
                }
            }
      );
    }
  }

  
