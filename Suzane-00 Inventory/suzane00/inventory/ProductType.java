/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory;

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
import suzane00.global.Environment;
import suzane00.global.PropertyObject;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "product_type")
@NamedQueries (
    {
        @NamedQuery(name="ProductType.findAll",
        query="SELECT p FROM ProductType p"),
        @NamedQuery(name="ProductType.findById",
        query="SELECT p FROM ProductType p WHERE p.v_id = :id"),
        @NamedQuery(name="ProductType.findByName",
        query="SELECT p FROM ProductType p WHERE p.v_name = :name"),
    }
)
public class ProductType implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    @TableGenerator(name = "product_type_gen", allocationSize = 1)
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "product_type_gen")
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
    
    public ProductType() {
        
    }
    
    public ProductType(String _name, String _note) {
        setName(_name);
        setNote(_note);
    }
    
    public static ObservableList<ProductType> getAllTypes() {
        ObservableList<ProductType> types = FXCollections.observableArrayList(Environment.getEntityManager()
                                                         .createNamedQuery("ProductType.findAll", ProductType.class)
                                                          .getResultList());
        
        return PropertyObject.refreshProperies(types);
    }

    public static ObservableList<ProductType> getTypesByAttributes(String _name, String _note) {
        String query = "SELECT p FROM ProductType p WHERE p.v_id != 0";
        if (_name != null && _name.length() > 0) {
            query += " AND p.v_name = " + "'" + _name + "'";
        }
        if (_note != null && _note.length() > 0) {
            query += " AND p.v_note = " + "'" + _note + "'";
        }
        ObservableList<ProductType> types = FXCollections.observableArrayList(Environment.getEntityManager()
                                                         .createQuery(query, ProductType.class)
                                                         .getResultList());
        
        return PropertyObject.refreshProperies(types);
    }
    
    public static void saveType(ProductType _type) {
        Environment.getEntityManager().persist(_type);
    }
    
    public static void editType(ProductType _old, ProductType _new) {
        _old.setName(_new.getName());
        _old.setNote(_new.getNote());
    }
    
    public static void deleteType(ProductType _type) {
        Environment.getEntityManager().remove(_type);
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
        if (!(object instanceof ProductType)) {
            return false;
        }
        ProductType other = (ProductType) object;
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
