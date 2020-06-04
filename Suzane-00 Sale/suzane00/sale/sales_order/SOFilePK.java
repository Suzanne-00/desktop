/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_order;

import suzane00.inventory.PackedItemPK;
import suzane00.inventory.*;
import suzane00.source.*;

/**
 *
 * @author Usere
 */
public class SOFilePK {
    
    private Long v_salesOrder ;
    private PackedItemPK v_packedItem ;
    
    public Long getSalesOrder ()
    {
        return v_salesOrder;
    }

    public void setSalesOrder (Long _po)
    {
        this.v_salesOrder = _po;
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
        hash += v_salesOrder;
        hash += v_packedItem.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SOFilePK)) {
            return false;
        }
        
        SOFilePK other = (SOFilePK) object;
        if (this.v_salesOrder.equals(other.v_salesOrder))
            return false;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        
        return true;
    }
}
