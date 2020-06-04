/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.stock_reservation;

import suzane00.inventory.PackedItemPK;
import suzane00.inventory.*;

/**
 *
 * @author Usere
 */
public class SRFilePK {
    
    private Long v_stockReservation ;
    private PackedItemPK v_packedItem ;
    
    public Long getStockReservation ()
    {
        return v_stockReservation;
    }

    public void setStockReservation (Long _or)
    {
        this.v_stockReservation = _or;
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
        hash += v_stockReservation;
        hash += v_packedItem.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SRFilePK)) {
            return false;
        }
        
        SRFilePK other = (SRFilePK) object;
        if (this.v_stockReservation.equals(other.v_stockReservation))
            return false;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        
        return true;
    }
}
