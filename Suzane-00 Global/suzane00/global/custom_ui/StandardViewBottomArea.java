/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.global.custom_ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import suzane00.global.Utility;

/**
 *
 * @author Usere
 */
public class StandardViewBottomArea extends StandardBottomArea{
        
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* level 1 area */
    
    // ! protected HBox area_right;
    
    /* level 2 area */
    //area_right
    public Button btn_view  ;
    public Button btn_edit ;
    public Button btn_delete ;
    public Button btn_close ; 
    
    public StandardViewBottomArea() {
        super();
        setButtonsEffects();
    }
    
    public void setButtonViewActionPerformed(EventHandler<ActionEvent> _handler) {
        btn_view.setOnAction(_handler);
    }
    
    public void setButtonEditActionPerformed(EventHandler<ActionEvent> _handler) {
        btn_edit.setOnAction(_handler);
    }
    
    public void setButtonDeleteActionPerformed(EventHandler<ActionEvent> _handler) {
        btn_delete.setOnAction(_handler);
    }
    
    public void setButtonCloseActionPerformed(EventHandler<ActionEvent> _handler) {
        btn_close.setOnAction(_handler);
    }
    
    @Override
    protected void setButtonsEffects() {
        
        btn_view.setStyle(v_normalStyle);
        btn_edit.setStyle(v_normalStyle);
        btn_delete.setStyle(v_normalStyle);
        btn_close.setStyle(v_normalStyle);
        btn_view.setOnMousePressed(e -> btn_view.setStyle(v_clickedStyle));
        btn_edit.setOnMousePressed(e -> btn_edit.setStyle(v_clickedStyle));
        btn_delete.setOnMousePressed(e -> btn_delete.setStyle(v_clickedStyle));
        btn_close.setOnMousePressed(e -> btn_close.setStyle(v_clickedStyle));
        btn_view.setOnMouseReleased(e -> btn_view.setStyle(v_normalStyle));
        btn_edit.setOnMouseReleased(e -> btn_edit.setStyle(v_normalStyle));
        btn_delete.setOnMouseReleased(e -> btn_delete.setStyle(v_normalStyle));
        btn_close.setOnMouseReleased(e -> btn_close.setStyle(v_normalStyle));
        btn_view.setOnMouseEntered(e -> btn_view.setEffect(new DropShadow()));
        btn_edit.setOnMouseEntered(e -> btn_edit.setEffect(new DropShadow()));
        btn_delete.setOnMouseEntered(e -> btn_delete.setEffect(new DropShadow()));
        btn_close.setOnMouseEntered(e -> btn_close.setEffect(new DropShadow()));
        btn_view.setOnMouseExited(e -> btn_view.setEffect(null));
        btn_edit.setOnMouseExited(e -> btn_edit.setEffect(null));
        btn_delete.setOnMouseExited(e -> btn_delete.setEffect(null));
        btn_close.setOnMouseExited(e -> btn_close.setEffect(null));
    }
    
    
    @Override
    protected void initButtons() {
        btn_view = new Button("View") ;
        btn_edit = new Button("Edit");
        btn_delete = new Button("Delete");
        btn_close = new Button("Close");     
    }
    
    @Override
    protected void setButtons() {
        setButtons(btn_view, btn_edit, btn_delete, btn_close);
    }
}
