/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.purchase_order;

import suzane00.inventory.*;
import suzane00.inventory.PackedItemPK;
import suzane00.source.*;

/**
 *
 * @author Usere
 */
public class ItemPurchasePricePK {
    
    private Long v_supplier ;
    private PackedItemPK v_packedItem ;
    private double v_price;
    
    public Long getSupplier ()
    {
        return v_supplier;
    }

    public void setSupplier (Long _po)
    {
        this.v_supplier = _po;
    }
    
    public PackedItemPK getPackedItem ()
    {
        return v_packedItem;
    }

    public void setPackedItem(PackedItemPK _packedItem)
    {
        this.v_packedItem = _packedItem;
    }
    
    public void setPrice(double price) {
        v_price = price;
    }
    
    public double getPrice() {
        return  v_price;
    }
    
    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += v_supplier.hashCode();
        hash += v_packedItem.hashCode();
        hash += (new Double(v_price)).hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemPurchasePricePK)) {
            return false;
        }
        
        ItemPurchasePricePK other = (ItemPurchasePricePK) object;
        if (this.v_supplier.equals(other.v_supplier))
            return false;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        if ((new Double(v_price)).equals(other.v_packedItem))
            return false;
        
        return true;
    }
}
