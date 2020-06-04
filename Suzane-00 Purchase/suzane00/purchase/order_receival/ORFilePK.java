/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.order_receival;

import suzane00.inventory.PackedItemPK;
import suzane00.inventory.*;

/**
 *
 * @author Usere
 */
public class ORFilePK {
    
    private Long v_orderReceival ;
    private PackedItemPK v_packedItem ;
    
    public Long getOrderReceival ()
    {
        return v_orderReceival;
    }

    public void setOrderReceival (Long _or)
    {
        this.v_orderReceival = _or;
    }
    
    public PackedItemPK getPackedItem ()
    {
        return v_packedItem;
    }

    public void setPackedItem(PackedItemPK _item)
    {
        this.v_packedItem = _item;
    }
    
    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += v_orderReceival;
        hash += v_packedItem.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ORFilePK)) {
            return false;
        }
        
        ORFilePK other = (ORFilePK) object;
        if (this.v_orderReceival.equals(other.v_orderReceival))
            return false;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        
        return true;
    }
}
