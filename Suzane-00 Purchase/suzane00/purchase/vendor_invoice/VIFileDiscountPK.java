/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.vendor_invoice;

import suzane00.inventory.*;
import suzane00.source.*;

/**
 *
 * @author Usere
 */
public class VIFileDiscountPK {
    
    private VIFilePK v_file ;
    private String v_name ;
    
    public VIFilePK getFile ()
    {
        return v_file;
    }

    public void setFile (VIFilePK _file)
    {
        this.v_file = _file;
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
        hash += v_file.hashCode();
        hash += v_name.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VIFileDiscountPK)) {
            return false;
        }
        
        VIFileDiscountPK other = (VIFileDiscountPK) object;
        if (this.v_file.equals(other.v_file))
            return false;
        if (this.v_name.equals(other.v_name))
            return false;
        
        return true;
    }
}
