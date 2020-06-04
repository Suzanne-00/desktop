/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source;

import java.io.Serializable;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import suzane00.global.Controller;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "country")
@NamedQueries({
    @NamedQuery(name="Country.findAll",
    query="SELECT c FROM Country c"),
    @NamedQuery(name="Country.findById",
    query="SELECT c FROM Country c WHERE c.v_id = :id"),
    @NamedQuery(name="Country.findByName",
    query="SELECT c FROM Country c WHERE c.v_name = :name")
    }
)
public class Country implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    @TableGenerator(name = "country_gen", allocationSize = 1)
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "country_gen")
    private Long v_id;
    @Transient
    private SimpleLongProperty v_idProperty = new SimpleLongProperty();
    
    @Column(name = "name")
    private String v_name;
    @Transient
    private SimpleStringProperty v_nameProperty = new SimpleStringProperty();
    
    @Column(name = "code")
    private String v_code;
    @Transient
    private SimpleStringProperty v_codeProperty = new SimpleStringProperty();
    
    @Column(name = "note")
    private String v_note;
    @Transient
    private SimpleStringProperty v_noteProperty = new SimpleStringProperty();
    
    public Country() {
        
    }
    
    public Country( String _name, String _code, String _note ) {
        setName(_name);
        setCode(_code);
        setNote(_note);
    }
    
    public static ObservableList<Country> getAllCountries() {
        ObservableList<Country> countries = FXCollections.observableArrayList(Environment.getEntityManager()
                                                         .createNamedQuery("Country.findAll", Country.class)
                                                         .getResultList());
        
        return PropertyObject.refreshProperies(countries);
    }
    
    public static ObservableList<Country> getCountriesByAttributes(String _name, String _code, String _note) {
        String query = "SELECT c FROM Country c WHERE c.v_id != 0";
        if (_name != null && _name.length() > 0) {
            query += " AND c.v_name = " + "'" + _name + "'";
        }
        if (_code != null && _code.length() > 0) {
            query += " AND c.v_code = " + "'" + _code + "'";
        }
        if (_note != null && _note.length() > 0) {
            query += " AND c.v_note = " + "'" + _note + "'";
        }
        ObservableList<Country> countries = FXCollections.observableArrayList(Environment.getEntityManager()
                                                         .createQuery(query, Country.class)
                                                         .getResultList());
        
        return PropertyObject.refreshProperies(countries);
    }
    
    public static void saveCountry(Country _country) {
        Environment.getEntityManager().persist(_country);
    }
    
    public static void editCountry(Country _old, Country _new) {
        _old.setName(_new.getName());
        _old.setCode(_new.getCode());
        _old.setNote(_new.getNote());
    }
    
    public static void deleteCountry(Country _country) {
        Environment.getEntityManager().remove(_country);
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
     
    public String getCode() {
        return v_code;
    }

    public void setCode(String _code) {
        this.v_code = _code;
        v_codeProperty.setValue(_code);
    }
    
    public SimpleStringProperty codeProperty() {
        return v_codeProperty;
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
        if (!(object instanceof Country)) {
            return false;
        }
        Country other = (Country) object;
        if ((this.v_id == null && other.v_id != null) || (this.v_id != null && !this.v_id.equals(other.v_id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return v_name ;
    }
    
}
