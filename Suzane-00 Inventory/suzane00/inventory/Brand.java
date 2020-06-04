/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "brand")
@NamedQueries (
    {
        @NamedQuery(name="Brand.findAll",
        query="SELECT b FROM Brand b"),
        @NamedQuery(name="Brand.findById",
        query="SELECT b FROM Brand b WHERE b.v_id = :id"),
        @NamedQuery(name="Brand.findByName",
        query="SELECT b FROM Brand b WHERE b.v_name = :name"),
    }
)
public class Brand implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    @TableGenerator(name = "brand_gen", allocationSize = 1)
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "brand_gen")
    private Long v_id;
    @Transient
    private SimpleLongProperty v_idProperty = new SimpleLongProperty();
    
    @Column(name = "name")
    private String v_name;
    @Transient
    private SimpleStringProperty v_nameProperty = new SimpleStringProperty();
    
    @Column(name = "note")
    private String v_note;
    @Transient
    private SimpleStringProperty v_noteProperty = new SimpleStringProperty();
    
    public Brand() {
        
    }
    
    public Brand(String _name, String _note) {
        setName(_name);
        setNote(_note);
    }
    
    public static ObservableList<Brand> getAllBrands() {
        ObservableList<Brand> brands = FXCollections.observableArrayList(Environment.getEntityManager()
                                                    .createNamedQuery("Brand.findAll", Brand.class)
                                                    .getResultList());
        
        return PropertyObject.refreshProperies(brands);
    }
    
    public static ObservableList<Brand> getBrandsByAttributes(String _name, String _note) {
        String query = "SELECT b FROM Brand b WHERE b.v_id != 0";
        if (_name != null && _name.length() > 0) {
            query += " AND b.v_name = " + "'" + _name + "'";
        }
        if (_note != null && _note.length() > 0) {
            query += " AND b.v_note = " + "'" + _note + "'";
        }
        ObservableList<Brand> brands = FXCollections.observableArrayList(Environment.getEntityManager()
                                                    .createQuery(query, Brand.class)
                                                    .getResultList());
        
        return PropertyObject.refreshProperies(brands);
    }
    
    public static void saveBrand(Brand _brand) {
        Environment.getEntityManager().persist(_brand);
    }
    
    public static void editBrand(Brand _old, Brand _new) {
        _old.setName(_new.getName());
        _old.setNote(_new.getNote());
    }
    
    public static void deleteBrand(Brand _brand) {
        Environment.getEntityManager().remove(_brand);
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
        if (!(object instanceof Brand)) {
            return false;
        }
        Brand other = (Brand) object;
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
