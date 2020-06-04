/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_payment;

import suzane00.sale.sales_invoice.*;
import suzane00.sale.sales_order.*;
import suzane00.inventory.PackedItemPK;
import suzane00.inventory.*;
import suzane00.source.*;

/**
 *
 * @author Usere
 */
public class SPFilePK {
    
    private Long v_salesPayment ;
    private Long v_salesInvoice ;
    
    public Long getSalesPayment ()
    {
        return v_salesPayment;
    }

    public void setSalesPayment (Long _sp)
    {
        this.v_salesPayment = _sp;
    }
    
    public Long getSalesInvoice ()
    {
        return v_salesInvoice;
    }

    public void setSalesInvoice(Long _si)
    {
        this.v_salesInvoice = _si;
    }
    
    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += v_salesPayment;
        hash += v_salesInvoice.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SPFilePK)) {
            return false;
        }
        
        SPFilePK other = (SPFilePK) object;
        if (this.v_salesPayment.equals(other.v_salesPayment))
            return false;
        if (this.v_salesInvoice.equals(other.v_salesInvoice))
            return false;
        
        return true;
    }
}
