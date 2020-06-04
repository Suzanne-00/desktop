/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory;

import suzane00.inventory.PackedItemPK;

/**
 *
 * @author Usere
 */
public class ItemPricePK {
    
    private PackedItemPK v_packedItem ;
    private Long v_sellPriceType ;
    
    public PackedItemPK getPackedItem ()
    {
        return v_packedItem;
    }

    public void setPackedItem(PackedItemPK _item)
    {
        this.v_packedItem = _item;
    }

    public Long getSellPriceType ()
    {
        return v_sellPriceType;
    }

    public void setSellPriceType (Long _type)
    {
        this.v_sellPriceType = _type;
    }
    
    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += v_packedItem.hashCode();
        hash += v_sellPriceType;
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ItemPricePK)) {
            return false;
        }
        ItemPricePK other = (ItemPricePK) object;
        if (this.v_packedItem.equals(other.v_packedItem))
            return false;
        if (this.v_sellPriceType.equals(other.v_sellPriceType))
            return false;
        return true;
    }
}
