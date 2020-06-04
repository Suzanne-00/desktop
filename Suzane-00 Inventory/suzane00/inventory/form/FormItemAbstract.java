/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.form;

import java.util.Map;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import suzane00.global.Utility;
import suzane00.inventory.model.ItemPriceRow;

/**
 *
 * @author Usere
 */
public abstract class FormItemAbstract extends Stage {
    
    protected Label lbl_name = new Label("Name: ");
    protected Label lbl_code = new Label("Code: ");
    protected Label lbl_brand = new Label("Brand: ");
    protected Label lbl_type = new Label("Type: ");
    protected Label lbl_unit = new Label("Unit: ");
    protected Label lbl_note = new Label("Note: ");
    protected Label lbl_image = new Label("Image: ");
    protected TextField txt_name = new TextField();
    protected TextField txt_code = new TextField();
    protected TextArea txa_note = new TextArea();
    protected ComboBox cmb_brand = new ComboBox();
    protected ComboBox cmb_unit = new ComboBox();
    protected ComboBox cmb_type = new ComboBox();
    //protected ListView list_unit = new ListView();
    protected ListView list_type = new ListView();
    protected TableView<ItemPriceRow> tbl_price = new TableView();
    protected ImageView img_item = new ImageView();
    protected Button btn_brand = new Button(Utility.STANDARD_EXPAND_ICON);
    protected Button btn_type = new Button(Utility.STANDARD_EXPAND_ICON);
    protected Button btn_unit = new Button(Utility.STANDARD_EXPAND_ICON);
    
//    protected Button btn_save = new Button("Save");
//    protected Button btn_close = new Button("Close");
//    protected Button btn_new = new Button("New");
//    
    protected abstract void setAppearance();
            
}
