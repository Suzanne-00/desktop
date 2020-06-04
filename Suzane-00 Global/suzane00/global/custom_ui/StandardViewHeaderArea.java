/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.global.custom_ui;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;

/**
 *
 * @author Usere
 */
public class StandardViewHeaderArea extends VBox {
    
    protected Background v_buttonBackground;
    protected String v_normalStyle = "-fx-background-color: rgba(0,102,102,1); " +
            "-fx-text-fill: rgba(255,255,255,1); -fx-font-size: 15; " +
            "-fx-border-radius: 20 20 20 20; -fx-background-radius: 20 20 20 20;" ;
    protected String v_clickedStyle = "-fx-background-color: rgba(128,128,128,1); " +
            "-fx-text-fill: rgba(255,255,255,1); -fx-font-size: 15;";
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */
    
    /* level 1 area */
    public AnchorPane area_bottomHeader;
    
    /* level 2 area */
    
    // area_bottomHeader
    public Button btn_search = new Button("Search");
    
    /*------------------------------------------------- GUI COMPONENTS ------------------------------------------ */

    public StandardViewHeaderArea() {
        setAppearance() ;
        setButtonsEffects();
    }
    
    public void setContent(Node _content) {
        getChildren().clear();
        getChildren().addAll(_content, area_bottomHeader);
    }
    
    public void setButtonSearchActionPerformed(EventHandler<ActionEvent> _handler) {
        btn_search.setOnAction(_handler);
    }
    
    protected void setAppearance() {
        area_bottomHeader = new AnchorPane(btn_search);
        AnchorPane.setRightAnchor(btn_search, 0d);
        AnchorPane.setBottomAnchor(btn_search, 0d);
    }
    
    protected void setButtonsEffects() {
        
        btn_search.setStyle(v_normalStyle);
        btn_search.setOnMousePressed(e -> btn_search.setStyle(v_clickedStyle));
        btn_search.setOnMouseReleased(e -> btn_search.setStyle(v_normalStyle));
        btn_search.setOnMouseEntered(e -> btn_search.setEffect(new DropShadow()));
        btn_search.setOnMouseExited(e -> btn_search.setEffect(null));
    }
}
