/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.delivery_order;

import suzane00.sale.stock_reservation.*;
import suzane00.inventory.PackedItemPK;
import suzane00.inventory.*;

/**
 *
 * @author Usere
 */
public class DOFilePK {
    
    private Long v_deliveryOrder ;
    private PackedItemPK v_packedItem ;
    
    public Long getDeliveryOrder ()
    {
        return v_deliveryOrder;
    }

    public void setDeliveryOrder (Long _or)
    {
        this.v_deliveryOrder = _or;
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
        hash += v_deliveryOrder;
        hash += v_packedItem.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DOFilePK)) {
            return false;
        }
        
        DOFilePK other = (DOFilePK) object;
        if (this.v_deliveryOrder.equals(other.v_deliveryOrder))
            return false;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        
        return true;
    }
}
