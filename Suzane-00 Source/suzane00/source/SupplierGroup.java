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
@Table(name = "supplier_group")
@NamedQueries({
    @NamedQuery(name="SupplierGroup.findAll",
    query="SELECT g FROM SupplierGroup g"),
    @NamedQuery(name="SupplierGroup.findById",
    query="SELECT g FROM SupplierGroup g WHERE g.v_id = :id"),
    @NamedQuery(name="SupplierGroup.findByName",
    query="SELECT g FROM SupplierGroup g WHERE g.v_name = :name")
    }
)
public class SupplierGroup implements Serializable, PropertyObject {
    private static final long serialVersionUID = 1L;

    @TableGenerator(name = "supplier_group_gen", allocationSize = 1)
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "supplier_group_gen")
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
    
    public SupplierGroup() {
        
    }
    
    public SupplierGroup(String _name, String _note) {
        setName(_name);
        setNote(_note);
    }
    
    public static ObservableList<SupplierGroup> getAllGroups() {
        ObservableList<SupplierGroup> groups = FXCollections.observableArrayList(Environment.getEntityManager()
                                                            .createNamedQuery("SupplierGroup.findAll", SupplierGroup.class)
                                                            .getResultList());
        
        return PropertyObject.refreshProperies(groups);
    }
    
    public static ObservableList<SupplierGroup> getGroupsByAttributes(String _name, String _note) {
        String query = "SELECT g FROM SupplierGroup g WHERE g.v_id != 0";
        if (_name != null && _name.length() > 0) {
            query += " AND g.v_name = " + "'" + _name + "'";
        }
        if (_note != null && _note.length() > 0) {
            query += " AND g.v_note = " + "'" + _note + "'";
        }
        ObservableList<SupplierGroup> groups = FXCollections.observableArrayList(Environment.getEntityManager()
                                                            .createQuery(query, SupplierGroup.class)
                                                            .getResultList());
        
        return PropertyObject.refreshProperies(groups);
    }
    
    public static void saveGroup(SupplierGroup _group) {
        if (Environment.getEntityManager() == null) {
            Controller.setEntityManager(Environment.getEntityManager());
        }
        Environment.getEntityManager().persist(_group);
    }
    
    public static void editGroup(SupplierGroup _old, SupplierGroup _new) {
        _old.setName(_new.getName());
        _old.setNote(_new.getNote());
    }
    
    public static void deleteGroup(SupplierGroup _group) {
        Environment.getEntityManager().remove(_group);
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
        if (!(object instanceof SupplierGroup)) {
            return false;
        }
        SupplierGroup other = (SupplierGroup) object;
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
