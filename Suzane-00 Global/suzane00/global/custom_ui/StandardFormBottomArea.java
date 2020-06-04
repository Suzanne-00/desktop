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
import javafx.scene.text.Font;
import suzane00.global.Utility;

/**
 *
 * @author Usere
 */
public class StandardFormBottomArea extends StandardBottomArea{
    
    
    
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* level 1 area */
    
    // ! protected HBox area_right;
    
    /* level 2 area */
    //area_right
    public Button btn_save ;
    public Button btn_close ;
    public Button btn_new ;
    
    public StandardFormBottomArea() {
        super();
        setButtonsEffects();
    }
    
    
    public Button getSaveButton() {
        return btn_save;
    }
    
    public Button getNewButton() {
        return btn_new;
    }
    
    public Button getCloseButton() {
        return btn_close;
    }
    
    public void setViewMode(boolean _view) {
        btn_save.setVisible(!_view);
        btn_new.setVisible(!_view);
    }
    
    public void setButtonSaveActionPerformed(EventHandler<ActionEvent> _handler) {
        btn_save.setOnAction(_handler);
    }
    
    public void setButtonCloseActionPerformed(EventHandler<ActionEvent> _handler) {
        btn_close.setOnAction(_handler);
    }
  
    public void setButtonNewActionPerformed(EventHandler<ActionEvent> _handler) {
        btn_new.setOnAction(_handler);
    }
    
    public void setButtonsBackgrounds(Background _back) {
        v_buttonBackground = _back;
        btn_save.setBackground(_back);
        btn_new.setBackground(_back);
        btn_close.setBackground(_back);
    }
    
    public void setButtonsStyles(String _css) {
        v_normalStyle = _css;
        btn_save.setStyle(_css);
        btn_new.setStyle(_css);
        btn_close.setStyle(_css);
    }
    
    public void setButtonsOnMousePressed(String _css) {
        btn_save.setOnMousePressed(e -> btn_save.setStyle(v_normalStyle + " " + _css));
        btn_new.setOnMousePressed(e -> btn_new.setStyle(v_normalStyle + " " + _css));
        btn_close.setOnMousePressed(e -> btn_close.setStyle(v_normalStyle + " " + _css));
    }
    
    
//    public void setBackground(String _path) {
//        super.setBackground(_path);
//         try {
//            InputStream is = this.getClass().getResource(
//                    _path).openStream();
//            BackgroundImage myBI= new BackgroundImage(new Image(is,32,32,false,true),
//                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
//                new BackgroundSize(0, 0, false, false, false, true));
//            //btn_save.setBackground(new Background(myBI));
//            btn_save.setStyle("-fx-background-color: rgba(0, 153, 0, 1); "
//                    + "-fx-text-fill: rgba(255, 255, 255, 1); ");
//            //btn_new.setBackground(new Background(myBI));
//            btn_new.setStyle("-fx-background-color: rgba(0, 153, 0, 1);"
//                    + "-fx-text-fill: rgba(255, 255, 255, 1); ");
//            //btn_close.setBackground(new Background(myBI));
//            btn_close.setStyle("-fx-background-color: rgba(0, 153, 0, 1);"
//                    + "-fx-text-fill: rgba(255, 255, 255, 1); ");
//        }
//        catch(IOException e) {
//            e.printStackTrace();;
//        }
//    }
    
//    @Override
//    protected void setAppearance() {
//        super.setAppearance();
//        initComponents();
//        setButtons();
//    }
    
    @Override
    protected void setButtonsEffects() {
        
        btn_save.setStyle(v_normalStyle);
        btn_close.setStyle(v_normalStyle);
        btn_new.setStyle(v_normalStyle);
        btn_save.setOnMousePressed(e -> btn_save.setStyle(v_clickedStyle));
        btn_new.setOnMousePressed(e -> btn_new.setStyle(v_clickedStyle));
        btn_close.setOnMousePressed(e -> btn_close.setStyle(v_clickedStyle));
        btn_save.setOnMouseReleased(e -> btn_save.setStyle(v_normalStyle));
        btn_new.setOnMouseReleased(e -> btn_new.setStyle(v_normalStyle));
        btn_close.setOnMouseReleased(e -> btn_close.setStyle(v_normalStyle));
        btn_save.setOnMouseEntered(e -> btn_save.setEffect(new DropShadow()));
        btn_new.setOnMouseEntered(e -> btn_new.setEffect(new DropShadow()));
        btn_close.setOnMouseEntered(e -> btn_close.setEffect(new DropShadow()));
        btn_save.setOnMouseExited(e -> btn_save.setEffect(null));
        btn_new.setOnMouseExited(e -> btn_new.setEffect(null));
        btn_close.setOnMouseExited(e -> btn_close.setEffect(null));
    }
    
    @Override
    protected void initButtons() {
        btn_save = new Button("Save") ;
        btn_close = new Button("Close");
        btn_new = new Button("New");
    }
    
    @Override
    protected void setButtons() {
        setButtons(btn_new, btn_save, btn_close);
    }
}
