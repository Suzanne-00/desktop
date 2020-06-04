/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.form;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.DepthTest;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import static org.eclipse.persistence.sessions.SessionProfiler.Transaction;
import suzane00.global.Environment;
import suzane00.global.FormConstant;
import suzane00.global.Utility;
import suzane00.global.custom_ui.StandardFormBottomArea;
import suzane00.inventory.Brand;
import suzane00.inventory.Item;
import suzane00.inventory.PackedItem;
import suzane00.inventory.ProductType;
import suzane00.inventory.SellPriceType;
import suzane00.inventory.Unit;
import suzane00.inventory.custom_ui.IPRPriceTypeComboBox;
import suzane00.inventory.custom_ui.IPRUnitComboBox;
import suzane00.inventory.custom_ui.NumberTextField;
import suzane00.inventory.model.ItemPriceRow;

/**
 *
 * @author Usere
 */
public class FormItem extends FormItemAbstract {
    
    Item v_item;
    int v_mode;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* root area */
    
    protected VBox area_main;
    
    /* level 1 area */
    
    protected HBox area_upper;
    protected VBox area_lower;
    protected StandardFormBottomArea area_bottom;
    
    /* level 2 area */
    
    // area_upper
    protected VBox area_general;
    protected GridPane area_type;
    protected VBox area_image;
    
    // area_lower
    protected GridPane area_upperMid;
    // ! protected TableView tblItem;
    
    /* level 3 area */
    
    // area_upper -> area_general
    protected GridPane area_upperGeneral;
    protected VBox area_note;
    
    // area_upper -> area_type
    // ! protected Label lbl_type = new Label("Type: ");
    // ! protected ComboBox cmb_type = new ComboBox();
    // ! protected ListView list_type = new ListView();
    
    // area_upper -> area_image
    // ! protected Label lbl_image = new Label("Image: ");
    // ! protected ImageView img_item = new ImageView();
    
    // area_lower -> area_upperMid
    protected Label lbl_price;
    protected Label lbl_priceType;
    protected NumberTextField ntxt_price;
    protected ComboBox cmb_priceType;
    protected Button btn_addPrice;
    
    
    /* level 4 area */
    
    // area_upper -> area_left -> area_upperGeneral
    // ! protected Label lbl_name = new Label("Name: ");
    // ! protected Label lbl_code = new Label("Code: ");
    // ! protected Label lbl_brand = new Label("Brand: ");
    // ! protected TextField txt_name = new TextField();
    // ! protected TextField txt_code = new TextField();
    // ! protected ComboBox cmb_brand = new ComboBox();
    // ! protected Button btn_brand = new Button(Utility.STANDARD_EXPAND_ICON);
    
    // area_upper -> area_left -> area_note
    // ! protected Label lbl_note = new Label("Note: ");
    // ! protected TextArea txa_note = new TextArea();
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
        
    public FormItem() {
        initComponents();
        setAppearance();
        setData();
        setActionPerformed();
        Scene scene = new Scene(area_main);
        this.setScene(scene);
    }
    
    public FormItem(Item _item, int _mode) {
        this();
        setVariables(_item, _mode);
        disiplayData();
        if (v_mode == FormConstant.MODE_VIEW)
            disableEditing(true);
    }
   
    protected void initComponents() {
        v_mode = FormConstant.MODE_NEW;
    }
        
    @Override
    protected void setAppearance() {
        setMainArea();
    }
    
    protected void setData() {
        configurePriceTable();
        loadTypeData();
        loadBrandData();
        loadUnitData();
        loadPriceTypeData();
    }
    
    protected void setActionPerformed() {
        area_bottom.setButtonSaveActionPerformed(
                e -> {
                    if(!validateSave())
                        return;
                    
                    Item item = buildItem();
                    Environment.getEntityManager().getTransaction().begin();
                    Item.saveItem(item, tbl_price.getItems());
                    Environment.getEntityManager().getTransaction().commit();
                    clearInputs();
                }
        ); 
        
        area_bottom.setButtonCloseActionPerformed(e -> close());
        area_bottom.setButtonNewActionPerformed(
                e -> {
                    clearInputs();
                    disableEditing(false);
                }
        );
        
        btn_type.setOnAction( 
                e -> {
                    validateAddType();
                    list_type.getItems().add(cmb_type.getValue());
                }
        );
        btn_addPrice.setOnAction(e -> {
                    if(!validateAddPriceType())
                        return ;
                    
                    ItemPriceRow row = new ItemPriceRow((Unit) cmb_unit.getValue(), 
                            (SellPriceType) cmb_priceType.getValue(), ntxt_price.getValue());
                    tbl_price.getItems().add(row);
                }
        );
    }
    
    protected void setVariables(Item _item, int _mode) {
        v_item = _item;
        v_mode = _mode;
    }
    
    protected void disiplayData() {
        txt_code.setText(v_item.getCode());
        txt_name.setText(v_item.getName());
        cmb_brand.setValue(v_item.getBrand());
        list_type.getItems().addAll(v_item.getProductTypes());
        tbl_price.getItems().addAll(ItemPriceRow.findByItem(v_item));
    }
    
    /*----------------------------------------------- GUI CODE ---------------------------------------------- */
    
    /* root area */
    
    protected void setMainArea() {
        setUpperArea();
        setLowerArea();
        area_bottom = new StandardFormBottomArea();
        area_main = new VBox(area_upper, area_lower, area_bottom);
        //area_main.setPrefSize(Utility.SHORT_FORM_WIDTH, Utility.STANDARD_FORM_HEIGHT); // soal ukuran terserah kebutuhan
        area_main.setPadding(new Insets(Utility.STANDARD_GAP));
        area_main.setSpacing(Utility.STANDARD_GAP); 
        area_main.setBackground(Item.getMasterBackground());
        area_bottom.setBackground(Item.getMasterBackground());
    }
    
    /* level 1 area */

    protected void setUpperArea() {
        setGeneralArea();
        setTypeArea();
        setImageArea();
        
        area_upper = new HBox(area_general, area_type, area_image);
        area_upper.setSpacing(Utility.STANDARD_GAP);
    }
    
    protected void setLowerArea() {
        setUpperMidArea() ;
        tbl_price.setPrefWidth(Utility.STANDARD_TABLE_WIDTH);
        tbl_price.setPrefHeight(Utility.STANDARD_TABLE_HEIGHT);
        
        area_lower = new VBox(area_upperMid, tbl_price);
        area_lower.setSpacing(Utility.SHORT_GAP);
    }
    
    
    /* level 2 area*/
    
    // setUpperArea()
    protected void setGeneralArea() {
        setUpperGeneralArea();
        setNoteArea();
        area_general = new VBox(area_upperGeneral, area_note);
        area_general.setSpacing(Utility.SHORT_GAP);
    } 
    
    protected void setTypeArea() {
         area_type = new GridPane();
         area_type.setHgap(Utility.SHORT_GAP);
         area_type.setVgap(Utility.SHORT_GAP);
         cmb_type.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
         list_type.setPrefWidth(Utility.STANDARD_LIST_WIDTH);
         list_type.setPrefHeight(Utility.STANDARD_LIST_HEIGHT);
         GridPane.setColumnSpan(list_type, 2);
         
         area_type.add(lbl_type, 0, 0);
         area_type.add(cmb_type, 0, 1);
         area_type.add(btn_type, 1, 1);
         area_type.add(list_type, 0, 2);
    }
    
    protected void setImageArea() {
         img_item.setFitWidth(Utility.STANDARD_TXA_WIDTH);
         img_item.setFitHeight(Utility.STANDARD_TXA_HEIGHT);
         
         area_image = new VBox(lbl_image, img_item);
         area_image.setSpacing(Utility.SHORT_GAP);
    }
    
   // setLowerArea() 
    protected void setUpperMidArea() {
         lbl_price = new Label("Price: ");
         lbl_priceType = new Label("Price type; ");
         ntxt_price = new NumberTextField();
         cmb_priceType = new ComboBox();
         btn_addPrice = new Button("Add");
         area_upperMid = new GridPane();
         area_upperMid.setPadding(new Insets(Utility.SHORT_GAP));
         area_upperMid.setHgap(Utility.SHORT_GAP);
         area_upperMid.setVgap(Utility.SHORT_GAP);
         ntxt_price.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
         cmb_priceType.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
         cmb_unit.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);         

         area_upperMid.add(lbl_unit, 0, 0);
         area_upperMid.add(cmb_unit, 0, 1);
         area_upperMid.add(lbl_priceType, 1, 0);
         area_upperMid.add(cmb_priceType, 1, 1);
         area_upperMid.add(lbl_price, 2, 0);
         area_upperMid.add(ntxt_price, 2, 1);
         area_upperMid.add(btn_addPrice, 3, 1);
         area_upperMid.setBackground(Item.getMasterBackground());
    }
    
    
    /* level 3 area */
    
    // setGeneralArea()
    protected void setUpperGeneralArea() {
         area_upperGeneral = new GridPane();
         area_upperGeneral.setHgap(Utility.SHORT_GAP);
         area_upperGeneral.setVgap(Utility.SHORT_GAP);
         txt_name.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
         txt_code.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
         cmb_brand.setPrefWidth(Utility.STANDARD_TEXT_WIDTH);
         
         area_upperGeneral.add(lbl_name, 0, 0);
         area_upperGeneral.add(txt_name, 1, 0);
         area_upperGeneral.add(lbl_code, 0, 1);
         area_upperGeneral.add(txt_code, 1, 1);
         area_upperGeneral.add(lbl_brand, 0, 2);
         area_upperGeneral.add(cmb_brand, 1, 2);
    }
     
    protected void setNoteArea() {
         
         txa_note.setPrefWidth(Utility.STANDARD_TXA_WIDTH);
         txa_note.setPrefHeight(Utility.STANDARD_TXA_HEIGHT);
         
         area_note = new VBox(lbl_note, txa_note);
         area_note.setSpacing(Utility.SHORT_GAP);
    }
    
    
    
    /*----------------------------------------------- END GUI CODE ---------------------------------------------- */
    
    protected void configurePriceTable() {
        TableColumn<ItemPriceRow, Unit> unitCol = new TableColumn("Unit");
        unitCol.setMinWidth(100);
        unitCol.setCellValueFactory(
                new PropertyValueFactory<>("unit"));
        unitCol.setCellFactory((TableColumn<ItemPriceRow, Unit> p) -> {
                 return new IPRUnitComboBox();
            }
        );
        unitCol.setOnEditCommit(
            (TableColumn.CellEditEvent<ItemPriceRow, Unit> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setUnit(t.getNewValue());
            }
        );
 
        TableColumn<ItemPriceRow, SellPriceType> priceTypeCol = new TableColumn("Price Type");
        priceTypeCol.setMinWidth(100);
        priceTypeCol.setCellValueFactory(
                new PropertyValueFactory<>("sellPriceType"));
        priceTypeCol.setCellFactory((TableColumn<ItemPriceRow, SellPriceType> p) -> {
                 return new IPRPriceTypeComboBox();
            }
        );
        priceTypeCol.setOnEditCommit(
            (TableColumn.CellEditEvent<ItemPriceRow, SellPriceType> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setSellPriceType(t.getNewValue());
            }
        );
        
        TableColumn<ItemPriceRow, Number> priceCol = new TableColumn("Price");
        priceCol.setMinWidth(100);
        priceCol.setCellValueFactory(
                new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(TextFieldTableCell.<ItemPriceRow, Number>forTableColumn(new NumberStringConverter()));
        priceCol.setOnEditCommit(
            (TableColumn.CellEditEvent<ItemPriceRow, Number> t) -> {
                t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                 .setPrice(t.getNewValue().doubleValue());
            }
        );
 
        tbl_price.getColumns().addAll(unitCol, priceTypeCol, priceCol);
        tbl_price.setEditable(true);
    }
    
    protected void loadTypeData() {
        cmb_type.setItems(FXCollections.observableArrayList(ProductType.getAllTypes()));
    }
    
    protected void loadBrandData() {
        cmb_brand.setItems(FXCollections.observableArrayList(Brand.getAllBrands()));
    }
    
    protected void loadUnitData() {
        cmb_unit.setItems(FXCollections.observableArrayList(Unit.getAllUnits()));
    }
    
    protected void loadPriceTypeData() {
        cmb_priceType.setItems(FXCollections.observableArrayList(SellPriceType.getAllSellPriceTypes()));
    }
    
    protected void clearInputs() {
       txt_name.clear();
       txt_code.clear();
       ntxt_price.clear();
       txa_note.clear();
       cmb_brand.setValue(null);
       cmb_priceType.setValue(null);
       cmb_type.setValue(null);
       cmb_unit.setValue(null);
       list_type.getItems().clear();
//       list_unit.getItems().clear();
       tbl_price.getItems().clear();
   }
    
    protected Item buildItem() {
        Item item = new Item();
        
        item.setName(txt_name.getText());
        item.setProductTypes(buildTypes());
        item.setCode(txt_code.getText());
        item.setBrand((Brand)cmb_brand.getValue());
        item.setNote(txa_note.getText());
        
        return item;
    }

    protected ObservableList<ProductType> buildTypes() {
        ObservableList<ProductType> types = FXCollections.observableArrayList();
        for (ProductType t : (ObservableList<ProductType>) list_type.getItems()) {
            types.add(t);
        }    
        return types;
    }   
    
    protected boolean validateSave() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in saving item");
        
        if (txt_name == null) {
            alert.setContentText("Item doesn't have a name");
            alert.showAndWait();
            return false ;
        }
        
        return true ;
    }
    
    protected boolean validateAddPriceType() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in adding price");
        
        if(cmb_unit.getValue() == null) {
            alert.setContentText("Plaese choose the unit for the added price");
            alert.showAndWait();
            return false ;
        }
        
        if(cmb_priceType.getValue() == null) {
            alert.setContentText("Plaese choose the price type for the added price");
            alert.showAndWait();
            return false ;
        }
        
        if(ntxt_price.getValue() == null || ntxt_price.getValue() <= Utility.DOUBLE_TOLERANCE) {
            alert.setContentText("Plaese choose the price for the added price");
            alert.showAndWait();
            return false ;
        }
        
        for (ItemPriceRow row : tbl_price.getItems()) {
            if(row.getUnit().equals((Unit) cmb_unit.getValue()) &&
                row.getSellPriceType().equals((SellPriceType) cmb_priceType.getValue())) {
                    alert.setContentText("Spesific price is already on the list");
                    alert.showAndWait();
                    return false ;
            }
        }
        
        return true;
    }
    
    protected boolean validateAddType() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error in adding type");
        
        if (cmb_type.getValue() == null) {
            alert.setContentText("Please choose the type you want to add");
            alert.showAndWait();
            return false ;
        }
        
        return true ;
    }
    
    protected void disableEditing(boolean _disable) {
        txt_name.setDisable(_disable);
        txt_name.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txt_code.setDisable(_disable);
        txt_code.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        ntxt_price.setDisable(_disable);
        ntxt_price.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        txa_note.setDisable(_disable);
        txa_note.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_brand.setDisable(_disable);
        cmb_brand.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_priceType.setDisable(_disable);
        cmb_priceType.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_type.setDisable(_disable);
        cmb_type.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        cmb_unit.setDisable(_disable);
        cmb_unit.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        list_type.setDisable(_disable);
        list_type.setStyle("-fx-opacity: 1; -fx-text-fill: black;-fx-background-color: white");
        btn_type.setVisible(!_disable);
        btn_addPrice.setVisible(!_disable);
        tbl_price.setEditable(!_disable);
        area_bottom.setViewMode(_disable);
        area_upperMid.setVisible(!_disable);
    }
}
