/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.vendor_payment;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
import suzane00.global.Utility;
import suzane00.inventory.Cost;
import suzane00.purchase.vendor_invoice.VendorInvoice;
import suzane00.source.Address;
import suzane00.source.Customer;
import suzane00.source.Supplier;
import suzane00.transaction.Transaction;
import suzane00.transaction.model.ItemFileRow;
import suzane00.transaction.model.PaymentFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "vendor_payment")
public class VendorPayment extends Transaction implements Serializable, PropertyObject {
    
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier v_supplier;
    @Transient
    SimpleObjectProperty v_supplierProperty = new SimpleObjectProperty();
    
     @ManyToOne
    @JoinColumns({
        @JoinColumn( name = "city_id", referencedColumnName = "city_id"),
        @JoinColumn( name = "street_name", referencedColumnName = "street_name") })    
    private Address v_address;
    @Transient
    SimpleObjectProperty v_addressProperty = new SimpleObjectProperty();
    
    
    @OneToMany(mappedBy = "v_vendorPayment", cascade = CascadeType.ALL)
    private List<VPFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    @Column(name = "payment_type")
    private String v_paymentType;
    @Transient
    private SimpleStringProperty v_paymentTypeProperty = new SimpleStringProperty();
    
   
    
    public static ObservableList<VendorPayment> getVPSByAttributes(String _numb, Supplier _supplier, 
            Address _address, String _type, String _note, Calendar _issueDateFrom, 
            Calendar _issueDateTo, Calendar _dueDateFrom, Calendar _dueDateTo) {
        String query = "SELECT v FROM VendorPayment v WHERE v.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND v.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_supplier != null) {
            query += " AND v.v_supplier.v_id = " + _supplier.getId();
        }
        if (_address != null) {
            query += " AND v.v_address.v_city.v_id = " + _address.getCity().getId() +
                     " AND v.v_address.v_streetname = '" + _address.getStreetName() + "' ";
        }
        if (_type != null && _type.length() > 0) {
            query += " AND v.v_paymentType = " + "'" + _type + "'";
        }
        if (_note != null && _note.length() > 0) {
            query += " AND v.v_note = " + "'" + _note + "'";
        }
        if (_issueDateFrom != null) {
            Date issueDateFrom = new Date(_issueDateFrom.getTimeInMillis());
            query += " AND v.v_issueDate >= '" + sdf.format(issueDateFrom) + "'";
        }
        if (_issueDateTo != null) {
            Date issueDateTo = new Date(_issueDateTo.getTimeInMillis());
            query += " AND v.v_issueDate <= '" + sdf.format(issueDateTo) + "'";
        }
        if (_dueDateFrom != null) {
            Date dueDateFrom = new Date(_dueDateFrom.getTimeInMillis());
            query += " AND v.v_dueDate >= '" + sdf.format(dueDateFrom) + "'";
        }
        if (_dueDateTo != null) {
            Date dueDateTo = new Date(_dueDateTo.getTimeInMillis());
            query += " AND v.v_dueDate <= '" + sdf.format(dueDateTo) + "'";
        }
        ObservableList<VendorPayment> sis = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, VendorPayment.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(sis);
    }
    
    public static void saveSalesPayment(VendorPayment _sp, ObservableList<VPFile> _files) {
        for (VPFile _file : _files) {
            _file.setVendorPayment(_sp);
        }
        _sp.setFiles(_files);
        Environment.getEntityManager().persist(_sp);
    }
    
    public static void saveSalesPaymentByRow(VendorPayment _sp, ObservableList<? extends PaymentFileRow> _rows) {
        buildVendorPayment(_sp, _rows);
        Environment.getEntityManager().persist(_sp);
    }
    
    public static void editSalesPaymentByRow(VendorPayment _sp, ObservableList<? extends PaymentFileRow> _rows) {
        for (VPFile _file : _sp.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        _sp.setFiles(null);
        Environment.getEntityManager().flush();
        buildVendorPayment(_sp, _rows);
    }
    
    public static void deleteVendorPayment(VendorPayment _si) {
        Environment.getEntityManager().remove(_si);
        Environment.getEntityManager().getTransaction().commit();
    }
    
    public static double getTotalPaidForInvoice(VendorInvoice _vi) {
        try {
            Statement stmt = Environment.getConnection().createStatement();
            String sql = "SELECT SUM(amount) FROm vp_file WHERE vi_id = " + _vi.getId();
            ResultSet rs = null ;
            double paid = 0;
            rs = stmt.executeQuery(sql);
            rs.next();
            paid = rs.getDouble(1);
            return paid;
        }
        catch(SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public Supplier getSupplier() {
        return v_supplier;
    }

    public void setSupplier(Supplier _sup) {
        this.v_supplier = _sup;
        v_supplierProperty.setValue(v_supplier);
    }
    
    public SimpleObjectProperty supplierProperty() {
        return v_supplierProperty;
    }
    
    public Address getAddress() {
        return v_address;
    }

    public void setAddress(Address _address) {
        this.v_address = _address;
        v_addressProperty.setValue(v_address);
    }
    
    public SimpleObjectProperty addressProperty() {
        return v_addressProperty;
    }
    
    public List<VPFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<VPFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (VPFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<VPFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> filesProperty() {
        return v_filesProperty;
    }
    
    public String getPaymentType() {
        return v_paymentType;
    }

    public void setPaymentType(String _type) {
        this.v_paymentType = _type;
        v_paymentTypeProperty.setValue(_type);
    }
    
     public SimpleStringProperty paymentTypeProperty() {
        return v_paymentTypeProperty;
    }
    
    @Override
    public void refreshProperty() {
        super.refreshProperty();
        v_supplierProperty.setValue(v_supplier);
        v_addressProperty.setValue(v_address);
        v_paymentTypeProperty.setValue(v_paymentType);
        
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
            for (VPFile file : v_files) {
                v_filesProperty.add(new SimpleObjectProperty<VPFile>(file));
            }
        }
    }
    
     private static void buildVendorPayment(VendorPayment _sp, ObservableList<? extends PaymentFileRow> _rows) {
        setFiles(_sp, _rows);
    }
     
     private static void setFiles(VendorPayment _sp, ObservableList<? extends PaymentFileRow> _rows) {
        ObservableList<VPFile> files = FXCollections.observableArrayList();
        for (PaymentFileRow row : _rows) {
            if(row.getAmount()< Utility.DOUBLE_TOLERANCE)
                continue ;
            VPFile file = new VPFile();
            file.setVendorInvoice(VendorInvoice.getVendorInvoice(row.getId()));
            file.setAmount(row.getAmount());
            file.setNote(row.getNote());
            file.setVendorPayment(_sp);
            files.add(file);
        }
        _sp.setFiles(files);
    }
}
