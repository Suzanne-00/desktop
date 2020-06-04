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
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import suzane00.global.Utility;

/**
 *
 * @author Usere
 */
public class StandardSmallDialogBottomArea extends StandardBottomArea{
    
   
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* level 1 area */
    
    // ! protected HBox area_right;
    
    /* level 2 area */
    //area_right
    public Button btn_ok ;
    public Button btn_cancel ;
    
    public StandardSmallDialogBottomArea() {
        super();
        setButtonsEffects();;
    }
    
    public void setViewMode(boolean _view) {
        btn_ok.setVisible(!_view);
    }
    
    public void setButtonOkActionPerformed(EventHandler<ActionEvent> _handler) {
        btn_ok.setOnAction(_handler);
    }
    
    public void setButtonCancelActionPerformed(EventHandler<ActionEvent> _handler) {
        btn_cancel.setOnAction(_handler);
    }
     
    @Override
    protected void initButtons() {
        btn_ok = new Button("Ok") ;
        btn_cancel = new Button("Camcel");
    }
    
    @Override
    protected void setButtons() {
        setButtons(btn_ok, btn_cancel);
    }
    
    @Override
    protected void setButtonsEffects() {
        
        btn_ok.setStyle(v_normalStyle);
        btn_cancel.setStyle(v_normalStyle);
        btn_ok.setOnMousePressed(e -> btn_ok.setStyle(v_clickedStyle));
        btn_cancel.setOnMousePressed(e -> btn_cancel.setStyle(v_clickedStyle));
        btn_ok.setOnMouseReleased(e -> btn_ok.setStyle(v_normalStyle));
        btn_cancel.setOnMouseReleased(e -> btn_cancel.setStyle(v_normalStyle));
        btn_ok.setOnMouseEntered(e -> btn_ok.setEffect(new DropShadow()));
        btn_cancel.setOnMouseEntered(e -> btn_cancel.setEffect(new DropShadow()));
        btn_ok.setOnMouseExited(e -> btn_ok.setEffect(null));
        btn_cancel.setOnMouseExited(e -> btn_cancel.setEffect(null));
    }

}
