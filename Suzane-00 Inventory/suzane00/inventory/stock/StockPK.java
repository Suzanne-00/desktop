/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory.stock;

import suzane00.inventory.*;
import suzane00.inventory.PackedItemPK;

/**
 *
 * @author Usere
 */
public class StockPK {
    
    private PackedItemPK v_packedItem ;
    private Long v_area ;
    
    public PackedItemPK getPackedItem ()
    {
        return v_packedItem;
    }

    public void setPackedItem(PackedItemPK _item)
    {
        this.v_packedItem = _item;
    }

    public Long getArea ()
    {
        return v_area;
    }

    public void setArea (Long _area)
    {
        this.v_area = _area;
    }
    
    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += v_packedItem.hashCode();
        hash += v_area;
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StockPK)) {
            return false;
        }
        StockPK other = (StockPK) object;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        if (this.v_area.equals(other.v_area))
            return false;
        return true;
    }
}
