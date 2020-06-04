/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.global;

import javafx.collections.ObservableList;

/**
 *
 * @author Usere
 */
public interface PropertyObject {
    public void refreshProperty();
    public static <T extends PropertyObject> ObservableList<T> refreshProperies(ObservableList<T> _objects) {
        for (PropertyObject _object : _objects) {
            _object.refreshProperty();
        }
        
        return _objects;
    }
}
