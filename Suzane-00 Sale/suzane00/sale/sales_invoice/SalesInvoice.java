/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_invoice;

import suzane00.sale.sales_order.*;
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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
import suzane00.sale.sales_payment.SalesPayment;
import suzane00.transaction.model.ItemFileRow;
//import suzane00.sales.model.SOFileRow;
import suzane00.source.Address;
import suzane00.source.Customer;
import suzane00.source.Supplier;
import suzane00.transaction.model.PaymentFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "sales_invoice")
@NamedQueries (
    {
        @NamedQuery(name="SalesInvoice.findAll",
        query="SELECT s FROM SalesInvoice s"),
        @NamedQuery(name="SalesInvoice.findById",
        query="SELECT s FROM SalesInvoice s WHERE s.v_id = :id"),
        @NamedQuery(name="SalesInvoice.findByCustomer",
        query="SELECT s FROM SalesInvoice s WHERE s.v_salesOrder.v_customer = :customer")
    }
)
public class SalesInvoice extends Transaction implements Serializable, PropertyObject{
    private static final long serialVersionUID = 1L;
    
    @ManyToOne
    @JoinColumn(name = "so_id")
    private SalesOrder v_salesOrder;
    @Transient
    SimpleObjectProperty v_salesOrderProperty = new SimpleObjectProperty();
    
    @OneToMany(mappedBy = "v_salesInvoice", cascade = CascadeType.ALL)
    private List<SIFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    @OneToMany(mappedBy = "v_salesInvoice", cascade = CascadeType.ALL)
    private List<SIMiscCharge> v_miscCharges;
    @Transient
    private ObservableList<SimpleObjectProperty> v_miscChargesProperty = FXCollections.observableArrayList();
    
    @OneToMany(mappedBy = "v_salesInvoice", cascade = CascadeType.ALL)
    private List<SIDiscount> v_discounts;
    @Transient
    private ObservableList<SimpleObjectProperty> v_discountsProperty = FXCollections.observableArrayList();
    
    @Column(name = "payment_type")
    private String v_paymentType;
    @Transient
    private SimpleStringProperty v_paymentTypeProperty = new SimpleStringProperty();
    
    public static SalesInvoice getSalesInvoice(long _id) {
        return Environment.getEntityManager().createNamedQuery("SalesInvoice.findById", SalesInvoice.class)
                                             .setParameter("id", _id)
                                             .getSingleResult();
    }
    
    public static ObservableList<SalesInvoice> getByCustomer(Customer _cust) {
        return FXCollections.observableArrayList(Environment.getEntityManager().createNamedQuery("SalesInvoice.findByCustomer", SalesInvoice.class)
                                             .setParameter("customer", _cust)
                                             .getResultList());
    }
    
    public static ObservableList<SalesInvoice> getSISByAttributes(String _numb, SalesOrder _so, String _type, String _note, Calendar _issueDateFrom, Calendar _issueDateTo, Calendar _dueDateFrom, Calendar _dueDateTo) {
        String query = "SELECT s FROM SalesInvoice s WHERE s.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND s.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_so != null) {
            query += " AND s.v_salesOrder = " + _so;
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
        ObservableList<SalesInvoice> sis = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, SalesInvoice.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(sis);
    }
    
    public static void saveSalesInvoice(SalesInvoice _si, ObservableList<SIFile> _files, ObservableList<Cost> _discs) {
        for (SIFile _file : _files) {
            _file.setSalesInvoice(_si);
        }
        _si.setFiles(_files);
        setDiscounts(_si, _discs);
        Environment.getEntityManager().persist(_si);
    }
    
    public static void saveSalesInvoiceByRow(SalesInvoice _si, ObservableList<? extends ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
        buildSalesInvoice(_si, _rows, _discs, _miscs);
        Environment.getEntityManager().persist(_si);
    }
    
    public static void editSalesInvoiceByRow(SalesInvoice _si, ObservableList<? extends ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
        for (SIFile _file : _si.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        for (SIDiscount _disc : _si.getDiscounts()) {
            Environment.getEntityManager().remove(_disc);
        }
        for (SIMiscCharge _misc : _si.getMiscCharges()) {
            Environment.getEntityManager().remove(_misc);
        }
        _si.setFiles(null);
        _si.setDiscounts(null);
        _si.setMiscCharges(null);
        Environment.getEntityManager().flush();
        buildSalesInvoice(_si, _rows, _discs, _miscs);
    }
    
    public static void deleteSalesInvoice(SalesInvoice _si) {
        Environment.getEntityManager().remove(_si);
        Environment.getEntityManager().getTransaction().commit();
    }
    
    public static double getTotalAmountForInvoice(SalesInvoice _si) {
        try {
            Statement stmt = Environment.getConnection().createStatement();
            String sql = "SELECT get_total_amount_for_sales_invoice(" + _si.getId().intValue() + ")";
            ResultSet rs = null ;
            double total = 0;
            rs = stmt.executeQuery(sql);
            rs.next();
            total = rs.getDouble(1);
            return total;
        }
        catch(SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public static ObservableList<PaymentFileRow> convertToRows(List<SalesInvoice> _invoices) {
        ObservableList<PaymentFileRow> rows = FXCollections.observableArrayList();
        for (SalesInvoice _invoice : _invoices) {
            PaymentFileRow row = new PaymentFileRow();
            row.setId(_invoice.getId());
            row.setInvoiceNumber(_invoice.getTransactionNumber());
            row.setTotal(SalesInvoice.getTotalAmountForInvoice(_invoice));
            row.setPaid(SalesPayment.getTotalPaidForInvoice(_invoice));
            row.setAmount(0.0);
            row.setNote("");
            row.setIssueDate(_invoice.getIssueDate());
            row.setDueDate(_invoice.getDueDate());
            rows.add(row);
        }
        return rows;
    }
    
    public SalesOrder getSalesOrder() {
        return v_salesOrder;
    }

    public void setSalesOrder(SalesOrder _so) {
        this.v_salesOrder = _so;
        v_salesOrderProperty.setValue(_so);
    }
    
    public SimpleObjectProperty salesOrderProperty() {
        return v_salesOrderProperty;
    }
    
    public List<SIFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<SIFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (SIFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<SIFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> filesProperty() {
        return v_filesProperty;
    }
    
    public List<SIMiscCharge> getMiscCharges() {
        return v_miscCharges;
    }
    
    public void setMiscCharges(ObservableList<SIMiscCharge> _miscs) {
        v_miscCharges = _miscs;
        if(_miscs == null)
            return;
        for (SIMiscCharge _misc : _miscs) {
            v_miscChargesProperty.add(new SimpleObjectProperty<SIMiscCharge>(_misc));
        }
    }
    
    public ObservableList<SimpleObjectProperty> miscChargesProperty() {
        return v_miscChargesProperty;
    }
    
    public List<SIDiscount> getDiscounts() {
        return v_discounts;
    }
    
    public void setDiscounts(ObservableList<SIDiscount> _discounts) {
        v_discounts = _discounts;
        if(_discounts == null)
            return;
        for (SIDiscount _discount : _discounts) {
            v_discountsProperty.add(new SimpleObjectProperty<SIDiscount>(_discount));
        }
    }
    
    public ObservableList<SimpleObjectProperty> discountsProperty() {
        return v_discountsProperty;
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
        v_salesOrderProperty.setValue(v_salesOrder);
        v_paymentTypeProperty.setValue(v_paymentType);
        
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
            for (SIFile file : v_files) {
                v_filesProperty.add(new SimpleObjectProperty<SIFile>(file));
            }
        }
        if(v_discounts != null) {
            v_discountsProperty = FXCollections.observableArrayList();
            for (SIDiscount disc : v_discounts) {
                v_discountsProperty.add(new SimpleObjectProperty<SIDiscount>(disc));
            }
        }
        if(v_miscCharges != null) {
            v_miscChargesProperty = FXCollections.observableArrayList();
            for (SIMiscCharge charge : v_miscCharges) {
                v_miscChargesProperty.add(new SimpleObjectProperty<SIMiscCharge>(charge));
            }
        }
    }
    
    private static void buildSalesInvoice(SalesInvoice _si, ObservableList<? extends ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
        setFiles(_si, _rows);
        setDiscounts(_si, _discs);
        setMiscCharges(_si, _miscs);
    }

    private static void setDiscounts(SalesInvoice _si, ObservableList<Cost> _discs) {
        ObservableList<SIDiscount> discs = FXCollections.observableArrayList();
        if (_discs == null) {
            return;
        }
        for (Cost disc : _discs) {
            discs.add(new SIDiscount(_si, disc));
        }
        _si.setDiscounts(discs);
    }
    
    private static void setFiles(SalesInvoice _si, ObservableList<? extends ItemFileRow> _rows) {
        ObservableList<SIFile> files = FXCollections.observableArrayList();
        for (ItemFileRow row : _rows) {
            if(row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue ;
            SIFile file = new SIFile();
            ObservableList<SIFileDiscount> discs = FXCollections.observableArrayList();
            file.setPackedItem(Environment.getEntityManager().find(PackedItem.class, new PackedItemPK(row.getId(), row.getUnit().getId())));
//            file.setPriceType(row.getPriceType());
            file.setQuantity(row.getQuantity());
            file.setPrice(row.getPrice());
            if (row.getDiscounts() != null) {
                for (Cost _disc : row.getDiscounts()) {
                    discs.add(new SIFileDiscount(file, _disc));
                }
                file.setDiscounts(discs);
            }
            file.setSalesInvoice(_si);
            files.add(file);
        }
        _si.setFiles(files);
    }
    
    private static void setMiscCharges(SalesInvoice _si, ObservableList<Cost> _miscs) {
        ObservableList<SIMiscCharge> miscs = FXCollections.observableArrayList();
        if (_miscs == null) {
            return;
        }
        for (Cost misc : _miscs) {
            miscs.add(new SIMiscCharge(_si, misc));
        }
        _si.setMiscCharges(miscs);
    }
}
