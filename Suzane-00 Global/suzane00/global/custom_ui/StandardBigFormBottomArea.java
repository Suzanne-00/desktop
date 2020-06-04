/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.global.custom_ui;

import java.io.IOException;
import java.io.InputStream;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import suzane00.global.Utility;

/**
 *
 * @author Usere
 */
public class StandardBigFormBottomArea extends StandardFormBottomArea{
        
    protected boolean v_isCanceled = false;
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* level 1 area */
    
    // ! protected HBox area_right;
    
    /* level 2 area */
    //area_right
    // ! protected Button btn_save ;
    // ! protected Button btn_close ;
    // ! protected Button btn_new ;
    protected Button btn_print ; 
    
    /*------------------------------------------------- END GUI COMPONENTS ------------------------------------------ */
    
    public StandardBigFormBottomArea() {
        super();
    }
    
    public Button getPrintButton() {
        return btn_print;
    }
    
    @Override
    public void setButtonNewActionPerformed(EventHandler<ActionEvent> _handler) {
       
        super.setButtonNewActionPerformed(
                e -> {
                    _handler.handle(e);
                    setButtons(btn_new, btn_save, btn_close);
                    btn_save.setVisible(true);
                    btn_new.setVisible(true);
                }
        );
    }
    
    public void setButtonPrintPerformed(EventHandler<ActionEvent> _handler) {
        btn_print.setOnAction(_handler);
    }
    
    public void isCanceled(boolean _cancel) {
        v_isCanceled = _cancel;
    }
    
    @Override
    public void setViewMode(boolean _view) {
        super.setViewMode(_view);
        btn_print.setVisible(!_view);
    }
    
    @Override
    public void setButtonSaveActionPerformed(EventHandler<ActionEvent> _handler) {
        super.setButtonSaveActionPerformed(
            e -> {
                _handler.handle(e);
                if(!v_isCanceled)
                    setButtons(btn_new, btn_print, btn_close);
                else
                    v_isCanceled = false ;
                btn_new.setVisible(true);
                btn_print.setVisible(true);
            }
        );
    }
    
    @Override
    protected void setButtonsEffects() {
        super.setButtonsEffects();
        btn_print.setStyle(v_normalStyle);
        btn_print.setOnMousePressed(e -> btn_print.setStyle(v_clickedStyle));
        btn_print.setOnMouseReleased(e -> btn_print.setStyle(v_normalStyle));
        btn_print.setOnMouseEntered(e -> btn_print.setEffect(new DropShadow()));
        btn_print.setOnMouseExited(e -> btn_print.setEffect(null));
    }
    
    @Override
    public void setButtonsBackgrounds(Background _back) {
        super.setButtonsBackgrounds(_back);
        btn_print.setBackground(_back);
    }
    
    @Override
    public void setButtonsStyles(String _css) {
        super.setButtonsStyles(_css);
        btn_print.setStyle(_css);
    }
    
    @Override
    public void setButtonsOnMousePressed(String _css) {
        super.setButtonsOnMousePressed(_css);
        btn_print.setOnMousePressed(e -> btn_print.setStyle(v_normalStyle + " " + _css));
    }
    
    
//    @Override
//    public void setBackground(String _path) {
//        super.setBackground(_path);
//         try {
//            InputStream is = this.getClass().getResource(
//                    _path).openStream();
//            BackgroundImage myBI= new BackgroundImage(new Image(is,32,32,false,true),
//                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
//                new BackgroundSize(0, 0, false, false, false, true));
//            //btn_print.setBackground(new Background(myBI));
//            btn_print.setStyle("-fx-background-color: rgba(0, 153, 0, 1);"
//                    + "-fx-text-fill: rgba(255, 255, 255, 1); ");
//        }
//        catch(IOException e) {
//            e.printStackTrace();;
//        }
//    }
//    
    @Override
    protected void initButtons() {
       super.initButtons();
       btn_print = new Button("Print");
    }
    
//    @Override
//    protected void setButtons() {
//        setButtons(btn_new, btn_print, btn_save, btn_close);
//    }
}
