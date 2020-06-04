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
public class POMiscChargePK {
    
    private Long v_purchaseOrder ;
    private String v_name ;
    
    public Long getPOFile ()
    {
        return v_purchaseOrder;
    }

    public void setPurchaseOrder (Long _po)
    {
        this.v_purchaseOrder = _po;
    }
    
    public String getName ()
    {
        return v_name;
    }

    public void setName(String _name)
    {
        this.v_name = _name;
    }
    
    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += v_purchaseOrder.hashCode();
        hash += v_name.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof POMiscChargePK)) {
            return false;
        }
        
        POMiscChargePK other = (POMiscChargePK) object;
        if (this.v_purchaseOrder.equals(other.v_purchaseOrder))
            return false;
        if (this.v_name.equals(other.v_name))
            return false;
        
        return true;
    }
}
