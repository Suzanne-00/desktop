package suzane00.inventory.custom_ui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import suzane00.inventory.Unit;


public class NumberTextField extends TextField
{
    SimpleDoubleProperty v_value = new SimpleDoubleProperty();
    
    public NumberTextField() {
        super();
        v_value.setValue(null);
        setOnAction(
                e -> parse() 
        );
        
        focusedProperty().addListener(
            (ObservableValue<? extends Boolean> arg0, Boolean _old,Boolean _new) -> {
                if (!_new) {
                   parse();
                }
            }
        );
    }
    
    public Double getValue() {
        return v_value.getValue();
    }
    
    public void setValue(Double _value) {
        v_value.setValue(_value);
        setText(String.valueOf(v_value.getValue()));
    }
    
    public SimpleDoubleProperty valueProperty() {
        return v_value;
    }
    
    @Override
    public void clear() {
        v_value.setValue(null);
        setText("");
    }

    private void parse() {
        
        try {
            if(!getText().equals("")) 
                v_value.setValue(Double.valueOf(getText()));
            else
                v_value.setValue(null);
            
            setText(getText());
        }
        catch( NumberFormatException e) {
            v_value.setValue(null);
            setText("");
        }
    }
    
//    class NumberTextFieldKeyEventHandler implements EventHandler<KeyEvent> {
//
//        public void handle(KeyEvent event) {
//            parse();
//        }
//        
//    }
}