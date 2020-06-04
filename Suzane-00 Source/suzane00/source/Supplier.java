/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.source;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.Controller;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "supplier")
@NamedQueries(
    {
        @NamedQuery(name="Supplier.findAll",
        query="SELECT s FROM Supplier s"),
        @NamedQuery(name="Supplier.findById",
        query="SELECT s FROM Supplier s WHERE s.v_id = :id"),
        @NamedQuery(name="Supplier.findByName",
        query="SELECT s FROM Supplier s WHERE s.v_name = :name"),
        @NamedQuery(name="Supplier.findByGroup",
        query="SELECT s FROM Supplier s WHERE s.v_supplierGroup = :group"),
    }
)
public class Supplier extends Source implements Serializable {
    
    private static final long serialVersionUID = 1L;

    
    @ManyToOne
    @JoinColumn(name = "supplier_group_id")
    private SupplierGroup v_supplierGroup;
    @Transient
    private SimpleObjectProperty v_supplierGroupProperty = new SimpleObjectProperty();
    
    public static ObservableList<Supplier> getAllSuppliers() {
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList(Environment.getEntityManager()
                                                          .createNamedQuery("Supplier.findAll", Supplier.class)
                                                          .getResultList());
        
        return PropertyObject.refreshProperies(suppliers);
    }
    
    public static ObservableList<Supplier> getSuppliersByAttributes(String _name, SupplierGroup _group, String _note) {
        String query = "SELECT s FROM Supplier s WHERE s.v_id != 0";
        if (_name != null && _name.length() > 0) {
            query += " AND s.v_name = " + "'" + _name + "'";
        }
        if (_group != null) {
            query += " AND s.v_supplierGroup.v_id = " + "'" + _group.getId() + "'";
        }
        if (_note != null && _note.length() > 0) {
            query += " AND s.v_note = " + "'" + _note + "'";
        }
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList(Environment.getEntityManager()
                                                          .createQuery(query, Supplier.class)
                                                          .getResultList());
        
        return PropertyObject.refreshProperies(suppliers);
    }
    
    public static ObservableList<Supplier> getSuppliersBasedOnGroup(SupplierGroup _group) {
        if (Environment.getEntityManager() == null) {
            Controller.setEntityManager(Environment.getEntityManager());
        }
        return FXCollections.observableArrayList(Environment.getEntityManager().createNamedQuery("Supplier.findByCountry", Supplier.class).setParameter("group", _group).getResultList());
    }
    
    public static void saveSupplier(Supplier _supplier) {
        if (Environment.getEntityManager() == null) {
            Controller.setEntityManager(Environment.getEntityManager());
        }
        Environment.getEntityManager().persist(_supplier);
    }
    
    public static void editSupplier(Supplier _old, Supplier _new) {
        
        for (Address addr : _new.getAddresses()) {
            addr.setSource(_old);
        }
        
        _old.setName(_new.getName());
        _old.setSupplierGroup(_new.getSupplierGroup());
        _old.setAddresses(_new.getAddresses());
        _old.setNote(_new.getNote());
    }
    
    public static void deleteSupplier(Supplier _supplier) {
        Environment.getEntityManager().remove(_supplier);
    }
    
    public SupplierGroup getSupplierGroup() {
        return v_supplierGroup;
    }
    
    public void setSupplierGroup( SupplierGroup _group) {
        v_supplierGroup = _group;
        v_supplierGroupProperty.setValue(v_supplierGroup);
    }
    
    public SimpleObjectProperty supplierGroupProperty() {
        return v_supplierGroupProperty;
    }
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_supplierGroupProperty.setValue(v_supplierGroup);
    }
}
