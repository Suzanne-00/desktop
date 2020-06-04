/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source.form;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Usere
 */
public class FormRegisterSupplierAbstract extends Stage {
    
    protected Label lbl_name = new Label("Name; ");
    protected Label lbl_group = new Label("Group: ");
    protected Label lbl_note = new Label("Note: ");
    protected TextField txt_name = new TextField();
    protected ComboBox cmb_group = new ComboBox();
    protected TextArea txa_note = new TextArea();
}
