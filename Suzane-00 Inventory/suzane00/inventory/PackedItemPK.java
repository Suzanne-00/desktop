/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory;


/**
 *
 * @author Usere
 */
public class PackedItemPK {
    
    private Long v_item ;
    private Long v_unit ;
    
    public PackedItemPK() {
        
    }
    
    public PackedItemPK(Long _item, Long _unit) {
        v_item = _item;
        v_unit = _unit;
    }
    
    public Long getItem ()
    {
        return v_item;
    }

    public void setItem(long _item)
    {
        this.v_item = _item;
    }

    public Long getUnit ()
    {
        return v_unit;
    }

    public void setStreetName (Long _unit)
    {
        this.v_unit = _unit;
    }
    
    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += v_item;
        hash += v_unit;
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PackedItemPK)) {
            return false;
        }
        PackedItemPK other = (PackedItemPK) object;
        if (this.v_item.equals(other.v_item))
            return false;
        if (this.v_unit.equals(other.v_unit))
            return false;
        return true;
    }
}
