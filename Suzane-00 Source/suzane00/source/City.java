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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "city")
@NamedQueries (
    {
        @NamedQuery(name="City.findAll",
        query="SELECT c FROM City c"),
        @NamedQuery(name="City.findById",
        query="SELECT c FROM City c WHERE c.v_id = :id"),
        @NamedQuery(name="City.findByName",
        query="SELECT c FROM City c WHERE c.v_name = :name"),
        @NamedQuery(name="City.findByCountry",
        query="SELECT c FROM City c WHERE c.v_country = :country"),
    }
)
public class City implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    @TableGenerator(name = "city_gen", allocationSize = 1)
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "city_gen")
    private Long v_id;
    @Transient
    private SimpleLongProperty v_idProperty = new SimpleLongProperty();
    
    @ManyToOne
    @JoinColumn(name= "country_id")
    private Country v_country;
    @Transient
    private SimpleObjectProperty v_countryProperty = new SimpleObjectProperty();
    
    @Column(name = "name")
    private String v_name;
    @Transient
    private SimpleStringProperty v_nameProperty = new SimpleStringProperty();
    
    @Column(name = "note")
    private String v_note;
    @Transient
    private SimpleStringProperty v_noteProperty = new SimpleStringProperty();
    
    public City() {
        
    }
    
    public City( Country _country, String _name, String _note ) {
        setCountry(_country);
        setName(_name);
        setNote(_note);
    }
    
    public static ObservableList<City> getAllCities() {
        ObservableList<City> cities = FXCollections.observableArrayList(Environment.getEntityManager()
                                                   .createNamedQuery("City.findAll", City.class)
                                                   .getResultList());
        
        return PropertyObject.refreshProperies(cities);
    }
    
    public static ObservableList<City> getCitiesByAttributes(String _name, Country _country, String _note) {
        String query = "SELECT c FROM City c WHERE c.v_id != 0";
        if (_name != null && _name.length() > 0) {
            query += " AND c.v_name = " + "'" + _name + "'";
        }
        if (_country != null) {
            query += " AND c.v_country.v_id = " + "'" + _country.getId() + "'";
        }
        if (_note != null && _note.length() > 0) {
            query += " AND c.v_note = " + "'" + _note + "'";
        }
        ObservableList<City> cities = FXCollections.observableArrayList(Environment.getEntityManager()
                                                   .createQuery(query, City.class)
                                                   .getResultList());
        
        return PropertyObject.refreshProperies(cities);
    }
    
    public static ObservableList<City> getCitiesBasedOnCountry(Country _country) {
        ObservableList<City> cities = FXCollections.observableArrayList(Environment.getEntityManager()
                                                   .createNamedQuery("City.findByCountry", City.class)
                                                   .setParameter("country", _country)
                                                   .getResultList());
        
        return PropertyObject.refreshProperies(cities);
    }
    
    public static void saveCity(City _city) {
        Environment.getEntityManager().persist(_city);
    }

    public static void editCity(City _old, City _new) {
        _old.setName(_new.getName());
        _old.setCountry(_new.getCountry());
        _old.setNote(_new.getNote());
    }
    
    public static void deleteCity(City _city) {
        Environment.getEntityManager().remove(_city);
    }

    public Long getId() {
        return v_id;
    }

    public void setId(Long _id) {
        this.v_id = _id;
        v_idProperty.setValue(_id);
    }
    
    public SimpleLongProperty idProperty() {
       return v_idProperty;
    }
    
    public Country getCountry() {
         return v_country;
    }
     
    public void setCountry(Country _country) {
         v_country = _country;
         v_countryProperty.setValue(v_country);
    }
    
    public SimpleObjectProperty countryProperty() {
        return v_countryProperty;
    }
    
    public String getName() {
        return v_name;
    }

    public void setName(String _name) {
        this.v_name = _name;
        v_nameProperty.setValue(_name);
    }
    
    public SimpleStringProperty nameProperty() {
        return v_nameProperty;
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
        v_idProperty.setValue(v_id);
        v_countryProperty.setValue(v_country);
        v_nameProperty.setValue(v_name);
        v_noteProperty.setValue(v_note);
    } 

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (v_id != null ? v_id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the v_id fields are not set
        if (!(object instanceof City)) {
            return false;
        }
        City other = (City) object;
        if ((this.v_id == null && other.v_id != null) || (this.v_id != null && !this.v_id.equals(other.v_id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return v_name;
    }
    
}
