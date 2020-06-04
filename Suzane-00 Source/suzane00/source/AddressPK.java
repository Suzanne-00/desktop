/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source;

/**
 *
 * @author Usere
 */
public class AddressPK {
    
    private Long v_city ;
    private String v_streetName ;
    
    public Long getCity ()
    {
        return v_city;
    }

    public void setCity (long _city)
    {
        this.v_city = _city;
    }

    public String getStreetName ()
    {
        return v_streetName;
    }

    public void setStreetName (String _name)
    {
        this.v_streetName = _name;
    }
    
    @Override
    public int hashCode ()
    {
        int hash = 0;
        hash += v_city;
        hash += v_streetName.hashCode() ;
        return hash;
    }

    @Override
    public boolean equals (Object object)
    {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AddressPK)) {
            return false;
        }
        AddressPK other = (AddressPK) object;
        if (this.v_city.equals(other.v_city))
            return false;
        if (this.v_streetName.equals(other.v_streetName))
            return false;
        return true;
    }
}
