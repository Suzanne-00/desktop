/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.vendor_payment;

import suzane00.inventory.PackedItemPK;
import suzane00.inventory.*;
import suzane00.source.*;

/**
 *
 * @author Usere
 */
public class VPFilePK {
    
    private Long v_vendorPayment ;
    private Long v_vendorInvoice ;
    
    public Long getVendorPayment ()
    {
        return v_vendorPayment;
    }

    public void setVendorPayment (Long _vp)
    {
        this.v_vendorPayment = _vp;
    }
    
    public Long getVendorInvoice ()
    {
        return v_vendorInvoice;
    }

    public void setVendorInvoice(Long _vi)
    {
        this.v_vendorInvoice = _vi;
    }
    
    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += v_vendorPayment;
        hash += v_vendorInvoice.hashCode();
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VPFilePK)) {
            return false;
        }
        
        VPFilePK other = (VPFilePK) object;
        if (this.v_vendorPayment.equals(other.v_vendorPayment))
            return false;
        if (this.v_vendorInvoice.equals(other.v_vendorInvoice))
            return false;
        
        return true;
    }
}
