/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.global;


/**
 *
 * @author Usere
 */
public interface Observable {
    public void addObserver(Observer _obs);
    public void notifyObservers();
}
