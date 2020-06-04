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
public interface SimpleObject {
    
    public abstract void SimpleObject(String _name, String _note);
    public abstract void setId(Long _id);
    public abstract Long getId();
    public abstract void setName(String _text);
    public abstract String getName();
    public abstract void setNote(String _note);
    public abstract String getNote();
}
