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
@Table(name = "customer")
@NamedQueries(
    {
        @NamedQuery(name="Customer.findAll",
        query="SELECT c FROM Customer c"),
        @NamedQuery(name="Customer.findById",
        query="SELECT c FROM Customer c WHERE c.v_id = :id"),
        @NamedQuery(name="Customer.findByName",
        query="SELECT c FROM Customer c WHERE c.v_name = :name"),
        @NamedQuery(name="Customer.findByGroup",
        query="SELECT c FROM Customer c WHERE c.v_group = :group"),
    }
)
public class Customer extends Source implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "customer_group_id")
    private CustomerGroup v_group;
    @Transient
    SimpleObjectProperty v_groupProperty = new SimpleObjectProperty();
    
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList(Environment.getEntityManager()
                                                          .createNamedQuery("Customer.findAll", Customer.class)
                                                          .getResultList());
        
        return PropertyObject.refreshProperies(customers);
    }
    
    public static ObservableList<Customer> getCustomersByAttributes(String _name, CustomerGroup _group, String _note) {
        String query = "SELECT c FROM Customer c WHERE c.v_id != 0";
        if (_name != null && _name.length() > 0) {
            query += " AND c.v_name = " + "'" + _name + "'";
        }
        if (_group != null) {
            query += " AND c.v_group.v_id = " +  _group.getId() ;
        }
        if (_note != null && _note.length() > 0) {
            query += " AND c.v_note = " + "'" + _note + "'";
        }
        ObservableList<Customer> customers = FXCollections.observableArrayList(Environment.getEntityManager()
                                                           .createQuery(query, Customer.class)
                                                           .getResultList());
        
        return PropertyObject.refreshProperies(customers);
    }
    
    public static ObservableList<Customer> getCustomersBasedOnGroup(CustomerGroup _group) {
        ObservableList<Customer> customers = FXCollections.observableArrayList(Environment.getEntityManager()
                            .createNamedQuery("Customer.findByCountry", Customer.class)
                            .setParameter("group", _group)
                            .getResultList());
        
        return PropertyObject.refreshProperies(customers);
    }
    
    public static void saveCustomer(Customer _cust) {
        Environment.getEntityManager().persist(_cust);
    }
    
    public static void editCustomer(Customer _old, Customer _new) {
        for (Address addr : _new.getAddresses()) {
            addr.setSource(_old);
        }
        
        _old.setName(_new.getName());
        _old.setGroup(_new.getGroup());
        _old.setAddresses(_new.getAddresses());
        _old.setNote(_new.getNote());
    }
    
    public static void deleteCustomer(Customer _cust) {
        Environment.getEntityManager().remove(_cust);
    }
    
    public CustomerGroup getGroup() {
        return v_group;
    }
    
    public void setGroup(CustomerGroup _group) {
        v_group = _group;
        v_groupProperty.setValue(v_group);
    }
    
    public SimpleObjectProperty groupProperty() {
        return v_groupProperty;
    }
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_groupProperty.setValue(v_group);
    }
}
