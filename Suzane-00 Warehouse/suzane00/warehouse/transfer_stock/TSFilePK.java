/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.warehouse.transfer_stock;

import suzane00.inventory.PackedItemPK;
import suzane00.inventory.*;

/**
 *
 * @author Usere
 */
public class TSFilePK {
    
    private Long v_transferStock ;
    private PackedItemPK v_packedItem ;
    
    public Long getTransferStock ()
    {
        return v_transferStock;
    }

    public void setTransferStock (Long _sa)
    {
        this.v_transferStock = _sa;
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
        hash += v_transferStock;
        hash += v_packedItem.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TSFilePK)) {
            return false;
        }
        
        TSFilePK other = (TSFilePK) object;
        if (this.v_transferStock.equals(other.v_transferStock))
            return false;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        
        return true;
    }
}
