/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_invoice;

import suzane00.sale.sales_order.*;
import suzane00.inventory.*;
import suzane00.source.*;

/**
 *
 * @author Usere
 */
public class SIMiscChargePK {
    
    private Long v_salesInvoice ;
    private String v_name ;
    
    public Long getSalesInvoice ()
    {
        return v_salesInvoice;
    }

    public void setSalesInvoice (Long _file)
    {
        this.v_salesInvoice = _file;
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
        hash += v_salesInvoice.hashCode();
        hash += v_name.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SIMiscChargePK)) {
            return false;
        }
        
        SIMiscChargePK other = (SIMiscChargePK) object;
        if (this.v_salesInvoice.equals(other.v_salesInvoice))
            return false;
        if (this.v_name.equals(other.v_name))
            return false;
        
        return true;
    }
}
