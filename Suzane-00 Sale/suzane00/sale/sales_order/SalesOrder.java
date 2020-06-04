/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_order;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
import suzane00.global.Utility;
import suzane00.transaction.Transaction;
import suzane00.inventory.Area;
import suzane00.inventory.Cost;
import suzane00.inventory.PackedItem;
import suzane00.inventory.PackedItemPK;
import suzane00.transaction.model.ItemFileRow;
//import suzane00.sales.model.SOFileRow;
import suzane00.source.Address;
import suzane00.source.Customer;
import suzane00.source.Supplier;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "sales_order")
public class SalesOrder extends Transaction implements Serializable, PropertyObject{
    private static final long serialVersionUID = 1L;

    @Column(name = "customer_po")
    private String v_customerPO;
    @Transient
    SimpleStringProperty v_customerPOProperty = new SimpleStringProperty();
    
    @OneToMany(mappedBy = "v_salesOrder", cascade = CascadeType.ALL)
    private List<SOFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    @OneToMany(mappedBy = "v_salesOrder", cascade = CascadeType.ALL)
    private List<SOMiscCharge> v_miscCharges;
    @Transient
    private ObservableList<SimpleObjectProperty> v_miscChargesProperty = FXCollections.observableArrayList();
    
    
    @OneToMany(mappedBy = "v_salesOrder", cascade = CascadeType.ALL)
    private List<SODiscount> v_discounts;
    @Transient
    private ObservableList<SimpleObjectProperty> v_discountsProperty = FXCollections.observableArrayList();
    
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer v_customer;
    @Transient
    private SimpleObjectProperty v_customerProperty = new SimpleObjectProperty();
  
    
    public static ObservableList<SalesOrder> getSOSByAttributes(String _numb, String _custPO, 
            Customer _cust, String _note, Calendar _issueDateFrom, Calendar _issueDateTo, Calendar _dueDateFrom, Calendar _dueDateTo) {
        
        String query = "SELECT s FROM SalesOrder s WHERE s.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND s.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_custPO != null && _custPO.length() > 0) {
            query += " AND s.v_customerPO = " + "'" + _custPO + "'";
        }
        if (_cust != null) {
            query += " AND s.v_customer = " + _cust;
        }
        if (_note != null && _note.length() > 0) {
            query += " AND s.v_note = " + "'" + _note + "'";
        }
        if (_issueDateFrom != null) {
            Date issueDateFrom = new Date(_issueDateFrom.getTimeInMillis());
            query += " AND s.v_issueDate >= '" + sdf.format(issueDateFrom) + "'";
        }
        if (_issueDateTo != null) {
            Date issueDateTo = new Date(_issueDateTo.getTimeInMillis());
            query += " AND s.v_issueDate <= '" + sdf.format(issueDateTo) + "'";
        }
        if (_dueDateFrom != null) {
            Date dueDateFrom = new Date(_dueDateFrom.getTimeInMillis());
            query += " AND s.v_dueDate >= '" + sdf.format(dueDateFrom) + "'";
        }
        if (_dueDateTo != null) {
            Date dueDateTo = new Date(_dueDateTo.getTimeInMillis());
            query += " AND s.v_dueDate <= '" + sdf.format(dueDateTo) + "'";
        }
        ObservableList<SalesOrder> sos = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, SalesOrder.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(sos);
    }
    
    public static void saveSalesOrder(SalesOrder _so, ObservableList<SOFile> _files, ObservableList<Cost> _discs) {
        for (SOFile _file : _files) {
            _file.setSalesOrder(_so);
        }
        _so.setFiles(_files);
        setDiscounts(_so, _discs);
        Environment.getEntityManager().persist(_so);
    }
    
    public static void saveSalesOrderByRow(SalesOrder _so, ObservableList<ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
        buildSalesOrder(_so, _rows, _discs, _miscs);
        Environment.getEntityManager().persist(_so);
    }
    
    public static void editSalesOrderByRow(SalesOrder _so, ObservableList<ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
        for (SOFile _file : _so.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        for (SODiscount _disc : _so.getDiscounts()) {
            Environment.getEntityManager().remove(_disc);
        }
        for (SOMiscCharge _misc : _so.getMiscCharges()) {
            Environment.getEntityManager().remove(_misc);
        }
        _so.setFiles(null);
        _so.setDiscounts(null);
        _so.setMiscCharges(null);
        Environment.getEntityManager().flush();
        buildSalesOrder(_so, _rows, _discs, _miscs);
    }
    
    public static void deleteSalesOrder(SalesOrder _so) {
        Environment.getEntityManager().remove(_so);
        Environment.getEntityManager().getTransaction().commit();
    }
    
    public String getCustomerPO() {
        return v_customerPO;
    }

    public void setCustomerPO(String _po) {
        this.v_customerPO = _po;
        v_customerPOProperty.setValue(_po);
    }
    
     public SimpleStringProperty customerPOProperty() {
        return v_customerPOProperty;
    }
    
    
    public List<SOFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<SOFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (SOFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<SOFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> filesProperty() {
        return v_filesProperty;
    }
    
    public List<SOMiscCharge> getMiscCharges() {
        return v_miscCharges;
    }
    
    public void setMiscCharges(ObservableList<SOMiscCharge> _miscs) {
        v_miscCharges = _miscs;
        if(_miscs == null)
            return;
        for (SOMiscCharge _misc : _miscs) {
            v_miscChargesProperty.add(new SimpleObjectProperty<SOMiscCharge>(_misc));
        }
    }
    
    public ObservableList<SimpleObjectProperty> miscChargesProperty() {
        return v_miscChargesProperty;
    }
    
    public List<SODiscount> getDiscounts() {
        return v_discounts;
    }
    
    public void setDiscounts(ObservableList<SODiscount> _discounts) {
        v_discounts = _discounts;
        if(_discounts == null)
            return;
        for (SODiscount _discount : _discounts) {
            v_discountsProperty.add(new SimpleObjectProperty<SODiscount>(_discount));
        }
    }
    
    public ObservableList<SimpleObjectProperty> discountsProperty() {
        return v_discountsProperty;
    }
        
    public Customer getCustomer() {
        return v_customer;
    }

    public void setCustomer(Customer _customer) {
        this.v_customer = _customer;
        v_customerProperty.setValue(v_customer);
    }
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_customerProperty.setValue(v_customer);
        v_customerPOProperty.setValue(v_customerPO);
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
            for (SOFile file : v_files) {
                v_filesProperty.add(new SimpleObjectProperty<SOFile>(file));
            }
        }
        if(v_discounts != null) {
            v_discountsProperty = FXCollections.observableArrayList();
            for (SODiscount disc : v_discounts) {
                v_discountsProperty.add(new SimpleObjectProperty<SODiscount>(disc));
            }
        }
        if(v_miscCharges != null) {
            v_miscChargesProperty = FXCollections.observableArrayList();
            for (SOMiscCharge charge : v_miscCharges) {
                v_miscChargesProperty.add(new SimpleObjectProperty<SOMiscCharge>(charge));
            }
        }
    }
    
    private static void buildSalesOrder(SalesOrder _so, ObservableList<ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
        setFiles(_so, _rows);
        setDiscounts(_so, _discs);
        setMiscCharges(_so, _miscs);
    }

    private static void setDiscounts(SalesOrder _so, ObservableList<Cost> _discs) {
        ObservableList<SODiscount> discs = FXCollections.observableArrayList();
        if (_discs == null) {
            return;
        }
        for (Cost disc : _discs) {
            discs.add(new SODiscount(_so, disc));
        }
        _so.setDiscounts(discs);
    }
    
    private static void setFiles(SalesOrder _so, ObservableList<ItemFileRow> _rows) {
        ObservableList<SOFile> files = FXCollections.observableArrayList();
        for (ItemFileRow row : _rows) {
            if(row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            SOFile file = new SOFile();
            ObservableList<SOFileDiscount> discs = FXCollections.observableArrayList();
            file.setPackedItem(Environment.getEntityManager().find(PackedItem.class, new PackedItemPK(row.getId(), row.getUnit().getId())));
            file.setQuantity(row.getQuantity());
            file.setPrice(row.getPrice());
            if (row.getDiscounts() != null) {
                for (Cost _disc : row.getDiscounts()) {
                    discs.add(new SOFileDiscount(file, _disc));
                }
                file.setDiscounts(discs);
            }
            file.setSalesOrder(_so);
            files.add(file);
        }
        _so.setFiles(files);
    }
    
    private static void setMiscCharges(SalesOrder _so, ObservableList<Cost> _miscs) {
        ObservableList<SOMiscCharge> miscs = FXCollections.observableArrayList();
        if (_miscs == null) {
            return;
        }
        for (Cost misc : _miscs) {
            miscs.add(new SOMiscCharge(_so, misc));
        }
        _so.setMiscCharges(miscs);
    }
}
