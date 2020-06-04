/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source;

import java.io.Serializable;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "address")
@IdClass(AddressPK.class)
@NamedQueries (
    {
        @NamedQuery(name="Address.findAll",
        query="SELECT a FROM Address a"),
        @NamedQuery(name="Address.findById",
        query="SELECT a FROM Address a WHERE a.v_city = :city AND a.v_streetName = :streetName"),
        @NamedQuery(name="Address.findBySource",
        query="SELECT a FROM Address a WHERE a.v_source = :source")
    }
)
public class Address implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;
    
    public static ObservableList<Address> getAddressesBySource(Source _source) {
        ObservableList<Address> addresses = FXCollections.observableArrayList(Environment.getEntityManager()
                                                   .createNamedQuery("Address.findBySource", Address.class)
                                                   .setParameter("source", _source)
                                                   .getResultList());
        
        return PropertyObject.refreshProperies(addresses);
    }
 
    @Id
    @ManyToOne
    @JoinColumn(name= "city_id")
    private City v_city;
    @Transient
    private SimpleObjectProperty v_cityProperty = new SimpleObjectProperty();
    
    @Id
    @Column(name ="street_name")
    private String v_streetName;
    @Transient
    private SimpleStringProperty v_streetNameProperty = new SimpleStringProperty();
    
    @ManyToOne
    @JoinColumn(name= "source_id", referencedColumnName = "id")
    private Source v_source;
    @Transient
    private SimpleObjectProperty v_sourceProperty = new SimpleObjectProperty();

    @Column(name ="phone_number")
    private String v_phoneNumber;
    @Transient
    private SimpleStringProperty v_phoneNumberProperty = new SimpleStringProperty();
    
    @Column(name ="alt_phone_number")
    private String v_altPhoneNumber;
    @Transient
    private SimpleStringProperty v_altPhoneNumberProperty = new SimpleStringProperty();
    
    @Column(name ="email")
    private String v_email;
    @Transient
    private SimpleStringProperty v_emailProperty = new SimpleStringProperty();
    
    @Column(name ="alt_email")
    private String v_altEmail;
    @Transient
    private SimpleStringProperty v_altEmailProperty = new SimpleStringProperty();
    
    @Column(name ="postal_code")
    private String v_postalCode;
    @Transient
    private SimpleStringProperty v_postalCodeProperty = new SimpleStringProperty();
    
    @Column(name ="fax_number")
    private String v_faxNumber;
    @Transient
    private SimpleStringProperty v_faxNumberProperty = new SimpleStringProperty();
    
    @Column(name = "note")
    private String v_note;
    @Transient
    private SimpleStringProperty v_noteProperty = new SimpleStringProperty();
    
    public static void deleteAddress(Address _adrress) {
        Environment.getEntityManager().remove(_adrress);
    }
    
    public City getCity() {
         return v_city;
    }
     
    public void setCity(City _city) {
         v_city = _city;
         v_cityProperty.setValue(v_city);
    }
    
    public SimpleObjectProperty cityProperty() {
        return v_cityProperty;
    }
    
    public Source getSource() {
         return v_source;
    }
     
    public void setSource(Source _source) {
         v_source = _source;
         v_sourceProperty.setValue(v_source);
    }
    
    public SimpleObjectProperty sourceProperty() {
        return v_sourceProperty;
    }
    
    public String getStreetName() {
        return v_streetName;
    }

    public void setStreetName(String _name) {
        this.v_streetName = _name;
        v_streetNameProperty.setValue(_name);
    }
    
    public SimpleStringProperty streetNameProperty() {
       return v_streetNameProperty;
    }
     
    public String getPhoneNumber() {
        return v_phoneNumber;
    }

    public void setPhoneNumber(String _number) {
        this.v_phoneNumber = _number;
        v_phoneNumberProperty.setValue(_number);
    }
    
    public SimpleStringProperty phoneNumberProperty() {
        return v_phoneNumberProperty;
    }
     
    public String getAltPhoneNumber() {
        return v_altPhoneNumber;
    }

    public void setAltPhoneNumber(String _number) {
        this.v_altPhoneNumber = _number;
        v_altPhoneNumberProperty.setValue(_number);
    }
    
    public SimpleStringProperty altPhoneNumberProperty() {
        return v_altPhoneNumberProperty;
    }
     
    public String getEmail() {
        return v_email;
    }

    public void setEmail(String _email) {
        this.v_email = _email;
        v_emailProperty.setValue(_email);
    }
    
     public SimpleStringProperty emailProperty() {
        return v_emailProperty;
    }
     
     public String getAltEmail() {
        return v_altEmail;
    }

    public void setAltEmail(String _email) {
        this.v_altEmail = _email;
        v_altEmailProperty.setValue(_email);
    }
    
    public SimpleStringProperty altEmailProperty() {
        return v_altEmailProperty;
    }
     
    public String getPostalCode() {
        return v_postalCode;
    }

    public void setPostalCode(String _code) {
        this.v_postalCode = _code;
        v_postalCodeProperty.setValue(_code);
    }
    
    public SimpleStringProperty postalCodeProperty() {
        return v_postalCodeProperty;
    }
     
    public String getFaxNumber() {
        return v_faxNumber;
    }

    public void setFaxNumber(String _number) {
        this.v_faxNumber = _number;
        v_faxNumberProperty.setValue(_number);
    }
    
    public SimpleStringProperty faxNumberPropperty() {
        return v_faxNumberProperty;
    } 
     
    public String getNote() {
        return v_note;
    }

    public void setNote(String _note) {
        this.v_note = _note;
        v_noteProperty.setValue(_note);
    }
    
     public SimpleStringProperty noteProperty() {
       return v_noteProperty;
    }
     
    @Override
     public void refreshProperty() {
         v_sourceProperty.setValue(v_source);
         v_cityProperty.setValue(v_city);
         v_streetNameProperty.setValue(v_streetName);
         v_phoneNumberProperty.setValue(v_phoneNumber);
         v_altPhoneNumberProperty.setValue(v_altPhoneNumber);
         v_emailProperty.setValue(v_email);
         v_altEmailProperty.setValue(v_altEmail);
         v_postalCodeProperty.setValue(v_postalCode);
         v_faxNumberProperty.setValue(v_faxNumber);
         v_noteProperty.setValue(v_note);
     }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_city.getId() != null ? v_city.getId().hashCode() : 0);
        hash += (v_streetName != null ? v_streetName.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Address)) {
            return false;
        }
        Address other = (Address) object;
//        if (((v_city.getId() == null && other.getCity().getId() != null) || (v_streetName == null && other.getStreetName() != null)) ||
//                ((v_city.getId() != null && !v_city.getId().equals(other.getCity().getId())) || (v_streetName != null && !v_streetName.equals(other.getStreetName())) )) {
          
        if (! (v_city.equals(other.v_city) && v_streetName.equals(other.v_streetName)))
            return false;
        
        return true;
    }

    @Override
    public String toString() {
        return v_city + " " + v_streetName;
    }
    
}
