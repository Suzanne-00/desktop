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
public class POFileDiscountPK {
    
    private POFilePK v_poFile ;
    private String v_name ;
    
    public POFilePK getPurchaseOrder ()
    {
        return v_poFile;
    }

    public void setPurchaseOrder (POFilePK _po)
    {
        this.v_poFile = _po;
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
        hash += v_poFile.hashCode();
        hash += v_name.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof POFileDiscountPK)) {
            return false;
        }
        
        POFileDiscountPK other = (POFileDiscountPK) object;
        if (this.v_poFile.equals(other.v_poFile))
            return false;
        if (this.v_name.equals(other.v_name))
            return false;
        
        return true;
    }
}
