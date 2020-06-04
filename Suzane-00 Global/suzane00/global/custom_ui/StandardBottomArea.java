/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.global.custom_ui;

import java.io.IOException;
import java.io.InputStream;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
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
public abstract class StandardBottomArea extends AnchorPane{
    
    protected Background v_buttonBackground;
    protected String v_normalStyle = "-fx-background-color: rgba(0,102,102,1); " +
            "-fx-text-fill: rgba(255,255,255,1); -fx-font-size: 15; " +
            "-fx-border-radius: 20 20 20 20; -fx-background-radius: 20 20 20 20;" ;
  
    protected String v_clickedStyle = "-fx-background-color: rgba(128,128,128,1); " +
            "-fx-text-fill: rgba(255,255,255,1); -fx-font-size: 15;";
        
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* level 1 area */
    
    public HBox area_right;
    
    
    public StandardBottomArea() {
        setAppearance();
    }
    
    
    public void setBackground(String _path) {
        try {
            InputStream is = this.getClass().getResource(
                    _path).openStream();
            BackgroundImage myBI= new BackgroundImage(new Image(is,32,32,false,true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                new BackgroundSize(0, 0, false, false, false, true));
            setBackground(new Background(myBI));
            
        }
        catch(IOException e) {
            e.printStackTrace();;
        }
    }
    
    protected void setAppearance() {
        area_right = new HBox();
        getChildren().add(area_right);
        setRightAnchor(area_right, 0d);
        setBottomAnchor(area_right, 0d);
        setPadding(new Insets(Utility.STANDARD_GAP));
        initButtons();
        setButtons();;
    }
    
    protected void setButtons(Button ... _buttons) {
        area_right.getChildren().clear();
        area_right.getChildren().addAll(_buttons);
        area_right.setSpacing(Utility.SHORT_GAP);
    }
    
    
    protected abstract void setButtonsEffects();
    protected abstract void initButtons();
    protected abstract void setButtons();
        

}
