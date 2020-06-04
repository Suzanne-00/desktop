/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.purchase.vendor_payment;

import java.io.Serializable;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import suzane00.global.PropertyObject;
import suzane00.purchase.vendor_invoice.VendorInvoice;
import suzane00.transaction.Transaction;
import suzane00.transaction.model.PaymentFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "vp_file")
@IdClass(VPFilePK.class)
public class VPFile implements Serializable, PropertyObject {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "vp_id")
    private VendorPayment v_vendorPayment;
    @Transient
    SimpleObjectProperty v_vendorPaymentProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumn(name = "vi_id")
    private VendorInvoice v_vendorInvoice;
    @Transient
    SimpleObjectProperty v_vendorinvoiceProperty = new SimpleObjectProperty();
    
    @Column(name = "amount")
    private double v_amount;
    @Transient
    SimpleDoubleProperty v_amountProperty = new SimpleDoubleProperty();
    
    @Column(name = "note")
    private String v_note;
    @Transient
    SimpleStringProperty v_noteProperty = new SimpleStringProperty();
    
    public static ObservableList<PaymentFileRow> convertFilesToRows(List<VPFile> _files) {
        ObservableList<PaymentFileRow> rows = FXCollections.observableArrayList();
        for (VPFile file : _files) {
            PaymentFileRow row = new PaymentFileRow();
            row.setId(file.getVendorInvoice().getId());
            row.setInvoiceNumber(file.getVendorInvoice().getTransactionNumber());
            row.setTotal(VendorInvoice.getTotalAmountForInvoice(file.getVendorInvoice()));
            row.setPaid(VendorPayment.getTotalPaidForInvoice(file.getVendorInvoice()));
            row.setAmount(file.getAmount());
            row.setIssueDate(file.getVendorInvoice().getIssueDate());
            row.setDueDate(file.getVendorInvoice().getDueDate());
            rows.add(row);
        }
        return rows;
    }
    
    public VendorPayment getVendorPayment() {
        return v_vendorPayment;
    }

    public void setVendorPayment(VendorPayment _vp) {
        this.v_vendorPayment = _vp;
        v_vendorPaymentProperty.setValue(v_vendorPayment);
    }
    
    public SimpleObjectProperty vendorPaymentProperty() {
        return v_vendorPaymentProperty;
    }
    
   public VendorInvoice getVendorInvoice() {
        return v_vendorInvoice;
    }

    public void setVendorInvoice(VendorInvoice _si) {
        this.v_vendorInvoice = _si;
        v_vendorinvoiceProperty.setValue(v_vendorInvoice);
    }
    
    public SimpleObjectProperty vendorInvoiceProperty() {
        return v_vendorinvoiceProperty;
    }
    
    public double getAmount() {
        return v_amount;
    }

    public void setAmount(double _amount) {
        this.v_amount = _amount;
        v_amountProperty.setValue(v_amount);
    }
    
    public SimpleDoubleProperty amountProperty() {
        return v_amountProperty;
    }
    
    public String getNote() {
        return v_note;
    }

    public void setNote(String _note) {
        this.v_note = _note;
        v_noteProperty.setValue(v_note);
    }
    
    public SimpleStringProperty noteProperty() {
        return v_noteProperty;
    }

    @Override
    public void refreshProperty() {
        v_vendorPaymentProperty.setValue(v_vendorPayment);
        v_vendorinvoiceProperty.setValue(v_vendorInvoice);
        v_amountProperty.setValue(v_amount);
        v_noteProperty.setValue(v_note);
    }
}
