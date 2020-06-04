/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package suzane00.sale.sales_payment;

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
import suzane00.sale.sales_invoice.SalesInvoice;
import suzane00.transaction.Transaction;
import suzane00.transaction.model.PaymentFileRow;

/**
 *
 * @author Usere
 */
@Entity
@Table(name = "sp_file")
@IdClass(SPFilePK.class)
public class SPFile implements Serializable, PropertyObject {
    
    @Id
    @ManyToOne
    @JoinColumn(name = "sp_id")
    private SalesPayment v_salesPayment;
    @Transient
    SimpleObjectProperty v_salesPaymentProperty = new SimpleObjectProperty();
    
    @Id
    @ManyToOne
    @JoinColumn(name = "si_id")
    private SalesInvoice v_salesInvoice;
    @Transient
    SimpleObjectProperty v_salesInvoiceProperty = new SimpleObjectProperty();
    
    @Column(name = "amount")
    private double v_amount;
    @Transient
    SimpleDoubleProperty v_amountProperty = new SimpleDoubleProperty();
    
    @Column(name = "note")
    private String v_note;
    @Transient
    SimpleStringProperty v_noteProperty = new SimpleStringProperty();
    
    public static ObservableList<PaymentFileRow> convertFilesToRows(List<SPFile> _files) {
        ObservableList<PaymentFileRow> rows = FXCollections.observableArrayList();
        for (SPFile file : _files) {
            PaymentFileRow row = new PaymentFileRow();
            row.setId(file.getSalesInvoice().getId());
            row.setInvoiceNumber(file.getSalesInvoice().getTransactionNumber());
            row.setTotal(SalesInvoice.getTotalAmountForInvoice(file.getSalesInvoice()));
            row.setPaid(SalesPayment.getTotalPaidForInvoice(file.getSalesInvoice()));
            row.setAmount(file.getAmount());
            row.setIssueDate(file.getSalesInvoice().getIssueDate());
            row.setDueDate(file.getSalesInvoice().getDueDate());
            rows.add(row);
        }
        return rows;
    }
    
    public SalesPayment getSalesPayment() {
        return v_salesPayment;
    }

    public void setSalesPayment(SalesPayment _sp) {
        this.v_salesPayment = _sp;
        v_salesPaymentProperty.setValue(v_salesPayment);
    }
    
    public SimpleObjectProperty salesPaymentProperty() {
        return v_salesPaymentProperty;
    }
    
   public SalesInvoice getSalesInvoice() {
        return v_salesInvoice;
    }

    public void setSalesInvoice(SalesInvoice _si) {
        this.v_salesInvoice = _si;
        v_salesInvoiceProperty.setValue(v_salesInvoice);
    }
    
    public SimpleObjectProperty salesInvoiceProperty() {
        return v_salesInvoiceProperty;
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
        v_salesPaymentProperty.setValue(v_salesPayment);
        v_salesInvoiceProperty.setValue(v_salesInvoice);
        v_amountProperty.setValue(v_amount);
        v_noteProperty.setValue(v_note);
    }
}
