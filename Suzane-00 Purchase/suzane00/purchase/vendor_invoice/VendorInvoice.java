/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.vendor_invoice;

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
import suzane00.purchase.purchase_order.PurchaseOrder;
import suzane00.purchase.vendor_payment.VendorPayment;
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
@Table(name = "vendor_invoice")
@NamedQueries (
    {
        @NamedQuery(name="VendorInvoice.findAll",
        query="SELECT v FROM VendorInvoice v"),
        @NamedQuery(name="VendorInvoice.findById",
        query="SELECT v FROM VendorInvoice v WHERE v.v_id = :id"),
        @NamedQuery(name="VendorInvoice.findBySupplier",
        query="SELECT v FROM VendorInvoice v WHERE v.v_purchaseOrder.v_supplier = :supplier")
    }
)
public class VendorInvoice extends Transaction implements Serializable, PropertyObject{
    private static final long serialVersionUID = 1L;
    

    @ManyToOne
    @JoinColumn(name = "po_id")
    private PurchaseOrder v_purchaseOrder;
    @Transient
    SimpleObjectProperty v_purchaseOrderProperty = new SimpleObjectProperty();
    
    @OneToMany(mappedBy = "v_vendorInvoice", cascade = CascadeType.ALL)
    private List<VIFile> v_files;
    @Transient
    private ObservableList<SimpleObjectProperty> v_filesProperty = FXCollections.observableArrayList();
    
    @OneToMany(mappedBy = "v_vendorInvoice", cascade = CascadeType.ALL)
    private List<VIMiscCharge> v_miscCharges;
    @Transient
    private ObservableList<SimpleObjectProperty> v_miscChargesProperty = FXCollections.observableArrayList();
    
    
    @OneToMany(mappedBy = "v_vendorInvoice", cascade = CascadeType.ALL)
    private List<VIDiscount> v_discounts;
    @Transient
    private ObservableList<SimpleObjectProperty> v_discountsProperty = FXCollections.observableArrayList();
    
    @Column(name = "payment_type")
    private String v_paymentType;
    @Transient
    private SimpleStringProperty v_paymentTypeProperty = new SimpleStringProperty();
    
    public static VendorInvoice getVendorInvoice(long _id) {
        return Environment.getEntityManager().createNamedQuery("VendorInvoice.findById", VendorInvoice.class)
                                             .setParameter("id", _id)
                                             .getSingleResult();
    }
    
    public static ObservableList<VendorInvoice> getBySupplier(Supplier _supplier) {
        return FXCollections.observableArrayList(Environment.getEntityManager().createNamedQuery("VendorInvoice.findBySupplier", VendorInvoice.class)
                                             .setParameter("supplier", _supplier)
                                             .getResultList());
    }
    
    
    public static ObservableList<VendorInvoice> getVISByAttributes(String _numb, PurchaseOrder _po, 
            String _type, String _note, Calendar _issueDateFrom, Calendar _issueDateTo, 
            Calendar _dueDateFrom, Calendar _dueDateTo) {
        String query = "SELECT v FROM VendorInvoice v WHERE v.v_id != 0";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (_numb != null && _numb.length() > 0) {
            query += " AND v.v_transactionNumber = " + "'" + _numb + "'";
        }
        if (_po != null) {
            query += " AND v.v_purchaseOrder = " + _po;
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
            query += " AND i.v_issueDate <= '" + sdf.format(issueDateTo) + "'";
        }
        if (_dueDateFrom != null) {
            Date dueDateFrom = new Date(_dueDateFrom.getTimeInMillis());
            query += " AND v.v_dueDate >= '" + sdf.format(dueDateFrom) + "'";
        }
        if (_dueDateTo != null) {
            Date dueDateTo = new Date(_dueDateTo.getTimeInMillis());
            query += " AND v.v_dueDate <= '" + sdf.format(dueDateTo) + "'";
        }
        ObservableList<VendorInvoice> vis = FXCollections.observableArrayList(Environment.getEntityManager()
                                                      .createQuery(query, VendorInvoice.class)
                                                      .getResultList());
        
        return PropertyObject.refreshProperies(vis);
    }
    
    public static void saveVendorInvoice(VendorInvoice _si, ObservableList<VIFile> _files, ObservableList<Cost> _discs) {
        for (VIFile _file : _files) {
            _file.setVendorInvoice(_si);
        }
        _si.setFiles(_files);
        setDiscounts(_si, _discs);
        Environment.getEntityManager().persist(_si);
    }
    
    public static void saveVendorInvoiceByRow(VendorInvoice _vi, ObservableList<? extends ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
        buildVendorInvoice(_vi, _rows, _discs, _miscs);
        Environment.getEntityManager().persist(_vi);
    }
    
    public static void editVendorInvoiceByRow(VendorInvoice _vi, ObservableList<? extends ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
        for (VIFile _file : _vi.getFiles()) {
            Environment.getEntityManager().remove(_file);
        }
        for (VIDiscount _disc : _vi.getDiscounts()) {
            Environment.getEntityManager().remove(_disc);
        }
        for (VIMiscCharge _misc : _vi.getMiscCharges()) {
            Environment.getEntityManager().remove(_misc);
        }
        _vi.setFiles(null);
        _vi.setDiscounts(null);
        _vi.setMiscCharges(null);
        Environment.getEntityManager().flush();
        buildVendorInvoice(_vi, _rows, _discs, _miscs);
    }
    
    public static void deleteVendorInvoice(VendorInvoice _vi) {
        Environment.getEntityManager().remove(_vi);
        Environment.getEntityManager().getTransaction().commit();
    }
    
    public static double getTotalAmountForInvoice(VendorInvoice _vi) {
        try {
            Statement stmt = Environment.getConnection().createStatement();
            String sql = "SELECT get_total_amount_for_vendor_invoice(" + _vi.getId().intValue() + ")";
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
    
    public static ObservableList<PaymentFileRow> convertToRows(List<VendorInvoice> _invoices) {
        ObservableList<PaymentFileRow> rows = FXCollections.observableArrayList();
        for (VendorInvoice _invoice : _invoices) {
            PaymentFileRow row = new PaymentFileRow();
            row.setId(_invoice.getId());
            row.setInvoiceNumber(_invoice.getTransactionNumber());
            row.setTotal(VendorInvoice.getTotalAmountForInvoice(_invoice));
            row.setPaid(VendorPayment.getTotalPaidForInvoice(_invoice));
            row.setAmount(0);
            row.setNote("");
            row.setIssueDate(_invoice.getIssueDate());
            row.setDueDate(_invoice.getDueDate());
            rows.add(row);
        }
        return rows;
    }
    
    public PurchaseOrder getPurchaseOrder() {
        return v_purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder _so) {
        this.v_purchaseOrder = _so;
        v_purchaseOrderProperty.setValue(_so);
    }
    
    public SimpleObjectProperty purchaseOrderProperty() {
        return v_purchaseOrderProperty;
    }
    
    public List<VIFile> getFiles() {
        return v_files;
    }
    
    public void setFiles(ObservableList<VIFile> _files) {
        v_files = _files;
        if(_files == null)
            return;
        for (VIFile _file : _files) {
            v_filesProperty.add(new SimpleObjectProperty<VIFile>(_file));
        }
    }
    
    public ObservableList<SimpleObjectProperty> filesProperty() {
        return v_filesProperty;
    }
    
    public List<VIMiscCharge> getMiscCharges() {
        return v_miscCharges;
    }
    
    public void setMiscCharges(ObservableList<VIMiscCharge> _miscs) {
        v_miscCharges = _miscs;
        if(_miscs == null)
            return;
        for (VIMiscCharge _misc : _miscs) {
            v_miscChargesProperty.add(new SimpleObjectProperty<VIMiscCharge>(_misc));
        }
    }
    
    public ObservableList<SimpleObjectProperty> miscChargesProperty() {
        return v_miscChargesProperty;
    }
    
    public List<VIDiscount> getDiscounts() {
        return v_discounts;
    }
    
    public void setDiscounts(ObservableList<VIDiscount> _discounts) {
        v_discounts = _discounts;
        if(_discounts == null)
            return;
        for (VIDiscount _discount : _discounts) {
            v_discountsProperty.add(new SimpleObjectProperty<VIDiscount>(_discount));
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
        v_purchaseOrderProperty.setValue(v_purchaseOrder);
        if(v_files != null) {
            v_filesProperty = FXCollections.observableArrayList();
                for (VIFile file : v_files) {
                    v_filesProperty.add(new SimpleObjectProperty<VIFile>(file));
                }
        }
        if(v_discounts != null) {
            v_discountsProperty = FXCollections.observableArrayList();
            for (VIDiscount disc : v_discounts) {
                v_discountsProperty.add(new SimpleObjectProperty<VIDiscount>(disc));
            }
        }
        if(v_miscCharges != null) {
            v_miscChargesProperty = FXCollections.observableArrayList();
            for (VIMiscCharge charge : v_miscCharges) {
                v_miscChargesProperty.add(new SimpleObjectProperty<VIMiscCharge>(charge));
            }
        }
    }
    
    private static void buildVendorInvoice(VendorInvoice _si, ObservableList<? extends ItemFileRow> _rows, ObservableList<Cost> _discs, ObservableList<Cost> _miscs) {
        setFiles(_si, _rows);
        setDiscounts(_si, _discs);
        setMiscCharges(_si, _miscs);
    }

    private static void setDiscounts(VendorInvoice _si, ObservableList<Cost> _discs) {
        ObservableList<VIDiscount> discs = FXCollections.observableArrayList();
        if (_discs == null) {
            return;
        }
        for (Cost disc : _discs) {
            discs.add(new VIDiscount(_si, disc));
        }
        _si.setDiscounts(discs);
    }
    
    private static void setFiles(VendorInvoice _si, ObservableList<? extends ItemFileRow> _rows) {
        ObservableList<VIFile> files = FXCollections.observableArrayList();
        for (ItemFileRow row : _rows) {
            if(row.getQuantity() < Utility.DOUBLE_TOLERANCE)
                continue;
            VIFile file = new VIFile();
            ObservableList<VIFileDiscount> discs = FXCollections.observableArrayList();
            file.setPackedItem(Environment.getEntityManager().find(PackedItem.class, new PackedItemPK(row.getId(), row.getUnit().getId())));
            file.setQuantity(row.getQuantity());
            file.setPrice(row.getPrice());
            if (row.getDiscounts() != null) {
                for (Cost _disc : row.getDiscounts()) {
                    discs.add(new VIFileDiscount(file, _disc));
                }
                file.setDiscounts(discs);
            }
            file.setVendorInvoice(_si);
            files.add(file);
        }
        _si.setFiles(files);
    }
    
    private static void setMiscCharges(VendorInvoice _si, ObservableList<Cost> _miscs) {
        ObservableList<VIMiscCharge> miscs = FXCollections.observableArrayList();
        if (_miscs == null) {
            return;
        }
        for (Cost misc : _miscs) {
            miscs.add(new VIMiscCharge(_si, misc));
        }
        _si.setMiscCharges(miscs);
    }
}
