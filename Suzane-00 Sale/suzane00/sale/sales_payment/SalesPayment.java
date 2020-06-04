/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_payment;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.Environment;
import suzane00.global.PropertyObject;
import suzane00.global.Utility;
import suzane00.inventory.Cost;
import suzane00.sale.sales_invoice.SIFile;
import suzane00.sale.sales_invoice.SalesInvoice;
import suzane00.source.Customer;
import suzane00.transaction.Transaction;
import suzane00.transaction.model.ItemFileRow;
import suzane00.transaction.model.PaymentFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "sales_payment")
public class SalesPayment extends Transaction implements Serializable, PropertyObject {
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer v_customer;
    @Transient
    SimpleObjectProperty v_customerProperty = new SimpleObjectProperty();
    
    @OneToMany(mappedBy = "v_salesPayment", cascade = CascadeType.ALL)
    private List<SPFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
//    @Column(name = "amount")
//    private double v_amount;
//    @Transient
//    private SimpleDoubleProperty v_amountProperty = new SimpleDoubleProperty();
//    
    
    @Column(name = "payment_type")
    private String v_paymentType;
    @Transient
    private SimpleStringProperty v_paymentTypeProperty = new SimpleStringProperty();
    
    public static ObservableList<SalesPayment> getSPSByAttributes(String _numb, Customer _cust, String _type, String _note, Calendar _issueDateFrom, Calendar _issueDateTo, Calendar _dueDateFrom, Calendar _dueDateTo) {
        String query = "SELECT s FROM SalesPayment s WHERE s.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND s.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_cust != null) {
            query += " AND s.v_customer.v_id = " + _cust.getId();
        }
        if (_type != null && _type.length() > 0) {
            query += " AND s.v_paymentType = " + "'" + _type + "'";
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
        ObservableList<SalesPayment> sis = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, SalesPayment.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(sis);
    }
    
    public static void saveSalesPayment(SalesPayment _sp, ObservableList<SPFile> _files) {
        for (SPFile _file : _files) {
            _file.setSalesPayment(_sp);
        }
        _sp.setFiles(_files);
        Environment.getEntityManager().persist(_sp);
    }
    
    public static void saveSalesPaymentByRow(SalesPayment _sp, ObservableList<? extends PaymentFileRow> _rows) {
        buildSalesPayment(_sp, _rows);
        Environment.getEntityManager().persist(_sp);
    }
    
    public static void editSalesPaymentByRow(SalesPayment _sp, ObservableList<? extends PaymentFileRow> _rows) {
        for (SPFile _file : _sp.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        _sp.setFiles(null);
        Environment.getEntityManager().flush();
        buildSalesPayment(_sp, _rows);
    }
    
    public static void deleteSalesInvoice(SalesPayment _si) {
        Environment.getEntityManager().remove(_si);
        Environment.getEntityManager().getTransaction().commit();
    }
    
    public static double getTotalPaidForInvoice(SalesInvoice _si) {
        try {
            Statement stmt = Environment.getConnection().createStatement();
            String sql = "SELECT SUM(amount) FROm sp_file WHERE si_id = " + _si.getId();
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
    
    public Customer getCustomer() {
        return v_customer;
    }

    public void setCustomer(Customer _cust) {
        this.v_customer = _cust;
        v_customerProperty.setValue(v_customer);
    }
    
    public SimpleObjectProperty customerProperty() {
        return v_customerProperty;
    }
    
    public List<SPFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<SPFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (SPFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<SPFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> filesProperty() {
        return v_filesProperty;
    }
    
//    public double getAmount() {
//        return v_amount;
//    }
//
//    public void setAmount(double _amount) {
//        this.v_amount = _amount;
//        v_amountProperty.setValue(v_amount);
//    }
//    
//     public SimpleDoubleProperty amountProperty() {
//        return v_amountProperty;
//    }
    
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
        v_customerProperty.setValue(v_customer);
        v_paymentTypeProperty.setValue(v_paymentType);
        
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
            for (SPFile file : v_files) {
                v_filesProperty.add(new SimpleObjectProperty<SPFile>(file));
            }
        }
    }
    
     private static void buildSalesPayment(SalesPayment _sp, ObservableList<? extends PaymentFileRow> _rows) {
        setFiles(_sp, _rows);
    }
     
     private static void setFiles(SalesPayment _sp, ObservableList<? extends PaymentFileRow> _rows) {
        ObservableList<SPFile> files = FXCollections.observableArrayList();
        for (PaymentFileRow row : _rows) {
            if(row.getAmount()< Utility.DOUBLE_TOLERANCE)
                continue ;
            SPFile file = new SPFile();
            file.setSalesInvoice(SalesInvoice.getSalesInvoice(row.getId()));
            file.setAmount(row.getAmount());
            file.setNote(row.getNote());
            file.setSalesPayment(_sp);
            files.add(file);
        }
        _sp.setFiles(files);
    }
}
