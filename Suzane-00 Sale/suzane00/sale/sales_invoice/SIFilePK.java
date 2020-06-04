/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_invoice;

import suzane00.sale.sales_order.*;
import suzane00.inventory.PackedItemPK;
import suzane00.inventory.*;
import suzane00.source.*;

/**
 *
 * @author Usere
 */
public class SIFilePK {
    
    private Long v_salesInvoice ;
    private PackedItemPK v_packedItem ;
    
    public Long getSalesInvoice ()
    {
        return v_salesInvoice;
    }

    public void setSalesInvoice (Long _si)
    {
        this.v_salesInvoice = _si;
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
        hash += v_salesInvoice;
        hash += v_packedItem.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SIFilePK)) {
            return false;
        }
        
        SIFilePK other = (SIFilePK) object;
        if (this.v_salesInvoice.equals(other.v_salesInvoice))
            return false;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        
        return true;
    }
}
