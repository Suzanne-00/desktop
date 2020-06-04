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
public class StandardDialogBottomArea extends StandardSmallDialogBottomArea{
    
    
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* level 1 area */
    
    // ! HBox area_right;
    
    /* level 2 area */
    // area_right
    
    // ! Button btn_ok = new Button("Ok") ;
    // ! Button btn_cancel = new Button("Camcel");
    public Button btn_new ;
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public StandardDialogBottomArea() {
        super();
    }
    
    public void setViewMode(boolean _view) {
        super.setViewMode(_view);
        btn_new.setVisible(false);
    }
    
    public void setButtonNewActionPerformed(EventHandler<ActionEvent> _handler) {
        btn_new.setOnAction(_handler);
    }
    
    @Override
    protected void setButtonsEffects() {
        super.setButtonsEffects();
        btn_new.setStyle(v_normalStyle);
        btn_new.setOnMousePressed(e -> btn_ok.setStyle(v_clickedStyle));
        btn_new.setOnMouseReleased(e -> btn_ok.setStyle(v_normalStyle));
        btn_new.setOnMouseEntered(e -> btn_ok.setEffect(new DropShadow()));
        btn_new.setOnMouseExited(e -> btn_ok.setEffect(null));
    }
 
    @Override
    protected void initButtons() {
        super.initButtons();
        btn_new = new Button("New");
    }
    
    @Override
    protected void setButtons() {
        setButtons(btn_new, btn_ok, btn_cancel);
    }
   
}
