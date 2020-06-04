/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order;

import suzane00.inventory.*;
import suzane00.source.*;

/**
 *
 * @author Usere
 */
public class POFilePK {
    
    private Long v_purchaseOrder ;
    private PackedItemPK v_packedItem ;
    
    public Long getPurchaseOrder ()
    {
        return v_purchaseOrder;
    }

    public void setPurchaseOrder (Long _po)
    {
        this.v_purchaseOrder = _po;
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
        hash += v_purchaseOrder;
        hash += v_packedItem.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof POFilePK)) {
            return false;
        }
        
        POFilePK other = (POFilePK) object;
        if (this.v_purchaseOrder.equals(other.v_purchaseOrder))
            return false;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        
        return true;
    }
}
