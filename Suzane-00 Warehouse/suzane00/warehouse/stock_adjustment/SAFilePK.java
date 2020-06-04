/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.warehouse.stock_adjustment;

import suzane00.inventory.PackedItemPK;
import suzane00.inventory.*;

/**
 *
 * @author Usere
 */
public class SAFilePK {
    
    private Long v_stockAdjustment ;
    private PackedItemPK v_packedItem ;
    
    public Long getStockAdjustment ()
    {
        return v_stockAdjustment;
    }

    public void setStockAdjustment (Long _sa)
    {
        this.v_stockAdjustment = _sa;
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
        hash += v_stockAdjustment;
        hash += v_packedItem.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SAFilePK)) {
            return false;
        }
        
        SAFilePK other = (SAFilePK) object;
        if (this.v_stockAdjustment.equals(other.v_stockAdjustment))
            return false;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        
        return true;
    }
}
