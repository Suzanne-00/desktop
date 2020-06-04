/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.inventory;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import suzane00.global.PropertyObject;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;
        
    @TableGenerator(name = "product_gen", allocationSize = 1)
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "product_gen")
    private Long v_id;
    @Transient
    private SimpleLongProperty v_idProperty = new SimpleLongProperty();
    
    @ManyToOne
    @JoinColumn(name="brand_id")
    private Brand v_brand = null;
    @Transient
    private SimpleObjectProperty v_brandProperty = new SimpleObjectProperty();
    
    
    @ManyToMany
    @JoinTable(name="product_type_map",
        joinColumns=@JoinColumn(name="product_id"),
        inverseJoinColumns=@JoinColumn(name="type_id"))
    private List<ProductType> v_productTypes ;
    @Transient
    private ObservableList<SimpleObjectProperty> v_productTypesProperty = FXCollections.observableArrayList();
    
    
    @Column(name = "name")
    private String v_name;
    @Transient
    private SimpleStringProperty v_nameProperty = new SimpleStringProperty();
    
    @Column(name = "note")
    private String v_note;
    @Transient
    private SimpleStringProperty v_noteProperty = new SimpleStringProperty();
    
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
    
    public Brand getBrand() {
        return v_brand;
    }
    
    public void setBrand(Brand _brand) {
        v_brand = _brand;
        v_brandProperty.setValue(v_brand);
    }
    
    public SimpleObjectProperty brandProperty() {
        return v_brandProperty;
    }
    
    public List<ProductType> getProductTypes() {
        return v_productTypes;
    }
    
    public void setProductTypes( ObservableList<ProductType> _types) {
        v_productTypes = _types;
        for (ProductType _type : _types) {
            v_productTypesProperty.add(new SimpleObjectProperty<ProductType>(_type));
        }
    }
    
    public ObservableList<SimpleObjectProperty> productTypesProperty() {
        return v_productTypesProperty;
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
        v_brandProperty.setValue(v_brand);
        v_productTypesProperty = FXCollections.observableArrayList();
        for (ProductType type : v_productTypes) {
            v_productTypesProperty.add(new SimpleObjectProperty<ProductType>(type));
        }
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
        if (!(object instanceof Product)) {
            return false;
        }
        Product other = (Product) object;
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
