/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.vendor_invoice;

import suzane00.inventory.PackedItemPK;
import suzane00.inventory.*;
import suzane00.source.*;

/**
 *
 * @author Usere
 */
public class VIFilePK {
    
    private Long v_vendorInvoice ;
    private PackedItemPK v_packedItem ;
    
    public Long getVendorInvoice ()
    {
        return v_vendorInvoice;
    }

    public void setVendorInvoice (Long _si)
    {
        this.v_vendorInvoice = _si;
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
        hash += v_vendorInvoice;
        hash += v_packedItem.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VIFilePK)) {
            return false;
        }
        
        VIFilePK other = (VIFilePK) object;
        if (this.v_vendorInvoice.equals(other.v_vendorInvoice))
            return false;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        
        return true;
    }
}
