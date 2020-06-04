/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_retur;

import suzane00.inventory.PackedItemPK;
import suzane00.inventory.*;

/**
 *
 * @author Usere
 */
public class PRFilePK {
    
    private Long v_purchaseRetur ;
    private PackedItemPK v_packedItem ;
    
    public Long getPurchaseRetur ()
    {
        return v_purchaseRetur;
    }

    public void setPurchaseRetur (Long _or)
    {
        this.v_purchaseRetur = _or;
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
        hash += v_purchaseRetur;
        hash += v_packedItem.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PRFilePK)) {
            return false;
        }
        
        PRFilePK other = (PRFilePK) object;
        if (this.v_purchaseRetur.equals(other.v_purchaseRetur))
            return false;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        
        return true;
    }
}
