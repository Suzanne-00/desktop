/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_order;

import suzane00.inventory.*;
import suzane00.source.*;

/**
 *
 * @author Usere
 */
public class SODiscountPK {
    
    private Long v_salesOrder ;
    private String v_name ;
    
    public Long getPurchaseOrder ()
    {
        return v_salesOrder;
    }

    public void setPurchaseOrder (Long _po)
    {
        this.v_salesOrder = _po;
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
        hash += v_salesOrder.hashCode();
        hash += v_name.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SODiscountPK)) {
            return false;
        }
        
        SODiscountPK other = (SODiscountPK) object;
        if (this.v_salesOrder.equals(other.v_salesOrder))
            return false;
        if (this.v_name.equals(other.v_name))
            return false;
        
        return true;
    }
}
